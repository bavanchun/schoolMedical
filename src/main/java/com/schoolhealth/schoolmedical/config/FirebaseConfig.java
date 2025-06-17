package com.schoolhealth.schoolmedical.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        try {
            // Remove the classpath: prefix
            String path = firebaseConfigPath.replace("classpath:", "");
            ClassPathResource resource = new ClassPathResource(path);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                }
        } catch (IOException e) {
            throw new RuntimeException("Firebase initialization failed", e);
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        return FirebaseMessaging.getInstance();
    }
}
