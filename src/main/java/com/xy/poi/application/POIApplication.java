package com.xy.poi.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.xy.poi")
public class POIApplication {

	public static void main(String[] args) {
		SpringApplication.run(POIApplication.class, args);
	}

}
