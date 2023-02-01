package com.busbooking.user.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.busbooking.data.request.EmailResponse;
import com.busbooking.user.request.BookTicketsDto;
import com.busbooking.user.request.PassengerDetailsDto;
import com.busbooking.user.request.UpdateSeatCount;
import com.busbooking.user.response.PassengerResponse;
import com.busbooking.user.response.TicketResponse;
import com.busbooking.user.response.UserDetails;
import com.busbooking.user.service.UserService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

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

		Optional<User> user = userRepository.findById(bookTicketsDto.getUserId());
		if (!user.isPresent()) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("user.not.found"), HttpStatus.BAD_REQUEST.value()));
		}

		Optional<BusDetails> bus = busDetailsRepository.findById(bookTicketsDto.getBusId());
		if (!bus.isPresent()) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("bus.not.found"), HttpStatus.BAD_REQUEST.value()));
		}

		BookTickets passengerDeatils = null;
		List<PassengerResponse> allPassenger = new ArrayList<>();
		PassengerResponse psngResponse = null;
		TicketResponse ticketResponse = null;
		for (PassengerDetailsDto PassengerInfo : bookTicketsDto.getPasanger()) {

			passengerDeatils = BookTickets.builder().PassengerName(PassengerInfo.getPname()).age(PassengerInfo.getAge())
					.gender(PassengerInfo.getGender()).date(bus.get().getDate()).seatNo(PassengerInfo.getSeatNo())
					.busId(bus.get()).status(TicketStatus.CONFIRMED).userId(user.get()).build();

			bookTicketsRepository.save(passengerDeatils);

			psngResponse = PassengerResponse.builder().ticketId(passengerDeatils.getTicketId())
					.PassengerName(passengerDeatils.getPassengerName()).age(passengerDeatils.getAge())
					.gender(passengerDeatils.getGender()).date(bus.get().getDate()).seatNo(passengerDeatils.getSeatNo())
					.status(TicketStatus.CONFIRMED).build();

			allPassenger.add(psngResponse);
		}
		System.out.println("Allpassanger=" + allPassenger.size());

		int seatCount = allPassenger.size();

		UserDetails userInfo = UserDetails.builder().userId(user.get().getId()).userName(user.get().getUsername())
				.emailId(user.get().getEmail()).build();

		ticketResponse = TicketResponse.builder().userDetails(userInfo).busDetails(bus.get()).passengers(allPassenger)
				.build();

		UpdateSeatCount seat = UpdateSeatCount.builder().busId(bus.get().getId()).seatCount(seatCount).build();

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl("http://localhost:8083/api/admin/update/seat/count").queryParam("busId", seat.getBusId())
				.queryParam("seatCount", seat.getSeatCount());
		HttpEntity<?> entity = new HttpEntity<>(seat);
		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, entity,
				String.class);

		try {
			sendInvoice(ticketResponse);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			Optional<User> userId = userRepository.findById(id);
			if (!userId.isPresent()) {
				return ResponseEntity
						.ok(new MessageResponse(env.getProperty("customer.not.found"), HttpStatus.BAD_REQUEST.value()));
			}
			List<BookTickets> tickets = bookTicketsRepository.findByUserId(userId.get());

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
			Optional<BookTickets> tickets = bookTicketsRepository.findById(tId);
			if (!tickets.isPresent()) {
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

	public void sendInvoice(TicketResponse ticketResponse) throws DocumentException {

		String ticketStatus = null;

		try {
			// generate QR code here
			String data = ticketResponse.toString();

			int width = 200;
			int height = 200;
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
			java.awt.Image qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

			// Convert the QR code image to a iText Image
			Image image = Image.getInstance(qrImage, null);

			// send logo in ticket pdf here
			String logoUrl = "images\\TicketImage.JPG";
			Image img = Image.getInstance(logoUrl);
			img.scaleAbsolute(200, 200);
			Phrase phrase = new Phrase();
			phrase.add(new Chunk(img, 350, -200));
			System.out.println(img);

			Document document = new Document();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	         FileOutputStream baos=new FileOutputStream(new File("E:\\BusTicket_Booking\\InvoiceLocation\\Tickets.pdf"));
			PdfWriter.getInstance(document, baos);

			document.open();

			document.add(new Paragraph("Bus Ticket Invoice"));
			document.add(new Paragraph(phrase));
			document.add(new Paragraph("------------------------------------------------------------------"));
			document.add(new Paragraph("User Details:"));
			document.add(new Paragraph("User Id:" + ticketResponse.getUserDetails().getUserId()));
			document.add(new Paragraph("User Name:" + ticketResponse.getUserDetails().getUserName()));
			document.add(new Paragraph("User EmailId:" + ticketResponse.getUserDetails().getEmailId()));
			document.add(new Paragraph("-------------------------------------------------------------------"));
			document.add(new Paragraph("passenger Details:"));
			for (PassengerResponse invoice : ticketResponse.getPassengers()) {
				document.add(new Paragraph("Passenger Name:" + invoice.getPassengerName()));
				document.add(new Paragraph("Passenger age:" + invoice.getAge()));
				document.add(new Paragraph("Passenger gender:" + invoice.getGender()));
				document.add(new Paragraph("Passenger seatNo:" + invoice.getSeatNo()));
				document.add(new Paragraph("Ticket Status:" + invoice.getStatus()));

				ticketStatus = invoice.getStatus().toString();
			}
			document.add(new Paragraph("------------------------------------------------------------------"));
			document.add(new Paragraph("Bus Details:"));
			document.add(new Paragraph("Bus Id:" + ticketResponse.getBusDetails().getId()));
			document.add(new Paragraph("Bus No:" + ticketResponse.getBusDetails().getBusNo()));
			document.add(new Paragraph("Bus Name:" + ticketResponse.getBusDetails().getBusName()));
			document.add(new Paragraph("Bus DriverName:" + ticketResponse.getBusDetails().getDriverName()));
			document.add(new Paragraph("Contact Num:" + ticketResponse.getBusDetails().getContNum()));
			document.add(new Paragraph("From Place:" + ticketResponse.getBusDetails().getFromPlace()));
			document.add(new Paragraph("To Place:" + ticketResponse.getBusDetails().getToPlace()));
			document.add(new Paragraph("Departure Time:" + ticketResponse.getBusDetails().getDepTime()));
			document.add(new Paragraph("Arriving Time:" + ticketResponse.getBusDetails().getArvTime()));
			document.add(new Paragraph("Ticket Price:" + ticketResponse.getBusDetails().getTkkPrice()));
			if (ticketStatus.equals("CONFIRMED")) {
			document.add(image);
			}
			document.close();

			byte[] pdfBytes = baos.toByteArray();
			System.out.println("PDF created successfully with " + pdfBytes.length + " bytes.");

			String url = "http://localhost:5000/api/email/send";
			EmailResponse emailResponse = new EmailResponse();
			emailResponse.setToemail(ticketResponse.getUserDetails().getEmailId());
			emailResponse.setSubject("Ust BusTicket Invoice");
			if (ticketStatus.equals("CONFIRMED")) {
				emailResponse.setBody("Please find the below Ticket Confirmation Details for your HAPPY JOURNEY!!");
			} else {
				emailResponse.setBody("below Ticket Cancelled Successfully!");
			}
			emailResponse.setInvoice(pdfBytes);
			postData(url, emailResponse);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception occured in Invoice generation!");
		}
	}

	public ResponseEntity<String> postData(String url, Object request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> entity = new HttpEntity<>(request, headers);
		return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

	}

	@Override
	public ResponseEntity<?> cancelTickets(String tId) {
		TicketResponse ticketResponse = null;
		PassengerResponse psngResponse = null;
		List<PassengerResponse> allPassenger = new ArrayList<>();
		if (Objects.isNull(tId)) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("invalid.input"), HttpStatus.BAD_REQUEST.value()));
		}
		try {
			Optional<BookTickets> tickets = bookTicketsRepository.findById(tId);
			if (!tickets.isPresent()) {
				return ResponseEntity
						.ok(new MessageResponse(env.getProperty("ticket.not.found"), HttpStatus.BAD_REQUEST.value()));
			} else {
				BookTickets cancelTickets = tickets.get();

				cancelTickets.setStatus(TicketStatus.CANCELLED);

				bookTicketsRepository.save(cancelTickets);

				UserDetails userInfo = UserDetails.builder().userId(cancelTickets.getUserId().getId())
						.userName(cancelTickets.getUserId().getUsername()).emailId(cancelTickets.getUserId().getEmail())
						.build();

				psngResponse = PassengerResponse.builder().ticketId(cancelTickets.getTicketId())
						.PassengerName(cancelTickets.getPassengerName()).age(cancelTickets.getAge())
						.gender(cancelTickets.getGender()).date(cancelTickets.getBusId().getDate())
						.seatNo(cancelTickets.getSeatNo()).status(cancelTickets.getStatus()).build();
				allPassenger.add(psngResponse);

				ticketResponse = TicketResponse.builder().userDetails(userInfo).busDetails(cancelTickets.getBusId())
						.passengers(allPassenger).build();

				try {
					sendInvoice(ticketResponse);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return ResponseEntity.ok(new MessageResponse(HttpStatus.OK.value(),
						env.getProperty("ticket.cancel.success"), ticketResponse));

			}

		} catch (Exception e) {
			return ResponseEntity
					.ok(new MessageResponse(env.getProperty("ticket.not.found"), HttpStatus.NOT_FOUND.value()));
		}
	}
}
