package com.app.financemanager.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.financemanager.dao.ProfileDao;
import com.app.financemanager.model.Profile;


@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private ProfileDao profileDao;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Profile profile=profileDao.findByEmail(email);
		
		if(profile==null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		return new User(profile.getEmail(), profile.getPassword(),new ArrayList<>());
			
		
	}

}
