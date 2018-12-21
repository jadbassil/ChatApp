package com.ChatApp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ChatApp.models.Chat;

public interface ChatRepository extends CrudRepository<Chat, Integer>{

}
