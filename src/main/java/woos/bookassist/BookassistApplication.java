package woos.bookassist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BookassistApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookassistApplication.class, args);
    }

}
