package com.exolon.finance.finance_tracker.security;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	//always use final when you are using @RequiredArgsConstructor
	private final JwtService jwtService;
	private  final CustomUserDetailsService userDetailsService;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");

		if(authHeader == null || !authHeader.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = authHeader.substring(7);
		try {

			if(!jwtService.verifyToken(token)) {

				filterChain.doFilter(request, response);
				return;
			}

			String userId = jwtService.extractUserId(token);

		
			if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = userDetailsService.getUserByUserId(
						UUID.fromString(userId)
					);

				 UsernamePasswordAuthenticationToken authToken =
	                        new UsernamePasswordAuthenticationToken(
	                                userDetails,
	                                null,
	                                userDetails.getAuthorities()
	                        );
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			
		} catch (Exception e) {
			 logger.error("JWT filter error", e);
		}
		filterChain.doFilter(request, response);
	}

}
