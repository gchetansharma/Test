package org.gcs;

import java.io.InputStream;

public class UserDemo {
    public static void main(String[] args) {
        try {
            // Example 1: Read User from classpath resource file
            System.out.println("=== Reading User from classpath resource ===");
            InputStream inputStream = UserDemo.class.getClassLoader().getResourceAsStream("user.json");
            User user1 = UserService.readUserFromInputStream(inputStream);
            System.out.println("User loaded from file: " + user1);
            System.out.println();

            // Example 2: Convert User object to JSON string
            System.out.println("=== Converting User object to JSON ===");
            String jsonString = UserService.convertUserToJson(user1);
            System.out.println("User as JSON:\n" + jsonString);
            System.out.println();

            // Example 3: Read User from a JSON string
            System.out.println("=== Reading User from JSON string ===");
            String json = "{\"id\":2,\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"email\":\"jane.smith@example.com\",\"phone\":\"+1-555-0456\",\"age\":28,\"active\":true}";
            User user2 = UserService.readUserFromString(json);
            System.out.println("User loaded from string: " + user2);
            System.out.println();

            // Example 4: Modify and convert back to JSON
            System.out.println("=== Modifying and converting User to JSON ===");
            user2.setEmail("jane.updated@example.com");
            user2.setAge(29);
            String updatedJson = UserService.convertUserToJson(user2);
            System.out.println("Updated User as JSON:\n" + updatedJson);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
