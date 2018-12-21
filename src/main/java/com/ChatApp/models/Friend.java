package com.ChatApp.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "friends", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "friendId"}))
public class Friend {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer friendshipId;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	@Column(nullable = false)
	private Integer friendId;
	
	@Column(nullable = false)
	private Integer status; //0:pending ; 1:accepted ; 2:declined

	public Friend(User user, Integer friendId, Integer status) {
		super();
		this.user = user;
		this.friendId = friendId;
		this.status = status;
	}

	public Friend() {
		super();
	}

	public Integer getFriendshipId() {
		return friendshipId;
	}

	public void setFriendshipId(Integer friendshipId) {
		this.friendshipId = friendshipId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getFriendId() {
		return friendId;
	}

	public void setFriendId(Integer friendId) {
		this.friendId = friendId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
}
