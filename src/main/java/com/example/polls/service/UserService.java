package com.example.polls.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.polls.exception.EmailExistsException;
import com.example.polls.model.User;
import com.example.polls.model.VerificationToken;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.VerificationTokenRepository;

/**
 * @author <egadEldin.ext@orange.com> Essam Eldin
 *
 */
@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepository repository;
 
    @Autowired
    private VerificationTokenRepository tokenRepository;
 
    @Override
    public User registerNewUserAccount(User accountDto) 
      throws EmailExistsException {
         
        if (emailExist(accountDto)) {
            throw new EmailExistsException(
              "There is an account with that email adress: "
              + accountDto.getEmail());
        }

        return repository.save(accountDto);
    }
 
    private boolean emailExist(User user) {
        return  (repository.existsByEmail(user.getEmail())||repository.existsByUsername(user.getUsername()));

    }
     
    @Override
    public User getUser(String verificationToken) {
        User user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
    }
    
 
     
    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }
     
    @Override
    public void saveRegisteredUser(User user) {
        repository.save(user);
    }
     
    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }
    
}
