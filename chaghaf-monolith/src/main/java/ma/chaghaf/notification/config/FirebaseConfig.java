package ma.chaghaf.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials-path:/app/config/firebase-service-account.json}")
    private String credentialsPath;

    @PostConstruct
    public void initFirebase() {
        if (!FirebaseApp.getApps().isEmpty()) return;

        try {
            InputStream serviceAccount;

            // Essayer le chemin absolu d'abord (production)
            if (Files.exists(Paths.get(credentialsPath))) {
                serviceAccount = new FileInputStream(credentialsPath);
                log.info("Firebase: chargement depuis {}", credentialsPath);
            } else {
                // Fallback: classpath (dev)
                serviceAccount = new ClassPathResource("firebase-service-account.json").getInputStream();
                log.info("Firebase: chargement depuis classpath");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            FirebaseApp.initializeApp(options);
            log.info("Firebase initialisé avec succès");

        } catch (IOException e) {
            log.warn("Firebase non initialisé (fichier credentials manquant): {}", e.getMessage());
        }
    }
}
