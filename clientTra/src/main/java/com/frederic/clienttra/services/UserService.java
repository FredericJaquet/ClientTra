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

/**
 * Service class for user-related operations such as CRUD,
 * profile update, password change, and authentication-related queries.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves all users belonging to the current user's company,
     * sorted by username ignoring case.
     *
     * @return list of UserForAdminDTO representing users for admin view.
     */
    @Transactional(readOnly = true)
    public List<UserForAdminDTO> getAllUsers() {
        int idCompany = SecurityUtils.getCurrentUserCompanyId();

        return userRepository.findAllByCompany_IdCompany(idCompany)
                .stream()
                .map(userMapper::toAdminDTO)
                .sorted(Comparator.comparing(UserForAdminDTO::getUserName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves details of the currently authenticated user.
     *
     * @return UserSelfDTO with personal user details.
     * @throws UserNotFoundException if the user is not found in the database.
     */
    @Transactional(readOnly = true)
    public UserSelfDTO getCurrentUserDetails() {
        int idUser = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(idUser)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toSelfDTO(user);
    }

    /**
     * Retrieves the User entity of the currently authenticated user.
     *
     * @return User entity.
     * @throws UserNotFoundException if user not found.
     */
    @Transactional(readOnly = true)
    public User getCurrentUserEntity() {
        int idUser = SecurityUtils.getCurrentUserId();

        return userRepository.findById(idUser)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     * Retrieves a user by its ID if it belongs to the current user's company.
     *
     * @param id the user ID.
     * @return Optional containing UserForAdminDTO if found.
     * @throws UserNotFoundException if user not found or does not belong to company.
     */
    @Transactional(readOnly = true)
    public Optional<UserForAdminDTO> getUserById(Integer id) {
        int idCompany = SecurityUtils.getCurrentUserCompanyId();

        User user = userRepository.findByIdUserAndCompany_IdCompany(id, idCompany)
                .orElseThrow(UserNotFoundException::new);
        UserForAdminDTO dto = userMapper.toAdminDTO(user);

        return Optional.of(dto);
    }

    /**
     * Soft deletes (disables) a user by ID.
     * Prevents deleting users outside current company,
     * and prevents a single admin from deleting themselves.
     *
     * @param id the user ID to delete.
     * @throws UserNotFoundException if user not found.
     * @throws AccessDeniedException if user is not in current company.
     * @throws CantDeleteSelfException if the current user tries to delete themselves when they are the only admin.
     */
    @Transactional
    public void deleteUserById(int id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int ownerCompanyId = currentUser.getIdCompany();

        if (userToDelete.getCompany().getIdCompany() != ownerCompanyId) {
            throw new AccessDeniedException();
        } else {
            if (id == currentUser.getIdUser()) {
                int adminCount = userRepository.countByCompany_IdCompanyAndRole_RoleName(ownerCompanyId, "ADMIN");
                if (adminCount <= 1) {
                    throw new CantDeleteSelfException();
                }
            }
        }

        userToDelete.setEnabled(false);
        userRepository.save(userToDelete);
    }

    /**
     * Reactivates (enables) a user by ID.
     * Throws if user is not in current company.
     *
     * @param id the user ID to reactivate.
     * @throws UserNotFoundException if user not found.
     * @throws AccessDeniedException if user is not in current company.
     */
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

    /**
     * Creates a new user under the current company.
     *
     * @param dto the DTO containing user creation data.
     * @return UserForAdminDTO of the created user.
     * @throws UserNotAuthenticatedException if no current company found.
     */
    @Transactional
    public UserForAdminDTO createUser(CreateUserRequestDTO dto) {
        boolean exists=userRepository.existsByUserName(dto.getUsername());
        if(exists){
            throw new UserAlreadyExistsException();
        }
        Company currentCompany = companyService.getCurrentCompany()
                .orElseThrow(UserNotAuthenticatedException::new);

        User user = userMapper.toEntity(dto);
        user.setCompany(currentCompany);

        userRepository.save(user);

        return userMapper.toAdminDTO(user);
    }

    /**
     * Updates profile data of the currently authenticated user.
     *
     * @param dto DTO containing new values for the user's profile fields.
     *            Fields that are null will not be updated.
     * @throws UserNotFoundException if current user not found.
     */
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

    /**
     * Changes password for the currently authenticated user.
     * Validates the current password before updating.
     *
     * @param dto DTO containing current and new passwords.
     * @throws UserNotAuthenticatedException if current user not found.
     * @throws InvalidPasswordException if current password does not match.
     */
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
