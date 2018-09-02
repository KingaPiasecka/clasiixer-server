package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.CurrentUser;
import com.example.demo.dto.UserDTO;
import com.example.demo.exceptions.EmailExistsException;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User createUser(UserDTO userDTO) throws EmailExistsException {
        if (emailExists(userDTO.getEmail())) {
            throw new EmailExistsException();
        }

        User newUser = new User();
        newUser.setEmail(userDTO.getEmail());
        newUser.setUserName(userDTO.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        newUser.setRole("USER");
        return userRepository.save(newUser);
    }

    private boolean emailExists(String email) {
        User byEmail = userRepository.findByEmail(email);
        return byEmail != null;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public CurrentUser getCurrentUser() {
        CurrentUser currentUser = new CurrentUser();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = null;
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        }
        String userName = userDetails.getUsername();
        currentUser.setEmail(userName);
        User byEmail = userRepository.findByEmail(userName);
        currentUser.setUsername(byEmail.getUsername());
        return currentUser;
    }
}
