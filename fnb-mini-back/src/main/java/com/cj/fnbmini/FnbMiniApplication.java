package com.cj.fnbmini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FnbMiniApplication {

	public static void main(String[] args) {
		SpringApplication.run(FnbMiniApplication.class, args);
	}

}
