package com.busbooking.user.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.busbooking.data.enums.TicketStatus;
import com.busbooking.data.model.BookTickets;
import com.busbooking.data.model.BusDetails;
import com.busbooking.data.model.User;
import com.busbooking.data.payload.response.MessageResponse;
import com.busbooking.data.repository.BusDetailsRepository;
import com.busbooking.data.repository.PassengerDetailsRepository;
import com.busbooking.data.repository.UserRepository;
import com.busbooking.user.request.BookTicketsDto;
import com.busbooking.user.request.PassengerDetailsDto;
import com.busbooking.user.response.PassengerResponse;
import com.busbooking.user.response.TicketResponse;
import com.busbooking.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	BusDetailsRepository busDetailsRepository;

	@Autowired
	UserRepository userRepository;

//	@Autowired
//	TicketBookingRepository ticBookingRepository;

	@Autowired
	PassengerDetailsRepository passengerDetailsRepository;

	@Autowired
	Environment env;

	@Override
	public ResponseEntity<?> viewAllBus() {

		List<BusDetails> allBus = busDetailsRepository.findAll();
		if (Objects.isNull(allBus)) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("datd.not.found"), HttpStatus.NOT_FOUND.value()));
		}

		return ResponseEntity
				.ok(new MessageResponse(HttpStatus.OK.value(), env.getProperty("fetched.busDetails"), allBus));
	}

	@Override
	public ResponseEntity<?> sortBusByDateAndPlaces(LocalDate date, String fromPlace, String toPlace) {
		if (Objects.isNull(date) && Objects.isNull(fromPlace) && Objects.isNull(toPlace)) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("invalid.input"), HttpStatus.BAD_REQUEST.value()));
		}
		List<BusDetails> sortDate = null;
		List<BusDetails> sortPlace = null;
		List<BusDetails> sortByDateAndPlace = null;
		if (Objects.nonNull(date) && StringUtils.isEmpty(fromPlace) && StringUtils.isEmpty(fromPlace)) {
			sortDate = busDetailsRepository.findByDate(date);
			if (Objects.isNull(sortDate)) {
				return ResponseEntity
						.ok(new MessageResponse(env.getProperty("date.not.found"), HttpStatus.NOT_FOUND.value()));
			}
			return ResponseEntity.ok(
					new MessageResponse(HttpStatus.OK.value(), env.getProperty("fetched.busDetails.byDate"), sortDate));
		} else if (Objects.nonNull(date) && !StringUtils.isEmpty(fromPlace) && !StringUtils.isEmpty(fromPlace)) {
			sortPlace = busDetailsRepository.findByDate(date);
			sortByDateAndPlace = sortPlace.stream()
					.filter(f -> f.getFromPlace().equals(fromPlace) && f.getToPlace().equals(toPlace))
					.collect(Collectors.toList());
			if (sortByDateAndPlace.isEmpty()) {
				return ResponseEntity
						.ok(new MessageResponse(env.getProperty("place.not.found"), HttpStatus.NOT_FOUND.value()));
			}

		}
		return ResponseEntity.ok(new MessageResponse(HttpStatus.OK.value(),
				env.getProperty("fetched.busDetails.byDateAndPlaces"), sortByDateAndPlace));

	}

	@Override
	public ResponseEntity<?> bookTickets(BookTicketsDto bookTicketsDto) {
		if (Objects.isNull(bookTicketsDto)) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("invalid.input"), HttpStatus.BAD_REQUEST.value()));
		}

		User user = userRepository.findById(bookTicketsDto.getUserId()).get();
		if (Objects.isNull(user)) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("user.not.found"), HttpStatus.BAD_REQUEST.value()));
		}

		BusDetails bus = busDetailsRepository.findById(bookTicketsDto.getBusId()).get();
		if (Objects.isNull(user)) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("bus.not.found"), HttpStatus.BAD_REQUEST.value()));
		}

		BookTickets passengerDeatils = null;
		List<PassengerResponse> allPassenger=new ArrayList<>();
		PassengerResponse psngResponse=null;
		TicketResponse ticketResponse=null;
		for (PassengerDetailsDto PassengerInfo : bookTicketsDto.getPasanger()) {
			
			passengerDeatils = BookTickets.builder().PassengerName(PassengerInfo.getPname()).age(PassengerInfo.getAge())
					.gender(PassengerInfo.getGender()).date(bus.getDate()).seatNo(PassengerInfo.getSeatNo())
					.busDetails(bus)
					.status(TicketStatus.CONFIRMED)
					.userId(user).build();
			
			passengerDetailsRepository.save(passengerDeatils);
			
			psngResponse=psngResponse.builder()
			.ticketId(passengerDeatils.getTicketId())
			.PassengerName(passengerDeatils.getPassengerName())
			.age(passengerDeatils.getAge())
			.gender(passengerDeatils.getGender())
			.date(bus.getDate())
			.seatNo(passengerDeatils.getSeatNo())
			.status(TicketStatus.CONFIRMED)
			.build();
			
			allPassenger.add(psngResponse);
		}
		System.out.println("Allpassanger="+allPassenger.size());
		ticketResponse = ticketResponse.builder()
				.userDetails(user)
				.busDetails(bus)
				.passengers(allPassenger)
				.build();	

		return ResponseEntity
				.ok(new MessageResponse(HttpStatus.OK.value(), env.getProperty("busticket.booked.success"), ticketResponse));
	}

}
