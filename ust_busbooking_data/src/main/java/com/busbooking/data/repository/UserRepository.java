package com.busbooking.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.busbooking.data.enums.ERole;
import com.busbooking.data.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);

	User findByusername(String username);

	
	

}
