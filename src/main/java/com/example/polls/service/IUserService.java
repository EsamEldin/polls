package com.example.polls.service;

import com.example.polls.exception.EmailExistsException;
import com.example.polls.model.User;
import com.example.polls.model.VerificationToken;

/**
 * @author <egadEldin.ext@orange.com> Essam Eldin
 *
 */
public interface IUserService {
    
    User registerNewUserAccount(User accountDto) 
      throws EmailExistsException;
 
    User getUser(String verificationToken);
  
   
 
    void saveRegisteredUser(User user);
 
    void createVerificationToken(User user, String token);
 
    VerificationToken getVerificationToken(String VerificationToken);
}
