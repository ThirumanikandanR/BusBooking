package com.busbooking.email.response;

public class EmailResponse {

	private String toemail;
	private String body;
	private String subject;
	private byte[] invoice;
	
	public String getToemail() {
		return toemail;
	}
	public void setToemail(String toemail) {
		this.toemail = toemail;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public byte[] getInvoice() {
		return invoice;
	}
	public void setInvoice(byte[] invoice) {
		this.invoice = invoice;
	}
	
	

}
