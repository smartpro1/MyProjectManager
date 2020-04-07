package com.promise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.promise.models.PasswordReset;
import com.promise.models.User;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long>{
	PasswordReset findByResetToken(String token);
}
