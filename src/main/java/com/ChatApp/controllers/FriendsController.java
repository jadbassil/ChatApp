package com.ChatApp.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ChatApp.models.Friend;
import com.ChatApp.models.User;
import com.ChatApp.repositories.UserRepository;

@Controller
public class FriendsController {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/addFriend")
	public String addFriend(@RequestParam String id, HttpSession session ) {
		User user = (User) session.getAttribute("user");
		List<Friend> friends = user.getFriends();
		friends.add(new Friend(user, Integer.valueOf(id), 0));
		userRepository.save(user);
		return "index";
	}
	
}
