package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateUserRequestDTO;
import com.frederic.clienttra.dto.read.UserForAdminDTO;
import com.frederic.clienttra.dto.read.UserSelfDTO;
import com.frederic.clienttra.dto.update.UpdatePasswordRequestDTO;
import com.frederic.clienttra.dto.update.UpdateSelfRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.User;
import com.frederic.clienttra.exceptions.*;
import com.frederic.clienttra.mappers.UserMapper;
import com.frederic.clienttra.repositories.UserRepository;
import com.frederic.clienttra.security.CustomUserDetails;
import com.frederic.clienttra.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserForAdminDTO> getAllUsers() {
        int idCompany = SecurityUtils.getCurrentUserCompanyId();

        return userRepository.findAllByCompany_IdCompany(idCompany)
                .stream()
                .map(userMapper::toAdminDTO).sorted(Comparator.comparing(UserForAdminDTO::getUserName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserSelfDTO getCurrentUserDetails() {
        int idUser = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(idUser)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toSelfDTO(user);
    }

    @Transactional(readOnly = true)
    public User getCurrentUserEntity() {
        int idUser = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(idUser)
                .orElseThrow(UserNotFoundException::new);

        return user;
    }

    @Transactional(readOnly = true)
    public Optional<UserForAdminDTO> getUserById(Integer id) {
        int idCompany = SecurityUtils.getCurrentUserCompanyId();

        User user = userRepository.findByIdUserAndCompany_IdCompany(id, idCompany)
                .orElseThrow(UserNotFoundException::new);
        UserForAdminDTO dto = userMapper.toAdminDTO(user);

        return Optional.of(dto);
    }

    @Transactional
    public void deleteUserById(int id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int ownerCompanyId = currentUser.getIdCompany();

        if (userToDelete.getCompany().getIdCompany() != ownerCompanyId) {
            throw new AccessDeniedException();
        }else{
            if(id == currentUser.getIdUser()){
                int adminCount = userRepository.countByCompany_IdCompanyAndRole_RoleName(ownerCompanyId, "ADMIN");
                if (adminCount <= 1) {
                    throw new CantDeleteSelfException();
                }
            }
        }

        userToDelete.setEnabled(false);
        userRepository.save(userToDelete);
    }

    @Transactional
    public void reactivateUserById(int id){
        User userToReactivate = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int ownerCompanyId = currentUser.getIdCompany();

        if (userToReactivate.getCompany().getIdCompany() != ownerCompanyId) {
            throw new AccessDeniedException();
        }

        userToReactivate.setEnabled(true);
        userRepository.save(userToReactivate);
    }

    @Transactional
    public UserForAdminDTO createUser(CreateUserRequestDTO dto) {
        Company currentCompany = companyService.getCurrentCompany()
                .orElseThrow(UserNotAuthenticatedException::new);

        User user = userMapper.toEntity(dto);
        user.setCompany(currentCompany);

        userRepository.save(user);

        return userMapper.toAdminDTO(user);
    }

    @Transactional
    public void updateCurrentUser(UpdateSelfRequestDTO dto) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(currentUser.getIdUser())
                .orElseThrow(UserNotFoundException::new);

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPreferredLanguage() != null){
            user.setPreferredLanguage(dto.getPreferredLanguage());
        }
        if (dto.getPreferredTheme() != null){
            user.setPreferredTheme(dto.getPreferredTheme());
        }
        if (dto.getDarkMode() != null){
            user.setDarkMode(dto.getDarkMode());
        }

        userRepository.save(user);
    }

    @Transactional
    public void changePassword(UpdatePasswordRequestDTO dto) {
        int userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotAuthenticatedException::new);

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPasswd())) {
            throw new InvalidPasswordException();
        }

        user.setPasswd(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

}
