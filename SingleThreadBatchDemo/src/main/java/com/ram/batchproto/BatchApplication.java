package com.ram.batchproto;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;




/*@Configuration
@EnableBatchProcessing
@ComponentScan
//spring boot configuration
@EnableAutoConfiguration

//@SpringBootApplication
//@EnableBatchProcessing
@EnableScheduling // Enables scheduling
@PropertySource(value = {"file:/home/qison/Projects/SpringBatch/src/main/resources/application.properties"}, ignoreResourceNotFound = false)
*/

public class BatchApplication {
	

	public static void main(String[] args) {
		//System.setProperty("spring.application.admin.enabled", "true");
		SpringApplication.run(JobRunner.class, args);
	}
	
	


}
