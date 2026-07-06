package org.gcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.InputStream;

public class UserService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads a User object from a JSON file using ObjectMapper
     *
     * @param filePath the path to the JSON file
     * @return User object populated with data from the JSON file
     * @throws Exception if file reading or JSON mapping fails
     */
    public static User readUserFromFile(String filePath) throws Exception {
        File file = new File(filePath);
        return objectMapper.readValue(file, User.class);
    }

    /**
     * Reads a User object from an InputStream (useful for resources in classpath)
     *
     * @param inputStream the InputStream pointing to the JSON data
     * @return User object populated with data from the JSON stream
     * @throws Exception if reading or JSON mapping fails
     */
    public static User readUserFromInputStream(InputStream inputStream) throws Exception {
        return objectMapper.readValue(inputStream, User.class);
    }

    /**
     * Reads a User object from a JSON string
     *
     * @param jsonString the JSON string
     * @return User object populated with data from the JSON string
     * @throws Exception if JSON mapping fails
     */
    public static User readUserFromString(String jsonString) throws Exception {
        return objectMapper.readValue(jsonString, User.class);
    }

    /**
     * Converts a User object to a JSON string
     *
     * @param user the User object to convert
     * @return JSON string representation of the User object
     * @throws Exception if JSON conversion fails
     */
    public static String convertUserToJson(User user) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
    }
}
