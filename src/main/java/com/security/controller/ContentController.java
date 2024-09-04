package com.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

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
}
