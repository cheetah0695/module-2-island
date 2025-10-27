package org.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Properties properties = new Properties();
    private static final Config INSTANCE = new Config();

    private Config() {
        try (FileInputStream input = new FileInputStream("src/main/resources/config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in resources");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public static float getFloat(String key) {
        return Float.parseFloat(properties.getProperty(key));
    }

    public static Config getInstance() {
        return INSTANCE;
    }
}
