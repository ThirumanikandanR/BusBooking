package com.busbooking.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.busbooking.data.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

}
