package atm.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Creates database connections using values stored in config.properties.
 */
public final class DBConnection {
    private static final String CONFIG_FILE = "/config.properties";
    private static final Properties PROPERTIES = loadProperties();

    private DBConnection() {
    }

    /**
     * Opens a new JDBC connection to the configured MySQL database.
     *
     * @return an open database connection
     * @throws SQLException if the JDBC connection cannot be opened
     */
    public static Connection getConnection() throws SQLException {
        String url = readProperty("db.url");
        String username = readProperty("db.username");
        String password = readProperty("db.password");
        return DriverManager.getConnection(url, username, password);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = DBConnection.class.getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IllegalStateException("Missing config.properties in src/main/resources.");
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load database configuration.", exception);
        }
    }

    private static String readProperty(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("Missing required database property: " + key);
        }
        return value;
    }
}
