package tech.developerdhairya.DigitBatua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DigitBatuaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitBatuaApplication.class, args);
	}
//add redis
}
