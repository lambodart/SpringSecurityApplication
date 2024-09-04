package com.security.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		// TODO Auto-generated method stub
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(isAuth -> isAuth.getAuthority().equals("ROLE_ADMIN"));
		if (isAdmin) 
			setDefaultTargetUrl("/admin/home");
		else
			setDefaultTargetUrl("/user/home");
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
