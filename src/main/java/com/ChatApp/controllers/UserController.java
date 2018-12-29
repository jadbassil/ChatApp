package com.ChatApp.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ChatApp.models.Chat;
import com.ChatApp.models.User;
import com.ChatApp.repositories.UserRepository;

@Controller
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/search")
	public ModelAndView searchUsers(@RequestParam(value="search", required=false) String search, HttpSession session) {
		ModelAndView mav = new ModelAndView("/search");
		User user = (User) session.getAttribute("user");
		System.out.println(search);
		if(user == null)
			return new ModelAndView("/auth/login");
		if(search == null)
			return new ModelAndView("search");
		if(search == "") {
			boolean add;
			List<User> users = new ArrayList<User>();
			Iterable<User> usersIt = userRepository.findAll();
			for (User u : usersIt) {
				add=true;
				if(u.getId() == user.getId())
					add=false;
				for(Chat c: u.getChats()) {
					if(c.getUsers().contains(u))
						add=false;
				}	
				if(add)
					users.add(u);
			}
			if(users.isEmpty())
				return new ModelAndView(new RedirectView("/search?found=false", true));
			mav.addObject("users", users);
			mav.setViewName("search");
			return mav;
		}
		if(search.contains("@")) {
			User userBymail  = userRepository.findByEmail(search);
			if(userBymail == null || userBymail.getId() == user.getId())
				return new ModelAndView(new RedirectView("/search?found=false", true));
			for(Chat c: user.getChats())
				if(c.getUsers().contains(userBymail))
					return new ModelAndView(new RedirectView("/search?found=false", true));
			mav.addObject("users", userBymail);	
			return mav;
		}
		if(search != ""){
			User userByUsername = userRepository.findByUsername(search);
			if(userByUsername == null || userByUsername.getId() == user.getId())
				return new ModelAndView(new RedirectView("/search?found=false", true));
			for(Chat c: user.getChats()) {
				System.out.println(userByUsername);
				System.out.println(c.getUsers().contains(userByUsername));
				System.out.println(c.getUsers());
				if(c.getUsers().contains(userByUsername))
					return new ModelAndView(new RedirectView("/search?found=false", true));
			}
			mav.addObject("users", userByUsername);
		}
		return mav;
	}
}
