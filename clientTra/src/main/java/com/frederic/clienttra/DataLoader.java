package com.frederic.clienttra;


import com.frederic.clienttra.entities.User;
import com.frederic.clienttra.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        /*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "user2pass";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);*/
    }
}
