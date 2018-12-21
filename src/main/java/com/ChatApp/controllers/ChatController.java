package com.ChatApp.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ChatApp.models.Chat;
import com.ChatApp.models.User;
import com.ChatApp.repositories.ChatRepository;
import com.ChatApp.repositories.UserRepository;

@Controller
public class ChatController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ChatRepository chatRepository;
	
	@GetMapping("/chat")
	public ModelAndView chat(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return new ModelAndView("auth/login");
		}
		ModelAndView mav = new ModelAndView("/chat");
		mav.addObject("user", user);
		List<Chat> privateChats = new ArrayList<>();
		List<Chat> groupChats = new ArrayList<>();
		for(Chat c : user.getChats()) {
			if(c.getType() == 0)
				privateChats.add(c);
			else if(c.getType() == 1)
				groupChats.add(c);
		}
		mav.addObject("user", user);
		mav.addObject("privateChats", privateChats);
		mav.addObject("groupChats", groupChats);
		mav.setViewName("/chat");
		return mav;
	}
	
	@GetMapping("/newchat")
	public ModelAndView newchat(@RequestParam String id, HttpSession session) {
		int newId = Integer.parseInt(id);
		ModelAndView mav = new ModelAndView("/chat");
		User user1 = (User) session.getAttribute("user");
		User user2 = userRepository.findById(newId).orElse(null);
		
		Chat chat = new Chat();
		chat.setAdmin(user1.getId());
		chat.setType(0); //private
		chat.setName(user2.getUsername());
		
		chatRepository.save(chat);
		user1.getChats().add(chat); user2.getChats().add(chat);
		userRepository.save(user1); userRepository.save(user2);
		List<Chat> privateChats = new ArrayList<>();
		List<Chat> groupChats = new ArrayList<>();
		for(Chat c : user1.getChats()) {
			if(c.getType() == 0)
				privateChats.add(c);
			else if(c.getType() == 1)
				groupChats.add(c);
		}
		mav.addObject("chat", chat);
		mav.addObject("privateChats", privateChats);
		mav.addObject("groupChats", groupChats);
		return mav;
	}
		

}
