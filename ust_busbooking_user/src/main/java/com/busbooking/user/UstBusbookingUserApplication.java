package com.busbooking.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.busbooking.admin","com.busbooking.data","com.busbooking.*"} )
public class UstBusbookingUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UstBusbookingUserApplication.class, args);
	}

}
