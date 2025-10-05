package com.example.TrustPostApplication.Config;

import com.example.TrustPostApplication.Models.Entities.User;
import com.example.TrustPostApplication.Models.Enums.Roles;
import com.example.TrustPostApplication.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String defaultAdminEmail = "admin@trustpost.com";

        if (userRepository.findByEmail(defaultAdminEmail).isEmpty()) {
            User superAdmin = new User();
            superAdmin.setEmail(defaultAdminEmail);
            superAdmin.setPassword(passwordEncoder.encode("admin123")); // set a strong password
            superAdmin.setRole(Roles.SUPERADMIN);
            superAdmin.setCreatedAt(LocalDateTime.now());

            userRepository.save(superAdmin);
            System.out.println("Default SUPER_ADMIN created: " + defaultAdminEmail);
        } else {
            System.out.println("SUPER_ADMIN already exists");
        }
    }
}
