package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.UserForAdminDTO;
import com.frederic.clienttra.dto.UserSelfDTO;
import com.frederic.clienttra.entities.User;
import com.frederic.clienttra.exceptions.UserErrorResponseException;
import com.frederic.clienttra.mappers.UserMapper;
import com.frederic.clienttra.repositories.UserRepository;
import com.frederic.clienttra.security.SecurityUtils;
import com.frederic.clienttra.utils.MessageResolver;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MessageResolver messageResolver;

    public UserService(UserRepository userRepository, UserMapper userMapper, MessageResolver messageResolver) {
        this.userRepository = userRepository;
        this.userMapper=userMapper;
        this.messageResolver=messageResolver;
    }

    public List<UserForAdminDTO> getAllUsers() {
        int idCompany = SecurityUtils.getCurrentUserCompanyId();

        return userRepository.findAllByCompany_IdCompany(idCompany)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public UserSelfDTO getCurrentUserDetails() {
        int idUser = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UserErrorResponseException(URLEncoder.encode(messageResolver.getMessage("error.user.not_found"), StandardCharsets.UTF_8)));
        return userMapper.toSelfDTO(user);
    }

    public Optional<UserForAdminDTO> getUserById(Integer id) {
        int idCompany = SecurityUtils.getCurrentUserCompanyId();
        return userRepository.findByIdUserAndCompany_IdCompany(id, idCompany)
                .map(userMapper::toDTO);
    }
}
