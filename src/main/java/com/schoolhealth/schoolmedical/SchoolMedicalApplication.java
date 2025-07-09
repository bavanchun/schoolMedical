package com.schoolhealth.schoolmedical;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SchoolMedicalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolMedicalApplication.class, args);
	}

}
