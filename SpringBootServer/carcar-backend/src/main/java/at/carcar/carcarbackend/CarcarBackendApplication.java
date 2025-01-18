package at.carcar.carcarbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.TimeZone;

@SpringBootApplication
public class CarcarBackendApplication {
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC+1"));
		SpringApplication.run(CarcarBackendApplication.class, args);
	}
}
