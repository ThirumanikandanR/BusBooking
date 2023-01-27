package com.busbooking.admin.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BusDetailsDto {

	private String busNo;
	
	private String busName;
	
	private String driverName;
	
	private String contNum;
	
	private int noOfSeats;
	
	private String tkkPrice;
	
	private LocalDate date;
	
	private String depTime;
	
	private String arvTime;
	
	private String fromPlace;
	
	private String toPlace;
}
