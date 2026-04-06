package com.exolon.finance.finance_tracker.security;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.exolon.finance.finance_tracker.model.AppUser;
import com.exolon.finance.finance_tracker.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	private final UserRepository repository;

    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = repository.findByEmail(username)
								.orElseThrow(()-> new UsernameNotFoundException("User not found"));
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().toString()));
		
		return new User(
					user.getEmail(),
					user.getPassword(),
					authorities
				);
	}
    
	public UserDetails getUserByUserId(UUID userId) throws UsernameNotFoundException{
		AppUser user = repository.findById(userId)
				.orElseThrow(()-> new UsernameNotFoundException("User not found"));
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(
					user.getRole().toString()
				)
			);
		return new User(
				user.getId().toString(),
				user.getPassword(),
				authorities
			);
	}

}
