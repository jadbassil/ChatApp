<<<<<<< HEAD
package com.ChatApp.controllers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.ChatApp.models.Chat;
import com.ChatApp.models.User;
import com.ChatApp.repositories.UserRepository;


@Controller
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/index")
	public String index() {
		return "index";
	}
	
	@GetMapping("/signup")
	public String signupForm(Model model) {
		model.addAttribute("user", new User());
		return "auth/signup";
	}
	
	@PostMapping("/signup")
	public String signupSubmit(@Valid User user, BindingResult bindingResult, Model model) {
		//check for errors
        if (bindingResult.hasErrors()) {
           return "auth/signup";
        }
		if(userRepository.findByUsername(user.getUsername()) != null)
			return "redirect:/signup?username=false";
		
		if(userRepository.findByEmail(user.getEmail()) != null)
			return "redirect:/signup?email=false";
		
		String password = user.getPassword();
		password = BCrypt.hashpw(password, BCrypt.gensalt());
		user.setPassword(password);
		try {
			userRepository.save(user);
			MailController.sendMail("jad-bassil@hotmail.com", user.getFname());
		}catch (Exception e) {
			return "redirect:/error";
		}
		return "redirect:/login?signup=true";
	}
	
	@GetMapping("/login")
	public String login() {
		return "auth/login";
	}

	@PostMapping("/login")
	public ModelAndView loginCheck(@RequestParam String username, @RequestParam String password, HttpSession session) {
		User user = userRepository.findByUsername(username);
		ModelAndView mav = new ModelAndView();
		if(user == null) {
			//return "redirect:/login?error=false";
			return new ModelAndView(new RedirectView("/login?error=false", true));
		}else if(!BCrypt.checkpw(password, user.getPassword())){
			//return "redirect:/login?error=false";
			return new ModelAndView(new RedirectView("/login?error=false", true));

		}else if(user.getVerified() == false){
			//return "redirect:/login?verified=false";
			return new ModelAndView(new RedirectView("/login?verified=false", true));
		}else {
			session.setAttribute("user", user);
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
			mav.addObject("privateChats", privateChats);
			mav.addObject("privateChatsNames", privateChatsNames);
			mav.addObject("groupChats", groupChats);
			mav.addObject("user", user);
			mav.setViewName("/chat");
			return mav;
		}		
	}
	
	@GetMapping("/verifymail")
	public String verifymail(@RequestParam String email) {
		User user = userRepository.findByEmail(email);
		user.setVerified(true);
		userRepository.save(user);
		return "redirect:/login?verified="+user.getVerified();
	}
}
=======
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

import com.ChatApp.models.Chat;
import com.ChatApp.models.User;
import com.ChatApp.repositories.UserRepository;


@Controller
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/index")
	public String index() {
		return "index";
	}
	
	@GetMapping("/signup")
	public String signupForm(Model model) {
		model.addAttribute("user", new User());
		return "auth/signup";
	}

	
	@PostMapping("/signup")
	public String signupSubmit(@Valid User user, BindingResult bindingResult, Model model) {
		//check for errors
        if (bindingResult.hasErrors()) {
           return "auth/signup";
        }
			
		if(userRepository.findByUsername(user.getUsername()) != null)
			return "redirect:/signup?username=false";
		
		if(userRepository.findByEmail(user.getEmail()) != null)
			return "redirect:/signup?email=false";
		
		String password = user.getPassword();
		password = BCrypt.hashpw(password, BCrypt.gensalt());
		user.setPassword(password);
		try {
			userRepository.save(user);
			MailController.sendMail(user.getEmail(), user.getFname());
		}catch (Exception e) {
			return "redirect:/error";
		}
		return "redirect:/login?signup=true";
	}
	
	@GetMapping("/login")
	public String login() {
		return "auth/login";
	}

	@PostMapping("/login")
	public ModelAndView loginCheck(@RequestParam String username, @RequestParam String password, HttpSession session) {
		User user = userRepository.findByUsername(username);
		ModelAndView mav = new ModelAndView();
		if(user == null) {
			//return "redirect:/login?error=false";
			return new ModelAndView(new RedirectView("/login?error=false", true));
		}else if(!BCrypt.checkpw(password, user.getPassword())){
			//return "redirect:/login?error=false";
			return new ModelAndView(new RedirectView("/login?error=false", true));

		}else if(user.getVerified() == false){
			//return "redirect:/login?verified=false";
			return new ModelAndView(new RedirectView("/login?verified=false", true));
		}else {
			/*when the user logged in , save it in a session*/
			session.setAttribute("user", user);
			List<Chat> privateChats = new ArrayList<>();
			List<Chat> groupChats = new ArrayList<>();
			for(Chat c : user.getChats()) {
				if(c.getType() == 0)
					privateChats.add(c);
				else if(c.getType() == 1)
					groupChats.add(c);
			}
			mav.addObject("privateChats", privateChats);
			mav.addObject("groupChats", groupChats);
			mav.addObject("user", user);
			mav.setViewName("/chat");
			return mav;
		}		
	}
	
	@GetMapping("/verifymail")
	public String verifymail(@RequestParam String email) {
		User user = userRepository.findByEmail(email);
		user.setVerified(true);
		userRepository.save(user);
		return "redirect:/login?verified="+user.getVerified();
	}
}
>>>>>>> 65a819c76ebfae7cadef0a55d0803126f74ec5aa
