package com.busbooking.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetails {
	
	private String userId;
	
	private String userName;
	
	private String emailId;
	
	

}
