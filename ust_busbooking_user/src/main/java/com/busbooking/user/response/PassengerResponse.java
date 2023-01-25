package com.busbooking.user.response;

import java.time.LocalDate;

import com.busbooking.data.enums.TicketStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PassengerResponse {

	private String ticketId;

	private String PassengerName;

	private int age;

	private String gender;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	private int seatNo;

	private TicketStatus status;
}
