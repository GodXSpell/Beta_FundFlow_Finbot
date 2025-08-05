package com.finbot.Beta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.finbot.Beta", "com.finbot.Beta.config"})
public class BetaApplication {
	public static void main(String[] args) {
		System.out.println(">>> Starting Beta application with explicit config scanning");
		SpringApplication.run(BetaApplication.class, args);
	}
}