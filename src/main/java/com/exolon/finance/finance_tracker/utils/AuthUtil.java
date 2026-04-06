package com.exolon.finance.finance_tracker.utils;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUtil {
	
	
	  public UUID getUserIdFromAuth(Authentication authentication) {
	    	if (authentication == null || !authentication.isAuthenticated()) {
	            throw new IllegalStateException("User is not authenticated"); 
	        }
	        
	        Object principal = authentication.getPrincipal();
	        
	        if (principal instanceof UserDetails userDetails) {
	            String userId = userDetails.getUsername();
	            return  UUID.fromString(userId);
	        }
	        
	        throw new IllegalStateException("Principal is not an instance of UserDetails");
	    }
}
