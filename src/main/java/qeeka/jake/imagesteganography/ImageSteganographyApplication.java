package qeeka.jake.imagesteganography;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ImageSteganographyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImageSteganographyApplication.class, args);
    }
}
