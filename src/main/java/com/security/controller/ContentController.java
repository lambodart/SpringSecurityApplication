package com.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.model.LoginForm;
import com.security.model.MyUserDetailService;
import com.security.webtoken.JwtService;

//@Controller
@RestController
public class ContentController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService; 

	@Autowired
	private MyUserDetailService myUserDetailService;
	
	@GetMapping("/homepage")
	public String indexPAge() {
		return "homepage";
	}
	
	
	@GetMapping("/admin/home")
	public String HomePage() {
		return "home_admin";
	}
	
	@GetMapping("/user/home")
	public String aboutPage() {
		return "home_user";
	}
	
	@GetMapping("/login")
	public String login() {
		return "custom";
	}
	
	@PostMapping("/authenticate")
	public String getAuthenticate(@RequestBody LoginForm fLoginForm) {
		fLoginForm.username();
		fLoginForm.password();
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(fLoginForm.username(), fLoginForm.password())
				);
		if (authenticate.isAuthenticated()) {
			String token = jwtService.generateToken(myUserDetailService.loadUserByUsername(fLoginForm.username()));
			System.out.println("token : "+token);
			return token;
		}else {
			throw new UsernameNotFoundException("4XX : Username not found");
		}
		//return "";
	}
}
