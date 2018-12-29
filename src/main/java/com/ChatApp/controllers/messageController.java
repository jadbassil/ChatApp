package com.ChatApp.controllers;

import java.util.Date;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import com.ChatApp.models.Chat;
import com.ChatApp.models.Message;
import com.ChatApp.models.User;
import com.ChatApp.repositories.ChatRepository;

@Controller
public class messageController {

	@Autowired
	private ChatRepository chatRepository;
	
	@PostMapping("/newMessage")
	public ModelAndView newMessage(@RequestParam String message, @RequestParam String senderId, @RequestParam String chatId, HttpSession session) {
		System.out.println("message: "+message);
		User user = (User) session.getAttribute("user");
		ModelAndView mav = new ModelAndView();
		if(user == null)
			return new ModelAndView("/auth/login");
		Integer cid = Integer.valueOf(chatId);
		Integer sid = Integer.valueOf(senderId);
		Chat chat = chatRepository.findById(cid).orElse(null);
		Date time = new Date();
		Message msg = new Message();
		msg.setMessage(message);
		msg.setTime(time);
		msg.setChat(chat);
		msg.setSenderId(sid);
		chat.getMessages().add(msg);
		chatRepository.save(chat);
		mav.addObject("chat", chat);
		mav.setView(new RedirectView("/chat?id="+cid, true));
		return mav;
	}
	
    @MessageMapping("/{id}")
    @SendTo("/group/{id}")
    public Message greeting(Message message, @DestinationVariable("id") String id) throws Exception {
    	Chat chat = chatRepository.findById(Integer.valueOf(id)).orElse(null);
        message.setChat(chat);
        chat.getMessages().add(message);
        chatRepository.save(chat);
		return message;
    }
	
}
