package com.busbooking.admin.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdateBusDetailsDto {
	
	private String id;

	private String busNo;

	private String busName;

	private String driverName;

	private String contNum;

	private String noOfSeats;

	private String tkkPrice;

	private LocalDate date;

	private String depTime;

	private String arvTime;
}
