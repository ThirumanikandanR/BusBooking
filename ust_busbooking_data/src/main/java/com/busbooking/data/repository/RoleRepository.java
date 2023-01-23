package com.busbooking.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.busbooking.data.enums.ERole;
import com.busbooking.data.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>{

	
	
	Optional<Role> findByName(ERole name);

	Boolean existsByName(ERole name);


}
