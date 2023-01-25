package com.busbooking.data.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.busbooking.data.model.BusDetails;

@Repository
public interface BusDetailsRepository extends JpaRepository<BusDetails, String>{

	@Query("select b from BusDetails b where b.date=:date")
	List<BusDetails> findByDate(@Param("date") LocalDate date);

}
