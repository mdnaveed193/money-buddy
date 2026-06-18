package com.app.financemanager.service;

import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.financemanager.dao.ProfileDao;
import com.app.financemanager.dto.LoginResponse;
import com.app.financemanager.model.Profile;
import com.app.financemanager.responseStructure.ResponseStructure;
import com.app.financemanager.security.JwtUtils;
import com.app.financemanager.utils.MailUtils;

@Service
public class ProfileService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ProfileDao profileDao;

	@Autowired
	private MailUtils mailUtils;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	public ResponseEntity<ResponseStructure<Profile>> registerProfile(Profile profile) {
		System.out.println("service is hit");
		String email = profile.getEmail();
		System.out.println(email + " email is this");

		String encodedEmail = Base64.getEncoder().encodeToString(email.getBytes());

		String token = encodedEmail + UUID.randomUUID().toString();

		profile.setActivationToken(token);

		profile.setPassword(passwordEncoder.encode(profile.getPassword()));

		Profile savedProfile = profileDao.saveProfile(profile);

		String activationLink = "http://localhost:8080/profile/activate?token=" + token;

		mailUtils.sendActivationMail(profile.getEmail(), activationLink);

		ResponseStructure<Profile> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("user registered successfully");
//		structure.setData(savedProfile);

		return new ResponseEntity<>(structure, HttpStatus.CREATED);

	}

	public ResponseEntity<ResponseStructure<String>> activateAccount(String token) {

		Profile profile = profileDao.findByActivationToken(token);

		ResponseStructure<String> structure = new ResponseStructure<>();

		if (profile == null) {

			structure.setStatusCode(HttpStatus.NOT_FOUND.value());

			structure.setMessage("Invalid Activation Token");

			structure.setData(null);

			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		profile.setActive(true);

		profile.setActivationToken(null);

		profileDao.saveProfile(profile);

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Account Activated Successfully");

		structure.setData("Welcome to Money Buddy");

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<LoginResponse>> loginUser(Profile profile) {
		ResponseStructure<LoginResponse> structure = new ResponseStructure<>();
		if (profile.getEmail() == "" || profile.getPassword() == "") {
			structure.setStatusCode(HttpStatus.BAD_REQUEST.value());
			structure.setMessage("Email and password is required");

			return new ResponseEntity<>(structure, HttpStatus.BAD_REQUEST);
		}

		Profile user = profileDao.findByEmail(profile.getEmail());
		if (user == null) {
			structure.setStatusCode(HttpStatus.NOT_FOUND.value());
			structure.setMessage("User Not Found");

			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		if (!user.isActive()) {
			structure.setStatusCode(HttpStatus.FORBIDDEN.value());
			structure.setMessage("Please Activate Your Account First");

			return new ResponseEntity<>(structure, HttpStatus.FORBIDDEN);
		}
		try {

			authenticationManager.authenticate(

					new UsernamePasswordAuthenticationToken(

							profile.getEmail(),

							profile.getPassword()));

		} catch (BadCredentialsException e) {

			structure.setStatusCode(HttpStatus.UNAUTHORIZED.value());

			structure.setMessage("Invalid Password");

			return new ResponseEntity<>(structure, HttpStatus.UNAUTHORIZED);
		}

		String token = jwtUtils.generateToken(profile.getEmail());

		LoginResponse response = new LoginResponse();

		response.setToken(token);

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Login Successful");

		structure.setData(response);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

}
