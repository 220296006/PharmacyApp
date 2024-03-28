package za.ac.cput.utils;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JdbcSchemaUtils {
    private JdbcSchemaUtils() {
    }
    /**
     * Loads the content of the provided schema resource and replaces the
     * {@link JdbcIndexedSessionRepository#DEFAULT_TABLE_NAME} by the provided table name.
     * @param schemaResource the schema resource
     * @param tableName the table name to replace
     * @return the schema resource with the table name replaced
     */
    public static Resource replaceDefaultTableName(Resource schemaResource, String tableName) throws IOException {
        try (InputStream inputStream = schemaResource.getInputStream()) {
            String schemaScript = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String newSchema = schemaScript.replace(JdbcIndexedSessionRepository.DEFAULT_TABLE_NAME, tableName);
            return new ByteArrayResource(newSchema.getBytes(StandardCharsets.UTF_8));
        }
    }

}
