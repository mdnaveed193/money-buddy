package com.app.financemanager.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.financemanager.model.Profile;
import com.app.financemanager.repository.ProfileRepository;

@Repository
public class ProfileDao {

	@Autowired
	private ProfileRepository profileRepository;
	
	
	public Profile saveProfile(Profile profile) {
		return profileRepository.save(profile);
	}
	
	public Profile findByActivationToken(String token) {
		return profileRepository.findByActivationToken(token);
	}

	public Profile findByEmail(String email) {
		// TODO Auto-generated method stub
		return profileRepository.findByEmail(email);
		
	}
}
