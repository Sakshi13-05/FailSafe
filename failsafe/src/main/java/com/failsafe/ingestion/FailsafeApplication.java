package com.failsafe.ingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@SpringBootApplication
public class FailsafeApplication {

    public static void main(String[] args) {
        try {
            // 1. Extract ca.pem from the JAR and write it to a temporary OS file
            InputStream is = FailsafeApplication.class.getResourceAsStream("/ca.pem");
            if (is != null) {
                File tempFile = File.createTempFile("aiven-ca", ".pem");
                tempFile.deleteOnExit(); // Automatically cleans up when the app stops
                Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // 2. Pass this temporary physical file path into an environment variable
                System.setProperty("KAFKA_TRUSTSTORE_PATH", tempFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpringApplication.run(FailsafeApplication.class, args);
    }
}