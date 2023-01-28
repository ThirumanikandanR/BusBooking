package com.busbooking.data.db;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.busbooking.data.enums.ERole;
import com.busbooking.data.model.Role;
import com.busbooking.data.repository.RoleRepository;

@Component
public class DBOperationRunner implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;

	@Override
	public void run(String... args) throws Exception {

		if (roleRepository.count() == 0) {

			long size = roleRepository.count();
			System.out.println("Repo Size" + size);

			roleRepository.saveAll(Arrays.asList(

					new Role("9ab2cb7e-7c27-11eb-9439-0242ac130002", ERole.ROLE_CUSTOMER),
					new Role("9ab2cb7e-7c27-11eb-9439-0242ac131234", ERole.ROLE_ADMIN))

			);

		}
	}

}
