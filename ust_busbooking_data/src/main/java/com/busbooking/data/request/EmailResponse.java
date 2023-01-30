package com.busbooking.data.request;

import lombok.Data;

@Data
public class EmailResponse {

	private String toemail;
	private String body;
	private String subject;
	private byte[] invoice;

}
