package org.gcs;

import java.io.InputStream;

public class MailerDemo {
    public static void main(String[] args) {
        try {
            // Example 1: Read Single Mailer from classpath resource file
            System.out.println("=== Reading Single Mailer from classpath resource ===");
            InputStream inputStream = MailerDemo.class.getClassLoader().getResourceAsStream("mailers.json");
            Mailer mailer = MailerService.readMailerFromInputStream(inputStream);
            System.out.println("Mailer loaded successfully!");
            System.out.println();

            // Example 2: Display mailer with details
            System.out.println("=== Mailer Details ===");
            System.out.println("URL: " + mailer.getUrl());
            System.out.println("Recipients (" + mailer.getRecipients().size() + "): " + mailer.getRecipients());
            System.out.println("CC (" + mailer.getCc().size() + "): " + mailer.getCc());
            System.out.println("BCC: " + (mailer.getBcc() == null || mailer.getBcc().isEmpty() ? "None" : mailer.getBcc()));
            
            // Display AllowedMailers
            if (mailer.getAllowedMailers() != null) {
                System.out.println("AllowedMailers (" + mailer.getAllowedMailers().size() + "):");
                for (AllowedMailers am : mailer.getAllowedMailers()) {
                    System.out.println("  - Email: " + am.getEmail() + " | Valid From: " + am.getValidFrom() + " | Valid Until: " + am.getValidUntil());
                }
            }
            System.out.println();

            // Example 3: Convert back to JSON
            System.out.println("=== Converting Mailer back to JSON ===");
            String jsonOutput = MailerService.convertMailerToJson(mailer);
            System.out.println(jsonOutput);

            // Example 4: Modify mailer and show the change
            System.out.println("\n=== Modifying Mailer ===");
            System.out.println("Original URL: " + mailer.getUrl());
            mailer.setUrl("smtp.gmail-updated.com");
            System.out.println("Updated URL: " + mailer.getUrl());
            System.out.println("Updated Mailer as JSON:\n" + MailerService.convertMailerToJson(mailer));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
