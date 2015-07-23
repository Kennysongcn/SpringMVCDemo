package com.maincarry.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class User {

	private String userName;
	private String password;
	private String email;
	private String nickName;
	
	public User() {
	}
	@NotEmpty(message="�û�������Ϊ��")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Size(min=1,max=10,message="����Ӧ����1-10λ��֮��")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Email(message="�����ʽ����ȷ")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public User(String userName, String password, String nickName, String email) {
		super();
		this.userName = userName;
		this.password = password;
		this.nickName = nickName;
		this.email = email;
	}
	
}
