package com.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.AbstractDaoAuthenticationConfigurer;

import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.security.model.MyUserDetailService;
import com.security.webtoken.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private MyUserDetailService userDetailService;
	
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	@Bean
	public UserDetailsService userDetailsService() {
		String password = "$2a$12$mcC3JGgL2xWtE9uuEw4aoOi3lYDCegUhR0UXKSQk540/j4itYJgRa";  //1234 bcrypt
		UserDetails USER = User.withUsername("user").password(password).roles("USER").build();
		UserDetails ADMIN = User.withUsername("admin").password(passwordEncoder().encode("1234")).roles("USER","ADMIN")
				//.roles("ADMIN")
				.build();
		
		UserDetails normalUser =  User.builder().username("user1").password(password).roles("USER"). build();
		UserDetails adminUser =  User.builder().username("admin1").password(password).roles("ADMIN"). build();
		return new InMemoryUserDetailsManager(USER, ADMIN,normalUser,adminUser);
	}
	*/
	
	@Bean
	public UserDetailsService userDetailsService() { 
		return userDetailService;
	}

	@Bean
	public AuthenticationProvider authenticationProvider () { 
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailService);
		authenticationProvider.setPasswordEncoder(passwordEncoder()); 
		return  authenticationProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager () {
		
		
		return new ProviderManager(authenticationProvider()); 
		
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		// httpSecurity.csrf().disable().authorizeHttpRequests().anyRequest().authenticated().and().formLogin();
		httpSecurity
			.csrf().disable()
			.authorizeHttpRequests(reg -> {
			reg.requestMatchers("/authenticate/**","/homepage","/register/**","/h2-console/**").permitAll(); 	// homepage should access to everyone
			reg.requestMatchers("/admin/**").hasRole("ADMIN");								// only admin can access
			reg.requestMatchers("/user/**").hasRole("USER");								// only user can access
			reg.anyRequest().authenticated();												// other than above any request come its authentication
		})
		//.formLogin(formlogin -> formlogin.permitAll());
		
		.formLogin( AbstractAuthenticationFilterConfigurer :: permitAll)
		.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
		;
		//.build();
			
		
		/*
		.formLogin(httpsecurityform -> {
			httpsecurityform.loginPage("/login")
			.successHandler(new AuthenticationSuccessHandler()) 
			.permitAll();
		});
		
		*/
		return httpSecurity.build();
	}
}
