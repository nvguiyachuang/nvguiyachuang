package world;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaApp {
    public static void main(String[] args) {
        // /opt/applog/gmall2020/app.log
        SpringApplication.run(KafkaApp.class, args);
    }
}
