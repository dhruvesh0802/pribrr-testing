package com.pb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class PBBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PBBackendApplication.class, args);
		System.out.println("Server is Up Now...................");
	}

}
