package com.app.financemanager.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.app.financemanager.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long>{

	Profile findByActivationToken(String token);

	Profile findByEmail(String email);

}
