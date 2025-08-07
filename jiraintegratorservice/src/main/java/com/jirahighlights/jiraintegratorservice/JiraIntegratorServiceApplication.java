package com.jirahighlights.jiraintegratorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JiraApiProperties.class)
public class JiraIntegratorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JiraIntegratorServiceApplication.class, args);
	}

}
