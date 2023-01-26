package com.busbooking.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.busbooking.data.model.BookTickets;
import com.busbooking.data.model.BusDetails;
import com.busbooking.data.model.User;

@Repository
public interface BookTicketsRepository extends JpaRepository<BookTickets, String> {

	@Query("select b from BookTickets b where b.userId=:userId")
	List<BookTickets> findByUserId(@RequestParam("userId") User userId);

	@Query("select b from BookTickets b where b.busId=:bId")
	List<BookTickets> findByBusId(@RequestParam("bId") BusDetails bId);



}
