package com.app.financemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.financemanager.dto.LoginResponse;
import com.app.financemanager.model.Profile;
import com.app.financemanager.responseStructure.ResponseStructure;
import com.app.financemanager.service.ProfileService;

@RestController
public class ProfileController {
	
	@Autowired
	private ProfileService profileService;

	@PostMapping("/register")
	public ResponseEntity<ResponseStructure<Profile>> registerProfile(@RequestBody Profile profile){
		System.out.println("controoler is hit");
		return profileService.registerProfile(profile);
	}
	
	
	@GetMapping("/profile/activate")
	public ResponseEntity<ResponseStructure<String>> activateAccount(@RequestParam String token){

	    return profileService.activateAccount(token);
	}
	
	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<LoginResponse>> loginUser(@RequestBody Profile profile){
		return profileService.loginUser(profile);
	}
}
