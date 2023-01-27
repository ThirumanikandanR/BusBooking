package com.busbooking.user.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.busbooking.data.enums.TicketStatus;
import com.busbooking.data.model.BookTickets;
import com.busbooking.data.model.BusDetails;
import com.busbooking.data.model.User;
import com.busbooking.data.payload.response.MessageResponse;
import com.busbooking.data.repository.BookTicketsRepository;
import com.busbooking.data.repository.BusDetailsRepository;
import com.busbooking.data.repository.UserRepository;
import com.busbooking.user.request.BookTicketsDto;
import com.busbooking.user.request.PassengerDetailsDto;
import com.busbooking.user.request.UpdateSeatCount;
import com.busbooking.user.response.PassengerResponse;
import com.busbooking.user.response.TicketResponse;
import com.busbooking.user.response.UserDetails;
import com.busbooking.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	BusDetailsRepository busDetailsRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	BookTicketsRepository bookTicketsRepository;

	@Autowired
	Environment env;

	@Autowired
	RestTemplate restTemplate;

	@Override
	public ResponseEntity<?> viewAllBus() {
		try {
			List<BusDetails> allBus = busDetailsRepository.findAll();
			if (Objects.isNull(allBus)) {
				return ResponseEntity
						.ok(new MessageResponse(env.getProperty("datd.not.found"), HttpStatus.NOT_FOUND.value()));
			}

			return ResponseEntity
					.ok(new MessageResponse(HttpStatus.OK.value(), env.getProperty("fetched.busDetails"), allBus));
		} catch (Exception e) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("failed.fetch.busDetails"), HttpStatus.NOT_FOUND.value()));
		}
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

	@Transactional
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
		List<PassengerResponse> allPassenger = new ArrayList<>();
		PassengerResponse psngResponse = null;
		TicketResponse ticketResponse = null;
		for (PassengerDetailsDto PassengerInfo : bookTicketsDto.getPasanger()) {

			passengerDeatils = BookTickets.builder().PassengerName(PassengerInfo.getPname()).age(PassengerInfo.getAge())
					.gender(PassengerInfo.getGender()).date(bus.getDate()).seatNo(PassengerInfo.getSeatNo()).busId(bus)
					.status(TicketStatus.CONFIRMED).userId(user).build();

			bookTicketsRepository.save(passengerDeatils);

			psngResponse = PassengerResponse.builder().ticketId(passengerDeatils.getTicketId())
					.PassengerName(passengerDeatils.getPassengerName()).age(passengerDeatils.getAge())
					.gender(passengerDeatils.getGender()).date(bus.getDate()).seatNo(passengerDeatils.getSeatNo())
					.status(TicketStatus.CONFIRMED).build();

			allPassenger.add(psngResponse);
		}
		System.out.println("Allpassanger=" + allPassenger.size());

		int seatCount = allPassenger.size();

		UserDetails userInfo = UserDetails.builder().userId(user.getId()).userName(user.getUsername())
				.emailId(user.getEmail()).build();

		ticketResponse = TicketResponse.builder().userDetails(userInfo).busDetails(bus).passengers(allPassenger)
				.build();

		UpdateSeatCount seat = UpdateSeatCount.builder().busId(bus.getId()).seatCount(seatCount).build();

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl("http://localhost:8083/api/admin/update/seat/count").queryParam("busId", seat.getBusId())
				.queryParam("seatCount", seat.getSeatCount());
		HttpEntity<?> entity = new HttpEntity<>(seat);
		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, entity,
				String.class);

		return ResponseEntity.ok(new MessageResponse(HttpStatus.OK.value(), env.getProperty("busticket.booked.success"),
				ticketResponse));
	}

	@Override
	public ResponseEntity<?> viewTicketsByCustomerId(String id) {
		if (Objects.isNull(id)) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("invalid.input"), HttpStatus.BAD_REQUEST.value()));
		}
		try {
			User userId = userRepository.findById(id).get();
			if (Objects.isNull(userId)) {
				return ResponseEntity
						.ok(new MessageResponse(env.getProperty("customer.not.found"), HttpStatus.BAD_REQUEST.value()));
			}
			List<BookTickets> tickets = bookTicketsRepository.findByUserId(userId);

			return ResponseEntity.ok(
					new MessageResponse(HttpStatus.OK.value(), env.getProperty("busticket.fteched.success"), tickets));
		} catch (Exception e) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("ticket.not.found"), HttpStatus.NOT_FOUND.value()));
		}
	}

	@Override
	public ResponseEntity<?> viewTicketsByTicketId(String tId) {
		if (Objects.isNull(tId)) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("invalid.input"), HttpStatus.BAD_REQUEST.value()));
		}
		try {
			BookTickets tickets = bookTicketsRepository.findById(tId).get();
			if (Objects.isNull(tickets)) {
				return ResponseEntity
						.ok(new MessageResponse(env.getProperty("ticket.not.found"), HttpStatus.BAD_REQUEST.value()));
			}
			return ResponseEntity
					.ok(new MessageResponse(HttpStatus.OK.value(), env.getProperty("ticket.fteched.success"), tickets));
		} catch (Exception e) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("ticket.not.found"), HttpStatus.NOT_FOUND.value()));
		}
	}

}
