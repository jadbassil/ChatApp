package com.ChatApp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ChatApp.models.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	User findByUsername(String username);
	User findByEmail(String email);
}
