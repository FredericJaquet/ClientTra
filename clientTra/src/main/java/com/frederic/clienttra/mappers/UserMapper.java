package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.read.UserForAdminDTO;
import com.frederic.clienttra.dto.read.UserSelfDTO;
import com.frederic.clienttra.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserForAdminDTO toAdminDTO(User user) {
        return UserForAdminDTO.builder()
                .idUser(user.getIdUser())
                .userName(user.getUserName())
                .email(user.getEmail())
                .roleName(user.getRole().getRoleName())
                .planName(user.getPlan().getPlanName())
                .build();
    }

    public UserSelfDTO toSelfDTO(User user) {
        return UserSelfDTO.builder()
                .idUser(user.getIdUser())
                .userName(user.getUserName())
                .email(user.getEmail())
                .preferredLanguage(user.getPreferredLanguage())
                .preferredTheme(user.getPreferredTheme())
                .darkMode(user.isDarkMode())
                .roleName(user.getRole().getRoleName())
                .planName(user.getPlan().getPlanName())
                .build();
    }
}
