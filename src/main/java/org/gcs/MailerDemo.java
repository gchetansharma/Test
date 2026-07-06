package org.gcs;

import java.io.InputStream;
import java.util.List;

public class MailerDemo {
    public static void main(String[] args) {
        try {
            // Example 1: Read Mailers from classpath resource file
            System.out.println("=== Reading Mailers from classpath resource ===");
            InputStream inputStream = MailerDemo.class.getClassLoader().getResourceAsStream("mailers.json");
            List<Mailer> mailers = MailerService.readMailersFromInputStream(inputStream);
            System.out.println("Total Mailers loaded: " + mailers.size());
            System.out.println();

            // Example 2: Display each mailer with details
            System.out.println("=== Mailer Details ===");
            for (int i = 0; i < mailers.size(); i++) {
                Mailer mailer = mailers.get(i);
                System.out.println("Mailer " + (i + 1) + ":");
                System.out.println("  URL: " + mailer.getUrl());
                System.out.println("  Recipients (" + mailer.getRecipients().size() + "): " + mailer.getRecipients());
                System.out.println("  CC (" + mailer.getCc().size() + "): " + mailer.getCc());
                System.out.println("  BCC: " + (mailer.getBcc() == null || mailer.getBcc().isEmpty() ? "None" : mailer.getBcc()));
                
                // Display AllowedMailers
                if (mailer.getAllowedMailers() != null) {
                    System.out.println("  AllowedMailers (" + mailer.getAllowedMailers().size() + "):");
                    for (AllowedMailers am : mailer.getAllowedMailers()) {
                        System.out.println("    - Email: " + am.getEmail() + " | Valid From: " + am.getValidFrom() + " | Valid Until: " + am.getValidUntil());
                    }
                }
                System.out.println();
            }

            // Example 3: Convert back to JSON
            System.out.println("=== Converting Mailers back to JSON ===");
            String jsonOutput = MailerService.convertMailersToJson(mailers);
            System.out.println(jsonOutput);

            // Example 4: Modify a mailer and show the change
            System.out.println("\n=== Modifying first mailer ===");
            Mailer firstMailer = mailers.get(0);
            System.out.println("Original URL: " + firstMailer.getUrl());
            firstMailer.setUrl("smtp.gmail-updated.com");
            System.out.println("Updated URL: " + firstMailer.getUrl());
            System.out.println("Updated Mailer as JSON:\n" + MailerService.convertMailerToJson(firstMailer));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
