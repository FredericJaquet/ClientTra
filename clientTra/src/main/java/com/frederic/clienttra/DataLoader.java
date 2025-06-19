package com.frederic.clienttra;


import com.frederic.clienttra.entities.User;
import com.frederic.clienttra.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
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

        // Buscar usuario por nombre
        Optional<User> result = userRepository.findByUserName("admin1");

        if (result.isPresent()) {
            System.out.println("✅ Usuario encontrado: " + result.get().getEmail());
        } else {
            System.out.println("❌ Usuario no encontrado.");
        }
    }
}
