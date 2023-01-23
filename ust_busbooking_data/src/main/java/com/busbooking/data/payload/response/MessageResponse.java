package com.busbooking.data.payload.response;



import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MessageResponse {

	private int status;
	private String message;
	private Object response;
	private HttpStatus httpsStatus;

	public MessageResponse(String message, int status) {
		this.message = message;
		this.status = status;
	}

	public MessageResponse(int status, String message, String string) {
		// TODO Auto-generated constructor stub
		this.status=status;
		this.message=message;
	}

	public MessageResponse(int status, String message, Object jwtResponse) {
		// TODO Auto-generated constructor stub
		this.status=status;
		this.message=message;
		this.response = jwtResponse;
	}

}

