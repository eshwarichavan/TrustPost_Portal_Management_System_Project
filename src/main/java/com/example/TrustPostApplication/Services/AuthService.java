package com.example.TrustPostApplication.Services;

import com.example.TrustPostApplication.Models.DTOs.*;
import com.example.TrustPostApplication.Models.Entities.User;
import com.example.TrustPostApplication.Models.Enums.Roles;
import com.example.TrustPostApplication.Repositories.UserRepository;
import com.example.TrustPostApplication.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public SignUpResponseDTO signUp(SignUpRequestDTO request){
        //check if user email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email Already Exists");
        }

        //create and save a new user
        User user=new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Roles.USER);  //by default role

        User saved=userRepository.save(user);

        // Generate JWT Token using email and role
        List<String> roles = Collections.singletonList(saved.getRole().name());
        String token = jwtUtil.generateToken(saved.getEmail(), roles);

        return new SignUpResponseDTO(
                saved.getUserId(),
                saved.getEmail(),
                saved.getRole(),
                "Signed Up Successfully",
                token
        );

    }

    public SignInResponseDTO signIn(SignInRequestDTO request) {
        //Get the user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        //Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        //Generate token using user's email and role
        List<String> roles = Collections.singletonList(user.getRole().name());
        String token = jwtUtil.generateToken(user.getEmail(), roles);

        //Return a response
        return new SignInResponseDTO(
                user.getUserId(),
                user.getEmail(),
                user.getRole(),
                "Login Successful",
                token
        );
    }


    //gets the current user from jwtutil
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}
