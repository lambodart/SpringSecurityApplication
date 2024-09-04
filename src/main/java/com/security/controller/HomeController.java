package com.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String indexPAge() {
		return "index";
	}
	
	
	@GetMapping("/home")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')") //it will work only if enableMethodSecurity
	public String HomePage() {
		return "home";
	}
	
	@GetMapping("/about")
	public String aboutPage() {
		return "about";
	}
}
