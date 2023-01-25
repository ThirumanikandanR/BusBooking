package com.busbooking.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(scanBasePackages = {"com.busbooking.admin","com.busbooking.data","com.busbooking.*"} )
@PropertySources(@PropertySource("classpath:message.properties"))
public class UstBusbookingUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UstBusbookingUserApplication.class, args);
	}

}
