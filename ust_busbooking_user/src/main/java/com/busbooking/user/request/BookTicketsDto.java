package com.busbooking.user.request;

import java.util.List;

import lombok.Data;

@Data
public class BookTicketsDto {

	private String userId;

	private String busId;

	private List<PassengerDetailsDto> pasanger;

}
