package com.frederic.clienttra.security;

import com.frederic.clienttra.entities.User;
import com.frederic.clienttra.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementing {@link UserDetailsService} to load user details
 * by username during the authentication process.
 * <p>
 * Retrieves the user from the database via {@link UserRepository} and constructs
 * a {@link CustomUserDetails} instance containing the user information and roles
 * for Spring Security.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads the user details by username.
     * <p>
     * Throws {@link UsernameNotFoundException} if the user is not found.
     *
     * @param username the username to search for
     * @return user details for Spring Security
     * @throws UsernameNotFoundException if no user with the given username exists
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("error.user.not_found"));

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().getRoleName())
        );

        String darkMode="light";

        if(user.isDarkMode()){
            darkMode="dark";
        }

        return new CustomUserDetails(
                user.getIdUser(),
                user.getUserName(),
                user.getPasswd(),
                user.isEnabled(),
                authorities,
                user.getCompany().getIdCompany(),
                user.getPreferredLanguage(),
                user.getPreferredTheme(),
                darkMode
        );
    }
}
