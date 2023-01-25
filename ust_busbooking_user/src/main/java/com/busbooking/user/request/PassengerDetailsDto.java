package com.busbooking.user.request;

import lombok.Data;

@Data
public class PassengerDetailsDto {

	private String pname;

	private int age;

	private String gender;
	
	private int seatNo;
}
