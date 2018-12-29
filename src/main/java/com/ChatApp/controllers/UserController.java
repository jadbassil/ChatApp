package com.ChatApp.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
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
			List<User> users = new ArrayList<User>();
			Iterable<User> usersIt = userRepository.findAll();
			for (User u : usersIt) {
				if(u.getId() == user.getId())
					continue;
				users.add(u);
			}
			mav.addObject("users", users);
			mav.setViewName("search");
			return mav;
		}
		if(search.contains("@")) {
			User userBymail  = userRepository.findByEmail(search);
			if(userBymail == null || userBymail.getId() == user.getId())
				return new ModelAndView(new RedirectView("/search?found=false", true));
			mav.addObject("users", userBymail);	
			return mav;
		}
		if(search != ""){
			User userByUsername = userRepository.findByUsername(search);
			if(userByUsername == null /*|| userByUsername.getId() == user.getId()*/)
				return new ModelAndView(new RedirectView("/search?found=false", true));
			mav.addObject("users", userByUsername);
		}
		return mav;
	}
	
	
	@GetMapping("/profile")
	public ModelAndView viewProfile(@RequestParam(value="id",required=false) int id, HttpSession session) {
		ModelAndView mav = new ModelAndView("/profile");
		//User user = (User) session.getAttribute("user");
		 User userById=userRepository.findById(id).orElse(null);
		 if(userById!=null)
			 mav.addObject("user", userById);
		return mav;
	}
	
	@GetMapping("/editProfile")
	public String editProfile(Model model) {
		model.addAttribute("user", new User());
		return "/editProfile";
	}
	
	@PostMapping("/editProfile")
	public String editSubmit(@Valid User user, BindingResult bindingResult, Model model,HttpSession session) {
		//check for errors
        if (bindingResult.hasErrors()) {
           return "/editProfile";
		}
		
		System.out.println("bindingResult :"+bindingResult.hasErrors());
        if(userRepository.findByUsername(user.getUsername()) != null) {
			return "redirect:/editProfile?error=false";
        }
		   
		User user1 = (User) session.getAttribute("user");
		if(user1==null) {
			return "redirect:/error";
		}
		int id=user1.getId();
		
		User userToUpdate=userRepository.findById(id).orElse(null);
		
		userToUpdate.setFname(user.getFname());
		userToUpdate.setLname(user.getLname());
		userToUpdate.setEmail(user.getEmail());
		userToUpdate.setUsername(user.getUsername());
		userToUpdate.setPhone(user.getPhone());
		userToUpdate.setGender(user.getGender());
		System.out.println(user.getFname()+"   "+user.getLname()+"  "+user.getEmail());
		String password = user.getPassword();
		password = BCrypt.hashpw(password, BCrypt.gensalt());
		userToUpdate.setPassword(password);
		try {
			userRepository.save(userToUpdate);
		}catch (Exception e) {
			return "redirect:/error";
		}
		return "redirect:/editProfile?edited=true";
      }
	
	
	/*
	public ModelAndView editProfile(HttpSession session) {
		ModelAndView mav = new ModelAndView("/editProfile");
		User userToUpdate = (User) session.getAttribute("user");
		User user=userRepository.findById(userToUpdate.getId()).orElse(null);
		
		userToUpdate.setFname(user.getFname());
		userToUpdate.setLname(user.getLname());
		userToUpdate.setEmail(user.getEmail());
		userToUpdate.setPassword(user.getPassword());
		
		userRepository.save(userToUpdate);
		
		return mav;
	}*/
	
}
