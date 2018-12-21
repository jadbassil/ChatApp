package com.ChatApp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ChatApp.models.Friend;

public interface FriendsRepository extends CrudRepository<Friend, Integer>{

}
