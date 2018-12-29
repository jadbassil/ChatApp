package com.ChatApp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	@GetMapping("/")
	public String defaultChat(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "auth/login";
		}
		return "chat";
	}
	
	@GetMapping({"/chat", "/chat/{id}"})
	public ModelAndView getChat(@RequestParam	(name="id", required=false) String id, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return new ModelAndView("auth/login");
		}
		ModelAndView mav = new ModelAndView("/chat");
		mav.addObject("user", user);
		List<Chat> privateChats = new ArrayList<>();
		List<Chat> groupChats = new ArrayList<>();
		Map<String, Integer> privateChatsNames = new HashMap<String, Integer>();
		for(Chat c : user.getChats()) {
			if(c.getType() == 0) {
				privateChats.add(c);
				for(User u: c.getUsers()) {
					if(!u.getUsername().equals(user.getUsername()))
						privateChatsNames.put(u.getUsername(), c.getChatId());
				}
			}				
			else if(c.getType() == 1)
				groupChats.add(c);
		}
		if(id != null) {
			System.out.println(id);
			int id1 = Integer.parseInt(id);
			Chat chat = chatRepository.findById(id1).orElse(null);
			mav.addObject("chat", chat);
		}
		System.out.println(privateChatsNames);
		mav.addObject("privateChatsNames", privateChatsNames);
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
		if(user1 == null)
			return new ModelAndView("auth/login");
		Chat chat = new Chat();
		chat.setAdmin(user1.getId());
		chat.setType(0); //private		
		chatRepository.save(chat);
		user1.getChats().add(chat); user2.getChats().add(chat);
		List<Chat> privateChats = new ArrayList<>();
		List<Chat> groupChats = new ArrayList<>();
		Map<String, Integer> privateChatsNames = new HashMap<String, Integer>();
		for(Chat c : user1.getChats()) {
			if(c.getType() == 0) {
				privateChats.add(c);
				for(User u: c.getUsers()) {
					if(!u.getUsername().equals(user1.getUsername()))
						privateChatsNames.put(u.getUsername(), c.getChatId());
				}
			}				
			else if(c.getType() == 1)
				groupChats.add(c);
		}
		userRepository.save(user1); userRepository.save(user2);
		mav.addObject("chat", chat);
		mav.addObject("privateChats", privateChats);
		mav.addObject("groupChats", groupChats);
		mav.addObject("privateChatsNames", privateChatsNames);
		return mav;
	}
		

}
