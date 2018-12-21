package com.ChatApp.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	@Size(min=1,message="Please enter your first name")
	private String fname;
	
	@Column(nullable = false)
	@Size(min=1,message="Please enter your last name")
	private String lname;
	
	@Column(unique = true, nullable = false)
	@Size(min=1,message="Invalid username")
	private String username;
	

	@Column(nullable = false)
	@Size(min=6, message="Password must at least contain 6 caracters")
	private String password;
	
	@Column(unique = true, nullable = false)
	@NotNull
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="Email address is invalid")
	private String email;
	
	@Column(nullable=false)
	@Size(min=1,max=6,message="Select a gender")
	private String gender;
	
	@NotNull
	@Pattern(regexp="\\d{8}",message="Invalid Phone number")
	private String phone;
	
	@Column(insertable = false, columnDefinition = "BIT default '0'")
	private Boolean verified;
	
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name="ChatPartipants", joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"), inverseJoinColumns = @JoinColumn(name="chat_id", referencedColumnName="chatId"))
	private List<Chat> chats = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Friend> friends = new ArrayList<>();
	
	public User() {
		super();
	}

	public User(@NotNull String fname, @NotNull String lname, String username, @NotNull String password,
			@NotNull String email) {
		super();
		this.fname = fname;
		this.lname = lname;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	public List<Friend> getFriends() {
		return friends;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", fname=" + fname + ", lname=" + lname + ", username=" + username + ", password="
				+ password + ", email=" + email + ", verified=" + verified + "]";
	}
	
	
	
}
