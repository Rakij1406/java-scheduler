package com.ram.batchproto;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.boot.SpringApplication;

@EnableScheduling
public class JobRunner {

  @Scheduled(fixedRate = 10000)
  public void findAndRunJob() {
	 System.out.println("********findAndRunJob**********");
     SpringApplication.run(BatchConfig.class);
  }
}
