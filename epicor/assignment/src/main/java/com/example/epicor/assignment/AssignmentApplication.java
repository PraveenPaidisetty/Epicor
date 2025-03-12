package com.example.epicor.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


/**
 * @author - Praveen Paidisetty
 *
 * This is the Main Class.
 */
@SpringBootApplication
public class AssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentApplication.class, args);
    }


	/**
	 *
	 * @return this method provides us the bean of the rest template, which we will use in the service layer.
	 */
    @Bean
    public RestTemplate getTemplate() {
        return new RestTemplate();
    }

}
