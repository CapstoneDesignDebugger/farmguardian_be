package com.farmguardian.farmguardian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FarmguardianApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmguardianApplication.class, args);
	}

}

