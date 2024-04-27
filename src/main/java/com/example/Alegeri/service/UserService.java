package com.example.Alegeri.service;

import com.example.Alegeri.model.MyUser;
import com.example.Alegeri.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String getUserByUsername(String username) {
        Optional<MyUser> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get().getUsername();
        }
        return "";
    }

    public MyUser getAuthenticatedUser(Authentication user) {
        String loggedUsername = user.getName();
        Optional<MyUser> currentUser = userRepository.findByUsername(loggedUsername);
        if (currentUser.isPresent()) {
            return currentUser.get();
        }
        return null;
    }

    public void updateUserDescription(Authentication authentication, String newDescription) {
        Optional<MyUser> currentUser = userRepository.findByUsername(authentication.getName());
        if (currentUser.isPresent()) {
            MyUser loggedUser = currentUser.get();
            loggedUser.setDescription(newDescription);
            userRepository.save(loggedUser);
        }
    }

    public void addCandidateRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(authentication.getAuthorities());
        updatedAuthorities.add(new SimpleGrantedAuthority("CANDIDATE"));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
