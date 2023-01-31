package com.busbooking.admin.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.busbooking.admin.request.BusDetailsDto;
import com.busbooking.admin.response.AllPassengerResponse;
import com.busbooking.admin.response.PassengerDetails;
import com.busbooking.admin.service.AdminService;
import com.busbooking.data.enums.TicketStatus;
import com.busbooking.data.model.BookTickets;
import com.busbooking.data.model.BusDetails;
import com.busbooking.data.payload.response.MessageResponse;
import com.busbooking.data.repository.BookTicketsRepository;
import com.busbooking.data.repository.BusDetailsRepository;
import com.busbooking.data.repository.RoleRepository;
import com.busbooking.data.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	BusDetailsRepository busdeRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	Environment env;

	@Autowired
	BookTicketsRepository bookTicketsRepository;

	@Override
	public ResponseEntity<?> saveBusDetails(BusDetailsDto busDetailsDto) {
		if (Objects.isNull(busDetailsDto)) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("invalid.input"), HttpStatus.BAD_REQUEST.value()));
		}
		BusDetails busDetails = null;

		try {
			busDetails = BusDetails.builder().busNo(busDetailsDto.getBusNo()).busName(busDetailsDto.getBusName())
					.driverName(busDetailsDto.getDriverName()).contNum(busDetailsDto.getContNum())
					.noOfSeats(busDetailsDto.getNoOfSeats()).date(busDetailsDto.getDate())
					.depTime(busDetailsDto.getDepTime()).arvTime(busDetailsDto.getArvTime())
					.tkkPrice(busDetailsDto.getTkkPrice()).fromPlace(busDetailsDto.getFromPlace())
					.toPlace(busDetailsDto.getToPlace()).build();
			busdeRepository.save(busDetails);

			return ResponseEntity
					.ok(new MessageResponse(HttpStatus.OK.value(), env.getProperty("busDetails.saved"), busDetails));
		} catch (Exception e) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("BusDetails.not.saved"), HttpStatus.BAD_REQUEST.value()));
		}
	}

	@Override
	public ResponseEntity<?> updateBusDetails(String id, BusDetailsDto updaDto) {
		if (Objects.isNull(updaDto) || Objects.isNull(id)) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("invalid.input"), HttpStatus.BAD_REQUEST.value()));
		}

		try {
			Optional<BusDetails> busId = busdeRepository.findById(id);
			if (Objects.isNull(busId)) {
				return ResponseEntity
						.ok(new MessageResponse(env.getProperty("Invalid.id"), HttpStatus.BAD_REQUEST.value()));
			} else {
				BusDetails updateBus = busId.get();

				updateBus.setBusNo(updaDto.getBusNo());
				updateBus.setBusName(updaDto.getBusName());
				updateBus.setDriverName(updaDto.getDriverName());
				updateBus.setContNum(updaDto.getContNum());
				updateBus.setNoOfSeats(updaDto.getNoOfSeats());
				updateBus.setDate(updaDto.getDate());
				updateBus.setArvTime(updaDto.getArvTime());
				updateBus.setTkkPrice(updaDto.getTkkPrice());
				updateBus.setFromPlace(updaDto.getFromPlace());
				updateBus.setToPlace(updaDto.getToPlace());

				busdeRepository.save(updateBus);

				return ResponseEntity.ok(
						new MessageResponse(HttpStatus.OK.value(), env.getProperty("updated.busDetails"), updateBus));

			}

		} catch (Exception e) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("update.BusDetails.fail"), HttpStatus.BAD_REQUEST.value()));
		}
	}

	@Override
	public ResponseEntity<?> cancelBus(String busId) {
		try {
			Optional<BusDetails> cancelBus = null;
			if (Objects.nonNull(busId)) {
				cancelBus = busdeRepository.findById(busId);
			}
			if (cancelBus.isPresent()) {
				busdeRepository.deleteById(busId);
			}
			return ResponseEntity
					.ok(new MessageResponse(HttpStatus.OK.value(), env.getProperty("cancelld.bus.success"), cancelBus));
		} catch (Exception e) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("cancel.bus.fail"), HttpStatus.BAD_REQUEST.value()));
		}

	}

	@Override
	public ResponseEntity<?> viewAllUsers() {
//		String userRoleId="9ab2cb7e-7c27-11eb-9439-0242ac130002";
//	Optional<Role> userId=roleRepository.findById(userRoleId);
//	User user=userRepository.findByusername(userRoleId);
		return null;
	}

	@Override
	public ResponseEntity<?> viewAllPassengersByBusId(String busId) {

		if (Objects.isNull(busId)) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("invalid.input"), HttpStatus.BAD_REQUEST.value()));
		}
		Optional<BusDetails> bId = busdeRepository.findById(busId);
		if (!bId.isPresent()) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("bus.not.found"), HttpStatus.BAD_REQUEST.value()));
		}
		List<BookTickets> passengers = bookTicketsRepository.findByBusId(bId.get());

		List<PassengerDetails> allpassenger = new ArrayList<>();

		for (BookTickets pass : passengers) {
			if(pass.getStatus().equals(TicketStatus.CONFIRMED)) {

			PassengerDetails passengerDetails = PassengerDetails.builder().PassengerName(pass.getPassengerName())
					.ticketId(pass.getTicketId()).age(pass.getAge()).gender(pass.getGender()).seatNo(pass.getSeatNo())
					.date(pass.getDate()).status(pass.getStatus()).build();

			allpassenger.add(passengerDetails);
			}
		}
		AllPassengerResponse allPassengerResponse = AllPassengerResponse.builder().bus(bId.get()).passengers(allpassenger)
				.build();

		return ResponseEntity.ok(new MessageResponse(HttpStatus.OK.value(),
				env.getProperty("passenger.fteched.success"), allPassengerResponse));

	}

	@Override
	public ResponseEntity<?> updateSeatCount(String busId, int seatCount) {
		if (Objects.isNull(busId) && Objects.isNull(seatCount)) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("invalid.input"), HttpStatus.BAD_REQUEST.value()));
		}
		try {
			Optional<BusDetails> busInfo = busdeRepository.findById(busId);
			if (Objects.isNull(busId)) {
				return ResponseEntity
						.ok(new MessageResponse(env.getProperty("bus.not.found"), HttpStatus.BAD_REQUEST.value()));
			} else {
				BusDetails updateSeatCount = busInfo.get();

				int totalSeats = updateSeatCount.getNoOfSeats();

				int noOfSeats = totalSeats - seatCount;

				updateSeatCount.setNoOfSeats(noOfSeats);

				busdeRepository.save(updateSeatCount);

				return ResponseEntity.ok(new MessageResponse(HttpStatus.OK.value(),
						env.getProperty("updated.seat.success"), updateSeatCount));

			}
		} catch (Exception e) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("update.seatCount.fail"), HttpStatus.BAD_REQUEST.value()));
		}
	}
	
}
