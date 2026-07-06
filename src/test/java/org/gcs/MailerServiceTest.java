package org.gcs;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class MailerServiceTest {

    private String validMailersJson;
    private String singleMailerJson;
    private String invalidJson;

    @Before
    public void setUp() {
        // Valid JSON with one mailer containing AllowedMailers
        validMailersJson = "[\n" +
                "  {\n" +
                "    \"url\": \"smtp.gmail.com\",\n" +
                "    \"recipients\": [\n" +
                "      \"john@example.com\",\n" +
                "      \"jane@example.com\"\n" +
                "    ],\n" +
                "    \"cc\": [\n" +
                "      \"manager@example.com\"\n" +
                "    ],\n" +
                "    \"allowedMailers\": [\n" +
                "      {\n" +
                "        \"email\": \"support@gmail.com\",\n" +
                "        \"validFrom\": \"2024-01-01T08:00:00\",\n" +
                "        \"validUntil\": \"2025-01-01T08:00:00\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"email\": \"admin@gmail.com\",\n" +
                "        \"validFrom\": \"2024-01-01T08:00:00\",\n" +
                "        \"validUntil\": \"2025-06-30T23:59:59\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        singleMailerJson = "[\n" +
                "  {\n" +
                "    \"url\": \"smtp.outlook.com\",\n" +
                "    \"recipients\": [\"alice@company.com\"],\n" +
                "    \"cc\": [\"lead@company.com\"],\n" +
                "    \"allowedMailers\": [\n" +
                "      {\n" +
                "        \"email\": \"support@outlook.com\",\n" +
                "        \"validFrom\": \"2024-01-01T00:00:00\",\n" +
                "        \"validUntil\": \"2024-12-31T23:59:59\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        invalidJson = "{ invalid json }";
    }

    @Test
    public void testReadMailersFromString_ValidJson() throws Exception {
        List<Mailer> mailers = MailerService.readMailersFromString(validMailersJson);

        assertNotNull(mailers);
        assertEquals(1, mailers.size());

        Mailer mailer = mailers.get(0);
        assertEquals("smtp.gmail.com", mailer.getUrl());
        assertEquals(2, mailer.getRecipients().size());
        assertEquals("john@example.com", mailer.getRecipients().get(0));
        assertEquals(1, mailer.getCc().size());
        assertEquals("manager@example.com", mailer.getCc().get(0));
    }

    @Test
    public void testReadMailersFromString_AllowedMailers() throws Exception {
        List<Mailer> mailers = MailerService.readMailersFromString(validMailersJson);

        assertNotNull(mailers);
        Mailer mailer = mailers.get(0);
        List<AllowedMailers> allowedMailers = mailer.getAllowedMailers();

        assertNotNull(allowedMailers);
        assertEquals(2, allowedMailers.size());

        AllowedMailers am1 = allowedMailers.get(0);
        assertEquals("support@gmail.com", am1.getEmail());
        assertEquals(LocalDateTime.parse("2024-01-01T08:00:00"), am1.getValidFrom());
        assertEquals(LocalDateTime.parse("2025-01-01T08:00:00"), am1.getValidUntil());

        AllowedMailers am2 = allowedMailers.get(1);
        assertEquals("admin@gmail.com", am2.getEmail());
        assertEquals(LocalDateTime.parse("2024-01-01T08:00:00"), am2.getValidFrom());
        assertEquals(LocalDateTime.parse("2025-06-30T23:59:59"), am2.getValidUntil());
    }

    @Test
    public void testReadMailersFromInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(validMailersJson.getBytes());
        List<Mailer> mailers = MailerService.readMailersFromInputStream(inputStream);

        assertNotNull(mailers);
        assertEquals(1, mailers.size());
        assertEquals("smtp.gmail.com", mailers.get(0).getUrl());
    }

    @Test
    public void testReadMailersFromInputStream_MultipleMailers() throws Exception {
        String multipleMailersJson = "[\n" +
                "  {\n" +
                "    \"url\": \"smtp.gmail.com\",\n" +
                "    \"recipients\": [\"john@example.com\"],\n" +
                "    \"cc\": [\"manager@example.com\"],\n" +
                "    \"allowedMailers\": []\n" +
                "  },\n" +
                "  {\n" +
                "    \"url\": \"smtp.outlook.com\",\n" +
                "    \"recipients\": [\"alice@company.com\"],\n" +
                "    \"cc\": [\"lead@company.com\"],\n" +
                "    \"allowedMailers\": []\n" +
                "  },\n" +
                "  {\n" +
                "    \"url\": \"smtp.sendgrid.com\",\n" +
                "    \"recipients\": [\"kate@sendgrid.com\"],\n" +
                "    \"cc\": [\"manager@sendgrid.com\"],\n" +
                "    \"allowedMailers\": []\n" +
                "  }\n" +
                "]";

        InputStream inputStream = new ByteArrayInputStream(multipleMailersJson.getBytes());
        List<Mailer> mailers = MailerService.readMailersFromInputStream(inputStream);

        assertEquals(3, mailers.size());
        assertEquals("smtp.gmail.com", mailers.get(0).getUrl());
        assertEquals("smtp.outlook.com", mailers.get(1).getUrl());
        assertEquals("smtp.sendgrid.com", mailers.get(2).getUrl());
    }

    @Test
    public void testConvertMailersToJson() throws Exception {
        List<Mailer> mailers = MailerService.readMailersFromString(validMailersJson);
        String jsonOutput = MailerService.convertMailersToJson(mailers);

        assertNotNull(jsonOutput);
        assertTrue(jsonOutput.contains("smtp.gmail.com"));
        assertTrue(jsonOutput.contains("john@example.com"));
        assertTrue(jsonOutput.contains("support@gmail.com"));
        assertTrue(jsonOutput.contains("2024") || jsonOutput.contains("validFrom"));
    }

    @Test
    public void testConvertMailerToJson() throws Exception {
        List<Mailer> mailers = MailerService.readMailersFromString(singleMailerJson);
        Mailer mailer = mailers.get(0);
        String jsonOutput = MailerService.convertMailerToJson(mailer);

        assertNotNull(jsonOutput);
        assertTrue(jsonOutput.contains("smtp.outlook.com"));
        assertTrue(jsonOutput.contains("alice@company.com"));
        assertTrue(jsonOutput.contains("support@outlook.com"));
    }

    @Test
    public void testRoundTripConversion() throws Exception {
        // Read mailers from JSON
        List<Mailer> originalMailers = MailerService.readMailersFromString(validMailersJson);

        // Convert back to JSON
        String convertedJson = MailerService.convertMailersToJson(originalMailers);

        // Read again from converted JSON
        List<Mailer> reconvertedMailers = MailerService.readMailersFromString(convertedJson);

        // Verify data integrity
        assertEquals(originalMailers.size(), reconvertedMailers.size());
        assertEquals(originalMailers.get(0).getUrl(), reconvertedMailers.get(0).getUrl());
        assertEquals(originalMailers.get(0).getRecipients().size(),
                reconvertedMailers.get(0).getRecipients().size());
        assertEquals(originalMailers.get(0).getAllowedMailers().size(),
                reconvertedMailers.get(0).getAllowedMailers().size());
    }

    @Test
    public void testMailerWithNullBcc() throws Exception {
        String jsonWithNullBcc = "[\n" +
                "  {\n" +
                "    \"url\": \"smtp.test.com\",\n" +
                "    \"recipients\": [\"test@example.com\"],\n" +
                "    \"cc\": [\"cc@example.com\"],\n" +
                "    \"bcc\": null,\n" +
                "    \"allowedMailers\": []\n" +
                "  }\n" +
                "]";

        List<Mailer> mailers = MailerService.readMailersFromString(jsonWithNullBcc);
        Mailer mailer = mailers.get(0);

        assertNotNull(mailer);
        assertNull(mailer.getBcc());
    }

    @Test
    public void testMailerWithEmptyAllowedMailers() throws Exception {
        String jsonWithEmptyAllowed = "[\n" +
                "  {\n" +
                "    \"url\": \"smtp.test.com\",\n" +
                "    \"recipients\": [\"test@example.com\"],\n" +
                "    \"cc\": [\"cc@example.com\"],\n" +
                "    \"allowedMailers\": []\n" +
                "  }\n" +
                "]";

        List<Mailer> mailers = MailerService.readMailersFromString(jsonWithEmptyAllowed);
        Mailer mailer = mailers.get(0);

        assertNotNull(mailer.getAllowedMailers());
        assertEquals(0, mailer.getAllowedMailers().size());
    }

    @Test
    public void testMailerWithMultipleRecipients() throws Exception {
        String jsonWithMultipleRecips = "[\n" +
                "  {\n" +
                "    \"url\": \"smtp.test.com\",\n" +
                "    \"recipients\": [\n" +
                "      \"user1@example.com\",\n" +
                "      \"user2@example.com\",\n" +
                "      \"user3@example.com\",\n" +
                "      \"user4@example.com\",\n" +
                "      \"user5@example.com\"\n" +
                "    ],\n" +
                "    \"cc\": [\"cc@example.com\"],\n" +
                "    \"allowedMailers\": []\n" +
                "  }\n" +
                "]";

        List<Mailer> mailers = MailerService.readMailersFromString(jsonWithMultipleRecips);
        Mailer mailer = mailers.get(0);

        assertEquals(5, mailer.getRecipients().size());
        assertEquals("user1@example.com", mailer.getRecipients().get(0));
        assertEquals("user5@example.com", mailer.getRecipients().get(4));
    }

    @Test
    public void testMailerWithMultipleCCs() throws Exception {
        String jsonWithMultipleCCs = "[\n" +
                "  {\n" +
                "    \"url\": \"smtp.test.com\",\n" +
                "    \"recipients\": [\"test@example.com\"],\n" +
                "    \"cc\": [\n" +
                "      \"cc1@example.com\",\n" +
                "      \"cc2@example.com\",\n" +
                "      \"cc3@example.com\"\n" +
                "    ],\n" +
                "    \"allowedMailers\": []\n" +
                "  }\n" +
                "]";

        List<Mailer> mailers = MailerService.readMailersFromString(jsonWithMultipleCCs);
        Mailer mailer = mailers.get(0);

        assertEquals(3, mailer.getCc().size());
        assertEquals("cc1@example.com", mailer.getCc().get(0));
        assertEquals("cc3@example.com", mailer.getCc().get(2));
    }

    @Test
    public void testAllowedMailerFields() throws Exception {
        List<Mailer> mailers = MailerService.readMailersFromString(validMailersJson);
        AllowedMailers am = mailers.get(0).getAllowedMailers().get(0);

        // Verify all fields are properly deserialized
        assertNotNull(am.getEmail());
        assertNotNull(am.getValidFrom());
        assertNotNull(am.getValidUntil());

        // Verify field values
        assertEquals("support@gmail.com", am.getEmail());
        assertTrue(am.getValidFrom().isBefore(am.getValidUntil()));
    }

    @Test(expected = Exception.class)
    public void testReadMailersFromString_InvalidJson() throws Exception {
        MailerService.readMailersFromString(invalidJson);
    }

    @Test(expected = Exception.class)
    public void testReadMailersFromString_EmptyJson() throws Exception {
        MailerService.readMailersFromString("");
    }

    @Test
    public void testConvertMailerToJson_PreservesData() throws Exception {
        List<Mailer> mailers = MailerService.readMailersFromString(validMailersJson);
        Mailer originalMailer = mailers.get(0);

        String jsonOutput = MailerService.convertMailerToJson(originalMailer);
        List<Mailer> reconvertedList = MailerService.readMailersFromString("[" + jsonOutput + "]");
        Mailer reconvertedMailer = reconvertedList.get(0);

        assertEquals(originalMailer.getUrl(), reconvertedMailer.getUrl());
        assertEquals(originalMailer.getRecipients(), reconvertedMailer.getRecipients());
        assertEquals(originalMailer.getCc(), reconvertedMailer.getCc());
    }

    @Test
    public void testReadFromInputStream_WithValidData() throws Exception {
        InputStream stream = new ByteArrayInputStream(singleMailerJson.getBytes());
        List<Mailer> mailers = MailerService.readMailersFromInputStream(stream);

        assertNotNull(mailers);
        assertEquals(1, mailers.size());
        assertEquals("smtp.outlook.com", mailers.get(0).getUrl());
        assertEquals(1, mailers.get(0).getAllowedMailers().size());
    }

    @Test
    public void testMailerToString() throws Exception {
        List<Mailer> mailers = MailerService.readMailersFromString(validMailersJson);
        Mailer mailer = mailers.get(0);

        String toString = mailer.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Mailer"));
        assertTrue(toString.contains("smtp.gmail.com"));
    }

    @Test
    public void testAllowedMailerToString() throws Exception {
        List<Mailer> mailers = MailerService.readMailersFromString(validMailersJson);
        AllowedMailers am = mailers.get(0).getAllowedMailers().get(0);

        String toString = am.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("AllowedMailers"));
        assertTrue(toString.contains("support@gmail.com"));
    }

    @Test
    public void testEmptyMailersArray() throws Exception {
        String emptyJson = "[]";
        List<Mailer> mailers = MailerService.readMailersFromString(emptyJson);

        assertNotNull(mailers);
        assertEquals(0, mailers.size());
    }

    @Test
    public void testMultipleAllowedMailersPerMailer() throws Exception {
        String multipleAllowedJson = "[\n" +
                "  {\n" +
                "    \"url\": \"smtp.test.com\",\n" +
                "    \"recipients\": [\"test@example.com\"],\n" +
                "    \"cc\": [\"cc@example.com\"],\n" +
                "    \"allowedMailers\": [\n" +
                "      {\n" +
                "        \"email\": \"am1@test.com\",\n" +
                "        \"validFrom\": \"2024-01-01T00:00:00\",\n" +
                "        \"validUntil\": \"2024-12-31T23:59:59\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"email\": \"am2@test.com\",\n" +
                "        \"validFrom\": \"2024-01-01T00:00:00\",\n" +
                "        \"validUntil\": \"2024-12-31T23:59:59\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"email\": \"am3@test.com\",\n" +
                "        \"validFrom\": \"2024-01-01T00:00:00\",\n" +
                "        \"validUntil\": \"2024-12-31T23:59:59\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"email\": \"am4@test.com\",\n" +
                "        \"validFrom\": \"2024-01-01T00:00:00\",\n" +
                "        \"validUntil\": \"2024-12-31T23:59:59\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"email\": \"am5@test.com\",\n" +
                "        \"validFrom\": \"2024-01-01T00:00:00\",\n" +
                "        \"validUntil\": \"2024-12-31T23:59:59\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        List<Mailer> mailers = MailerService.readMailersFromString(multipleAllowedJson);
        Mailer mailer = mailers.get(0);

        assertEquals(5, mailer.getAllowedMailers().size());
        assertEquals("am1@test.com", mailer.getAllowedMailers().get(0).getEmail());
        assertEquals("am5@test.com", mailer.getAllowedMailers().get(4).getEmail());
    }
}
