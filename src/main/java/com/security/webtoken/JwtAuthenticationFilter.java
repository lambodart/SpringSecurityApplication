package com.security.webtoken;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.security.config.SecurityConfiguration;
import com.security.model.MyUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private MyUserDetailService detailService;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		System.out.println("header :: "+header);
		if ( header == null ||  !header.startsWith("Bearer ") ) {
			filterChain.doFilter(request, response);
			return ;
		}
		String jwt = header.substring(7); //removing Bearer -> 7 length
		
		//extract un
		String userName = jwtService.extractUserName(jwt);
		//request is not already loggged in 
		//already authenticated
		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userByUsername = detailService.loadUserByUsername(userName);
			//check for expiraton
			if (userByUsername != null && jwtService.isTokenValid(jwt)) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, userByUsername.getPassword()
						,userByUsername.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
			
		}
		filterChain.doFilter(request, response);
	}

	
}
