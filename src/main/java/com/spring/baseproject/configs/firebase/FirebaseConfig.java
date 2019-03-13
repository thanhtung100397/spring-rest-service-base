package com.spring.baseproject.configs.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    @Value("${application.firebase.google-services.path}")
    private String googleServicePath;
    @Value("${application.firebase.database.url}")
    private String databaseUrl;
    @Value("${application.firebase.storage.bucket}")
    private String storageBucket;

    @PostConstruct
    public void init() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(googleServicePath);
            FirebaseOptions.Builder optionsBuilder = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .setDatabaseUrl(databaseUrl)
                    .setStorageBucket(storageBucket);
            FirebaseOptions options = optionsBuilder.build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
