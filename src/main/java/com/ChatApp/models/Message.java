package com.ChatApp.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "messages")
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer messageId;
	
	@Column(nullable = false)
	private Integer senderId;
	
	@Column(nullable = false)
	private String message;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	
	@ManyToOne
	@JoinColumn(name = "chatId")
	@JsonBackReference
	private Chat chat;
	
	public String getTimejs() {
		return timejs;
	}

	public void setTimejs(String timejs) {
		this.timejs = timejs;
	}

	@Transient
	private String SenderName;

	@Transient
	private String timejs;
	
	public Message() {
		super();
	}

	public Message(Integer senderId, String message, Date time, Chat chat) {
		super();
		this.senderId = senderId;
		this.message = message;
		this.time = time;
		this.chat = chat;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public Integer getSenderId() {
		return senderId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", senderId=" + senderId + ", message=" + message + ", time=" + time
				+ ", chat=" + chat + "]";
	}

	public String getSenderName() {
		return SenderName;
	}

	public void setSenderName(String senderName) {
		SenderName = senderName;
	}
	
	
}
