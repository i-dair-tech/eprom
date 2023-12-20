package com.dqlick.eprom.Shared;

import org.springframework.stereotype.Service;

@Service
public class SharedObjectService {
    private String username ="test@gmail.com";
    private String password="test";
    
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


    
}