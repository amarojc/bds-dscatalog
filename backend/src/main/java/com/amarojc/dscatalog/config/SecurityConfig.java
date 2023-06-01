package com.amarojc.dscatalog.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Autowired
	private Environment env;
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
			http.headers().frameOptions().disable();
		}

		http.csrf(csrf -> csrf.disable());
		
		return http.build();
	}
}