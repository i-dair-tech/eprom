package com.dqlick.eprom.web.rest;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dqlick.eprom.Shared.SharedObjectService;

@RestController
@RequestMapping("/api")
public class SharedController {

	private SharedObjectService sharedObject;

	public SharedObjectService getSharedObject() {
		return sharedObject;
	}

	
	
    public SharedController(SharedObjectService sharedObject) {
		super();
		this.sharedObject = sharedObject;
	}

    @PostMapping("/addCredentials")
    public void addCredentials(@RequestBody Map<String, String> credentials) {
    	    	
        String username = credentials.get("username");
        String password = credentials.get("password");
        sharedObject.setPassword(password);
        sharedObject.setUsername(username);
    }


//	@RequestMapping("/addCredentials")
//	public void addCredentials(String username, String password) {
//		System.out.println("user before"+username);
//		System.out.println("pwd before "+password);
//
//		sharedObject.setPassword(password);
//		sharedObject.setUsername(username);
//		System.out.println("user"+sharedObject.getUsername());
//		System.out.println("pwd"+sharedObject.getPassword());
//
//	}
}
