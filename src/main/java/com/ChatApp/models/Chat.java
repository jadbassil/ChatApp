package com.ChatApp.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "chats")
public class Chat {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer chatId;
	
	@Column(nullable = false)
	private Integer admin;
	
	@Column(nullable = false)
	private Integer type;//0: private ; 1: group
	
	@Column(nullable = true)
	private String name;
	
	@ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable(name="ChatPartipants", joinColumns = @JoinColumn(name="chat_id", referencedColumnName="chatId"), inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName="id"))
	private List<User> users = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "chat")
	@JsonManagedReference
	private Set<Message> messages = new HashSet<Message>();
	
	public Chat() {
		super();
	}

	public Chat(Integer id, Integer admin, Integer type, String name) {
		super();
		this.chatId = id;
		this.admin = admin;
		this.type = type;
		this.name = name;
	}

	public Integer getId() {
		return chatId;
	}

	public void setId(Integer id) {
		this.chatId = id;
	}

	public Integer getAdmin() {
		return admin;
	}

	public void setAdmin(Integer admin) {
		this.admin = admin;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Integer getChatId() {
		return chatId;
	}

	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	public Set<Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}	
	
}
