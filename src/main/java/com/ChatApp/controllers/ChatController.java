package com.ChatApp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ChatApp.models.Chat;
import com.ChatApp.models.Message;
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
	public ModelAndView defaultChat(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return new ModelAndView("auth/login");
		}
		User userDb = userRepository.findById(user.getId()).orElse(null);
		session.setAttribute("user", userDb);
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

		System.out.println(privateChatsNames);
		mav.addObject("privateChatsNames", privateChatsNames);
		mav.addObject("privateChats", privateChats);
		mav.addObject("groupChats", groupChats);
		mav.setViewName("/chat");
		return mav;
	}
	
	@GetMapping({"/chat", "/chat/{id}"})
	public ModelAndView getChat(@RequestParam(name="id", required=false) String id, HttpSession session) {
		System.out.println("getChat");
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return new ModelAndView("auth/login");
		}
		User userDb = userRepository.findById(user.getId()).orElse(null);
		session.setAttribute("user", userDb);
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
			Map<String, Message> messages = new HashMap<String, Message>();
			Chat chat = chatRepository.findById(id1).orElse(null);
			mav.addObject("chat", chat);
			for(Message m: chat.getMessages()) {
				User sender = userRepository.findById(m.getSenderId()).orElse(null);
				String name = sender.getFname() + " " + sender.getLname();
				messages.put(name, m);
			}
			System.out.println(messages);
			mav.addObject("messages", messages);
		}

		System.out.println(privateChatsNames);
		mav.addObject("privateChatsNames", privateChatsNames);
		mav.addObject("privateChats", privateChats);
		mav.addObject("groupChats", groupChats);
		mav.setViewName("/chat");
		return mav;	
	}
	
	@GetMapping("/newchat")
	public ModelAndView newPrivateChat(@RequestParam String id, HttpSession session, ModelMap model) {
		int newId = Integer.parseInt(id);
		User user1 = (User) session.getAttribute("user");
		User user2 = userRepository.findById(newId).orElse(null);
		if(user1 == null)
			return new ModelAndView("auth/login");
		for(Chat c: user1.getChats()) {
			if(c.getType() == 0 && c.getUsers().contains(user2))
					return new ModelAndView("redirect:/search?exists=true");
		}
		Chat chat = new Chat();
		chat.setAdmin(user1.getId());
		chat.setType(0); //private	
		chat.getUsers().add(user1); chat.getUsers().add(user2);
		System.out.println(user1);
		chatRepository.save(chat);
		user1.getChats().add(chat); user2.getChats().add(chat);
		session.setAttribute("user", user1);
		System.out.println("new chat id: " + chat.getId());
		return new ModelAndView("redirect:/chat?id="+chat.getId());
	}
	
	/*@GetMapping("/newchat")
	public ModelAndView newPrivateChat(@RequestParam String id, HttpSession session) {
		int newId = Integer.parseInt(id);
		ModelAndView mav = new ModelAndView("/chat");
		User user1 = (User) session.getAttribute("user");
		User user2 = userRepository.findById(newId).orElse(null);
		if(user1 == null)
			return new ModelAndView("auth/login");
		for(Chat c: user1.getChats()) {
			if(c.getType() == 0 && c.getUsers().contains(user2))
					mav.setView(new RedirectView("/search?exists=true"));
		}
		Chat chat = new Chat();
		chat.setAdmin(user1.getId());
		chat.setType(0); //private	
		chat.getUsers().add(user1); chat.getUsers().add(user2);
		System.out.println(user1);
		chatRepository.save(chat);
		user1.getChats().add(chat); user2.getChats().add(chat);
		session.setAttribute("user", user1);
		System.out.println(user1);
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
		mav.addObject("chat", chat);
		mav.addObject("privateChats", privateChats);
		mav.addObject("groupChats", groupChats);
		mav.addObject("privateChatsNames", privateChatsNames);
		return mav;
	}*/
	
	@PostMapping("/newGroupChat")
	public ModelAndView newGroupChat(@RequestParam String groupName, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user == null)
			return new ModelAndView("auth/login");
		Chat chat = new Chat();
		chat.setAdmin(user.getId());
		chat.setName(groupName);
		chat.setType(1); //group
		chat.getUsers().add(user);
		chatRepository.save(chat);
		user.getChats().add(chat);
		session.setAttribute("user", user);
		System.out.println("new chat id: " + chat.getId());
		return new ModelAndView("redirect:/chat?id="+chat.getId());
	}
	/*@PostMapping("/newGroupChat")
	public ModelAndView newGroupChat(@RequestParam String groupName, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = (User) session.getAttribute("user");
		if(user == null)
			return new ModelAndView("auth/login");
		Chat chat = new Chat();
		chat.setAdmin(user.getId());
		chat.setName(groupName);
		chat.setType(1); //group
		chat.getUsers().add(user);
		chatRepository.save(chat);
		user.getChats().add(chat);
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
		mav.addObject("privateChatsNames", privateChatsNames);
		mav.addObject("privateChats", privateChats);
		mav.addObject("groupChats", groupChats);
		mav.setViewName("/chat");
		return mav;	
	}*/
	@PostMapping("/addToGroup")
	public ModelAndView addToGroup(@RequestParam String groupId, @RequestParam String userId, HttpSession session) {
		System.out.println("userID: "+userId+" groupId: "+groupId);
		User user = (User) session.getAttribute("user");
		if(user == null)
			return new ModelAndView("auth/login");
		Chat chat = chatRepository.findById(Integer.valueOf(groupId)).orElse(null);
		User user1 = userRepository.findById(Integer.valueOf(userId)).orElse(null);
		if(chat.getUsers().contains(user1))
			return new ModelAndView(new RedirectView("/search?existsInGroup=true"));
		chat.getUsers().add(user1);
		chatRepository.save(chat);
		System.out.println("new chat id: " + chat.getId());
		return new ModelAndView("redirect:/chat?id="+chat.getId());	
	}
	
	/*@PostMapping("/addToGroup")
	public ModelAndView addToGroup(@RequestParam String groupId, @RequestParam String userId, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = (User) session.getAttribute("user");
		if(user == null)
			return new ModelAndView("auth/login");
		Chat chat = chatRepository.findById(Integer.valueOf(groupId)).orElse(null);
		User user1 = userRepository.findById(Integer.valueOf(userId)).orElse(null);
		if(chat.getUsers().contains(user1))
			return new ModelAndView(new RedirectView("/search?existsInGroup=true"));
		chat.getUsers().add(user1);
		chatRepository.save(chat);
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
		Map<String, Message> messages = new HashMap<String, Message>();
		for(Message m: chat.getMessages()) {
			User sender = userRepository.findById(m.getSenderId()).orElse(null);
			String name = sender.getFname() + " " + sender.getLname();
			messages.put(name, m);
		}
		System.out.println(messages);
		mav.addObject("messages", messages);
		mav.addObject("privateChatsNames", privateChatsNames);
		mav.addObject("privateChats", privateChats);
		mav.addObject("groupChats", groupChats);
		mav.setViewName("/chat");
		return mav;	
	}*/
	
}
