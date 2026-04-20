package ma.chaghaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChaghafApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChaghafApplication.class, args);
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   🚀 Chaghaf Monolith is running!        ║");
        System.out.println("║   API: http://localhost:8080              ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }
}
