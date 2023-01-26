package com.busbooking.data.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.busbooking.data.enums.TicketStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Book_Tickets")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookTickets {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String ticketId;

	@Column(name = "Passenger_Name")
	private String PassengerName;

	private int age;

	private String gender;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	private int seatNo;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", nullable = false)
	private User userId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "bus_id", nullable = false)
	private BusDetails busId;

	private TicketStatus status;
}
