package com.theironyard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TextalarmApplication {

	public static void main(String[] args) {
		SpringApplication.run(TextalarmApplication.class, args);
	}
}
