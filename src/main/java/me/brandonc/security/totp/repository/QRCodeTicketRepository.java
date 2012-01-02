package me.brandonc.security.totp.repository;

public interface QRCodeTicketRepository {

	void createTicket(String name);

	boolean useTicket(String name);
}
