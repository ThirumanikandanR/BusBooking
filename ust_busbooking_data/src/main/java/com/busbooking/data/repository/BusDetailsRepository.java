package com.busbooking.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.busbooking.data.model.BusDetails;

@Repository
public interface BusDetailsRepository extends JpaRepository<BusDetails, String>{

}
