package com.busbooking.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.busbooking.data.model.BookTickets;

@Repository
public interface PassengerDetailsRepository extends JpaRepository<BookTickets, String>{

}
