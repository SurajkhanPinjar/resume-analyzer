package com.resume_analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.resume_analyzer.entity")
@EnableJpaRepositories("com.resume_analyzer.repository")
public class ResumeAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeAnalyzerApplication.class, args);
	}

}
