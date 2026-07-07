package org.gcs;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class MailerServiceTest {

    private String validMailersJson;
    private String singleMailerJson;
    private String invalidJson;
    private String resourcesPath;

    @Before
    public void setUp() {
        // Valid JSON with one mailer containing AllowedMailers (as list)
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

        // Single mailer JSON (not a list)
        singleMailerJson = "{\n" +
                "  \"url\": \"smtp.outlook.com\",\n" +
                "  \"recipients\": [\"alice@company.com\"],\n" +
                "  \"cc\": [\"lead@company.com\"],\n" +
                "  \"allowedMailers\": [\n" +
                "    {\n" +
                "      \"email\": \"support@outlook.com\",\n" +
                "      \"validFrom\": \"2024-01-01T00:00:00\",\n" +
                "      \"validUntil\": \"2024-12-31T23:59:59\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        invalidJson = "{ invalid json }";

        resourcesPath = "src/main/resources/mailers.json";
    }

    // ===== Tests for Reading from File =====

    @Test
    public void testReadMailerFromFile_SingleMailerObject() throws Exception {
        Mailer mailer = MailerService.readMailerFromFile(resourcesPath);

        assertNotNull(mailer);
        assertEquals("smtp.gmail.com", mailer.getUrl());
        assertEquals(5, mailer.getRecipients().size());
        assertEquals(5, mailer.getCc().size());
    }

    @Test
    public void testReadMailerFromFile_VerifyUrl() throws Exception {
        Mailer mailer = MailerService.readMailerFromFile(resourcesPath);

        assertEquals("smtp.gmail.com", mailer.getUrl());
    }

    @Test
    public void testReadMailerFromFile_VerifyRecipients() throws Exception {
        Mailer mailer = MailerService.readMailerFromFile(resourcesPath);

        assertEquals(5, mailer.getRecipients().size());
        assertEquals("john.doe@example.com", mailer.getRecipients().get(0));
        assertEquals("jane.smith@example.com", mailer.getRecipients().get(1));
        assertEquals("mike.johnson@example.com", mailer.getRecipients().get(2));
        assertEquals("sarah.williams@example.com", mailer.getRecipients().get(3));
        assertEquals("david.brown@example.com", mailer.getRecipients().get(4));
    }

    @Test
    public void testReadMailerFromFile_VerifyCCs() throws Exception {
        Mailer mailer = MailerService.readMailerFromFile(resourcesPath);

        assertEquals(5, mailer.getCc().size());
        assertEquals("manager1@company.com", mailer.getCc().get(0));
        assertEquals("lead1@company.com", mailer.getCc().get(1));
        assertEquals("admin1@company.com", mailer.getCc().get(2));
        assertEquals("supervisor1@company.com", mailer.getCc().get(3));
        assertEquals("coordinator1@company.com", mailer.getCc().get(4));
    }

    @Test
    public void testReadMailerFromFile_VerifyAllowedMailers() throws Exception {
        Mailer mailer = MailerService.readMailerFromFile(resourcesPath);

        assertNotNull(mailer.getAllowedMailers());
        assertEquals(5, mailer.getAllowedMailers().size());
    }

    @Test
    public void testReadMailerFromFile_VerifyFirstAllowedMailer() throws Exception {
        Mailer mailer = MailerService.readMailerFromFile(resourcesPath);
        AllowedMailers am = mailer.getAllowedMailers().get(0);

        assertEquals("support@gmail.com", am.getEmail());
        assertEquals(LocalDateTime.parse("2024-01-01T08:00:00"), am.getValidFrom());
        assertEquals(LocalDateTime.parse("2025-01-01T08:00:00"), am.getValidUntil());
    }

    @Test
    public void testReadMailerFromFile_VerifySecondAllowedMailer() throws Exception {
        Mailer mailer = MailerService.readMailerFromFile(resourcesPath);
        AllowedMailers am = mailer.getAllowedMailers().get(1);

        assertEquals("admin@gmail.com", am.getEmail());
        assertEquals(LocalDateTime.parse("2024-01-01T08:00:00"), am.getValidFrom());
        assertEquals(LocalDateTime.parse("2025-06-30T23:59:59"), am.getValidUntil());
    }

    @Test
    public void testReadMailerFromFile_VerifyThirdAllowedMailer() throws Exception {
        Mailer mailer = MailerService.readMailerFromFile(resourcesPath);
        AllowedMailers am = mailer.getAllowedMailers().get(2);

        assertEquals("noreply@gmail.com", am.getEmail());
        assertEquals(LocalDateTime.parse("2024-02-15T00:00:00"), am.getValidFrom());
        assertEquals(LocalDateTime.parse("2024-12-31T23:59:59"), am.getValidUntil());
    }

    @Test
    public void testReadMailerFromFile_VerifyFourthAllowedMailer() throws Exception {
        Mailer mailer = MailerService.readMailerFromFile(resourcesPath);
        AllowedMailers am = mailer.getAllowedMailers().get(3);

        assertEquals("security@gmail.com", am.getEmail());
        assertEquals(LocalDateTime.parse("2024-01-01T08:00:00"), am.getValidFrom());
        assertEquals(LocalDateTime.parse("2026-01-01T08:00:00"), am.getValidUntil());
    }

    @Test
    public void testReadMailerFromFile_VerifyFifthAllowedMailer() throws Exception {
        Mailer mailer = MailerService.readMailerFromFile(resourcesPath);
        AllowedMailers am = mailer.getAllowedMailers().get(4);

        assertEquals("billing@gmail.com", am.getEmail());
        assertEquals(LocalDateTime.parse("2024-03-01T08:00:00"), am.getValidFrom());
        assertEquals(LocalDateTime.parse("2025-12-31T23:59:59"), am.getValidUntil());
    }

    @Test
    public void testReadMailerFromFile_FileExists() {
        File file = new File(resourcesPath);
        assertTrue("File should exist: " + resourcesPath, file.exists());
    }

    @Test
    public void testReadMailerFromFile_DataIntegrity() throws Exception {
        Mailer mailer = MailerService.readMailerFromFile(resourcesPath);

        assertNotNull("URL should not be null", mailer.getUrl());
        assertNotNull("Recipients should not be null", mailer.getRecipients());
        assertNotNull("CC should not be null", mailer.getCc());
        assertNotNull("AllowedMailers should not be null", mailer.getAllowedMailers());

        assertTrue("Recipients should not be empty", !mailer.getRecipients().isEmpty());
        assertTrue("CC should not be empty", !mailer.getCc().isEmpty());
        assertTrue("AllowedMailers should not be empty", !mailer.getAllowedMailers().isEmpty());
    }

    // ===== Tests for Reading Single Mailer from String =====

    @Test
    public void testReadMailerFromString_SingleObject() throws Exception {
        Mailer mailer = MailerService.readMailerFromString(singleMailerJson);

        assertNotNull(mailer);
        assertEquals("smtp.outlook.com", mailer.getUrl());
        assertEquals(1, mailer.getRecipients().size());
        assertEquals("alice@company.com", mailer.getRecipients().get(0));
    }

    @Test
    public void testReadMailerFromString_SingleAllowedMailer() throws Exception {
        Mailer mailer = MailerService.readMailerFromString(singleMailerJson);

        assertEquals(1, mailer.getAllowedMailers().size());
        AllowedMailers am = mailer.getAllowedMailers().get(0);
        assertEquals("support@outlook.com", am.getEmail());
    }

    // ===== Tests for Reading Single Mailer from InputStream =====

    @Test
    public void testReadMailerFromInputStream_SingleObject() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(singleMailerJson.getBytes());
        Mailer mailer = MailerService.readMailerFromInputStream(inputStream);

        assertNotNull(mailer);
        assertEquals("smtp.outlook.com", mailer.getUrl());
    }

    @Test
    public void testReadMailerFromInputStream_WithAllowedMailers() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(singleMailerJson.getBytes());
        Mailer mailer = MailerService.readMailerFromInputStream(inputStream);

        assertNotNull(mailer.getAllowedMailers());
        assertEquals(1, mailer.getAllowedMailers().size());
    }

    // ===== Tests for Converting Single Mailer =====

    @Test
    public void testConvertMailerToJson_FromFile() throws Exception {
        Mailer mailer = MailerService.readMailerFromFile(resourcesPath);
        String jsonOutput = MailerService.convertMailerToJson(mailer);

        assertNotNull(jsonOutput);
        assertTrue(jsonOutput.contains("smtp.gmail.com"));
        assertTrue(jsonOutput.contains("john.doe@example.com"));
    }

    @Test
    public void testConvertMailerToJson_RoundTrip() throws Exception {
        // Read from file
        Mailer originalMailer = MailerService.readMailerFromFile(resourcesPath);

        // Convert to JSON
        String jsonOutput = MailerService.convertMailerToJson(originalMailer);

        // Read back from JSON
        Mailer reconvertedMailer = MailerService.readMailerFromString(jsonOutput);

        // Verify
        assertEquals(originalMailer.getUrl(), reconvertedMailer.getUrl());
        assertEquals(originalMailer.getRecipients().size(), reconvertedMailer.getRecipients().size());
        assertEquals(originalMailer.getCc().size(), reconvertedMailer.getCc().size());
        assertEquals(originalMailer.getAllowedMailers().size(), reconvertedMailer.getAllowedMailers().size());
    }

    @Test
    public void testMailerToString_FromFile() throws Exception {
        Mailer mailer = MailerService.readMailerFromFile(resourcesPath);
        String toString = mailer.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Mailer"));
        assertTrue(toString.contains("smtp.gmail.com"));
    }

    // ===== Tests for List Reading (existing functionality) =====

    @Test
    public void testReadMailersFromString_ValidJson() throws Exception {
        List<Mailer> mailers = MailerService.readMailersFromString(validMailersJson);

        assertNotNull(mailers);
        assertEquals(1, mailers.size());

        Mailer mailer = mailers.get(0);
        assertEquals("smtp.gmail.com", mailer.getUrl());
    }

    @Test
    public void testReadMailersFromString_AllowedMailers() throws Exception {
        List<Mailer> mailers = MailerService.readMailersFromString(validMailersJson);

        assertNotNull(mailers);
        Mailer mailer = mailers.get(0);
        List<AllowedMailers> allowedMailers = mailer.getAllowedMailers();

        assertNotNull(allowedMailers);
        assertEquals(2, allowedMailers.size());
    }

    @Test
    public void testReadMailersFromInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(validMailersJson.getBytes());
        List<Mailer> mailers = MailerService.readMailersFromInputStream(inputStream);

        assertNotNull(mailers);
        assertEquals(1, mailers.size());
    }

    @Test
    public void testConvertMailersToJson() throws Exception {
        List<Mailer> mailers = MailerService.readMailersFromString(validMailersJson);
        String jsonOutput = MailerService.convertMailersToJson(mailers);

        assertNotNull(jsonOutput);
        assertTrue(jsonOutput.contains("smtp.gmail.com"));
        assertTrue(jsonOutput.contains("john@example.com"));
    }

    @Test
    public void testRoundTripConversion() throws Exception {
        List<Mailer> originalMailers = MailerService.readMailersFromString(validMailersJson);
        String convertedJson = MailerService.convertMailersToJson(originalMailers);
        List<Mailer> reconvertedMailers = MailerService.readMailersFromString(convertedJson);

        assertEquals(originalMailers.size(), reconvertedMailers.size());
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
    public void testEmptyMailersArray() throws Exception {
        String emptyJson = "[]";
        List<Mailer> mailers = MailerService.readMailersFromString(emptyJson);

        assertNotNull(mailers);
        assertEquals(0, mailers.size());
    }
}
