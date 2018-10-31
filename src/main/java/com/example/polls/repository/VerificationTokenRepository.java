package com.example.polls.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.polls.model.User;
import com.example.polls.model.VerificationToken;

/**
 * @author <egadEldin.ext@orange.com> Essam Eldin
 *
 */
public interface VerificationTokenRepository 
extends JpaRepository<VerificationToken, Long> {

  VerificationToken findByToken(String token);

  VerificationToken findByUser(User user);
}