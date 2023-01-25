package com.busbooking.user.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.busbooking.data.model.BusDetails;
import com.busbooking.data.payload.response.MessageResponse;
import com.busbooking.data.repository.BusDetailsRepository;
import com.busbooking.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	BusDetailsRepository busDetailsRepository;

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
		List<BusDetails> sortByDateAndPlace=null;
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

}
