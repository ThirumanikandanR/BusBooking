//package com.busbooking.data.model;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//
//import org.hibernate.annotations.GenericGenerator;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "Ticket_Booking")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class TicketBooking {
//
//	@Id
//	@GeneratedValue(generator = "uuid2")
//	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
//	private String id;
//
//	@ManyToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "user_id", nullable = false)
//	private User userId;
//
//	@ManyToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "bus_id", nullable = false)
//	private BusDetails busId;
//
//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "psgn_id", nullable = false)
//	private PassengerDeatils pasngDetails;
//	
//	
//	
//	
//
//}
