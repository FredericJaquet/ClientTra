package com.frederic.clienttra.security;

import com.frederic.clienttra.entities.User;
import com.frederic.clienttra.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository repo) {
        this.userRepository = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("error.user.not_found"));

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().getRoleName())
        );

        return new CustomUserDetails(
                user.getIdUser(),
                user.getUserName(),
                user.getPasswd(),
                authorities,
                user.getCompany().getIdCompany(),
                user.getPreferredLanguage()
        );
    }
}
