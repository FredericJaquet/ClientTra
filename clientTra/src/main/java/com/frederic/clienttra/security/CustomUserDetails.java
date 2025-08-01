package com.frederic.clienttra.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Implementation of Spring Security's {@link UserDetails} that holds custom user information.
 * <p>
 * Contains user identification, credentials, authorities, company association,
 * and user preferences such as preferred language.
 */
@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final int idUser;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;
    private final int idCompany;
    private final String preferredLanguage;

    /**
     * Indicates whether the user account has expired.
     * Always returns true since this implementation does not support account expiration.
     *
     * @return true, account is not expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user account is locked.
     * Always returns true since this implementation does not support account locking.
     *
     * @return true, account is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user credentials (password) have expired.
     * Always returns true since this implementation does not support credential expiration.
     *
     * @return true, credentials are not expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled.
     *
     * @return true if enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
