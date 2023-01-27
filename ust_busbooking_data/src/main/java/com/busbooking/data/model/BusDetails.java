package com.busbooking.data.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Bus_Details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusDetails {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	private String busNo;

	private String busName;

	private String driverName;

	private String contNum;

	private int noOfSeats;

	private String tkkPrice;
	
	private String fromPlace;
	
	private String toPlace;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	private String depTime;

	private String arvTime;

}
