package com.jodev.messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class MmEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MmEurekaApplication.class, args);
	}

}
