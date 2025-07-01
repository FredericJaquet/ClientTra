package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateUserRequestDTO;
import com.frederic.clienttra.dto.read.UserForAdminDTO;
import com.frederic.clienttra.dto.read.UserSelfDTO;
import com.frederic.clienttra.dto.update.UpdatePasswordRequestDTO;
import com.frederic.clienttra.dto.update.UpdateSelfRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Plan;
import com.frederic.clienttra.entities.Role;
import com.frederic.clienttra.entities.User;
import com.frederic.clienttra.exceptions.*;
import com.frederic.clienttra.mappers.UserMapper;
import com.frederic.clienttra.repositories.PlanRepository;
import com.frederic.clienttra.repositories.RoleRepository;
import com.frederic.clienttra.repositories.UserRepository;
import com.frederic.clienttra.security.CustomUserDetails;
import com.frederic.clienttra.security.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PlanRepository planRepository;
    private final CompanyOwnerService companyService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserForAdminDTO> getAllUsers() {
        int idCompany = SecurityUtils.getCurrentUserCompanyId();

        return userRepository.findAllByCompany_IdCompany(idCompany)
                .stream()
                .map(userMapper::toAdminDTO)
                .toList();
    }

    public UserSelfDTO getCurrentUserDetails() {
        int idUser = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(idUser)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toSelfDTO(user);
    }

    public Optional<UserForAdminDTO> getUserById(Integer id) {
        int idCompany = SecurityUtils.getCurrentUserCompanyId();

        return userRepository.findByIdUserAndCompany_IdCompany(id, idCompany)
                .map(userMapper::toAdminDTO);
    }

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

    public UserForAdminDTO createUser(CreateUserRequestDTO dto) {
        Company currentCompany = companyService.getCurrentCompany()
                .orElseThrow(UserNotAuthenticatedException::new);
        Role role = roleRepository.findById(dto.getIdRole())
                .orElseThrow(RoleNotFoundException::new);
        Plan plan = planRepository.findById(dto.getIdPlan())
                .orElseThrow(RoleNotFoundException::new);

        User user = new User();
        user.setUserName(dto.getUsername());
        user.setPasswd(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setCompany(currentCompany);
        user.setRole(role);
        user.setPlan(plan);

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
