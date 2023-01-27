package com.busbooking.eurekaRegistery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekanamingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekanamingServerApplication.class, args);
	}

}
