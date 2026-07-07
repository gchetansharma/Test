package org.gcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public class MailerService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Reads a list of Mailer objects from a JSON file using ObjectMapper
     *
     * @param filePath the path to the JSON file
     * @return List of Mailer objects populated with data from the JSON file
     * @throws Exception if file reading or JSON mapping fails
     */
    public static List<Mailer> readMailersFromFile(String filePath) throws Exception {
        File file = new File(filePath);
        return objectMapper.readValue(file, new TypeReference<List<Mailer>>() {});
    }

    /**
     * Reads a single Mailer object from a JSON file using ObjectMapper
     *
     * @param filePath the path to the JSON file
     * @return Mailer object populated with data from the JSON file
     * @throws Exception if file reading or JSON mapping fails
     */
    public static Mailer readMailerFromFile(String filePath) throws Exception {
        File file = new File(filePath);
        return objectMapper.readValue(file, Mailer.class);
    }

    /**
     * Reads a list of Mailer objects from an InputStream (useful for resources in classpath)
     *
     * @param inputStream the InputStream pointing to the JSON data
     * @return List of Mailer objects populated with data from the JSON stream
     * @throws Exception if reading or JSON mapping fails
     */
    public static List<Mailer> readMailersFromInputStream(InputStream inputStream) throws Exception {
        return objectMapper.readValue(inputStream, new TypeReference<List<Mailer>>() {});
    }

    /**
     * Reads a single Mailer object from an InputStream (useful for resources in classpath)
     *
     * @param inputStream the InputStream pointing to the JSON data
     * @return Mailer object populated with data from the JSON stream
     * @throws Exception if reading or JSON mapping fails
     */
    public static Mailer readMailerFromInputStream(InputStream inputStream) throws Exception {
        return objectMapper.readValue(inputStream, Mailer.class);
    }

    /**
     * Reads a list of Mailer objects from a JSON string
     *
     * @param jsonString the JSON string
     * @return List of Mailer objects populated with data from the JSON string
     * @throws Exception if JSON mapping fails
     */
    public static List<Mailer> readMailersFromString(String jsonString) throws Exception {
        return objectMapper.readValue(jsonString, new TypeReference<List<Mailer>>() {});
    }

    /**
     * Reads a single Mailer object from a JSON string
     *
     * @param jsonString the JSON string
     * @return Mailer object populated with data from the JSON string
     * @throws Exception if JSON mapping fails
     */
    public static Mailer readMailerFromString(String jsonString) throws Exception {
        return objectMapper.readValue(jsonString, Mailer.class);
    }

    /**
     * Converts a list of Mailer objects to a JSON string
     *
     * @param mailers the list of Mailer objects to convert
     * @return JSON string representation of the Mailer list
     * @throws Exception if JSON conversion fails
     */
    public static String convertMailersToJson(List<Mailer> mailers) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mailers);
    }

    /**
     * Converts a single Mailer object to a JSON string
     *
     * @param mailer the Mailer object to convert
     * @return JSON string representation of the Mailer object
     * @throws Exception if JSON conversion fails
     */
    public static String convertMailerToJson(Mailer mailer) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mailer);
    }
}

