package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateUserRequestDTO;
import com.frederic.clienttra.dto.read.UserForAdminDTO;
import com.frederic.clienttra.dto.read.UserSelfDTO;
import com.frederic.clienttra.dto.update.UpdatePasswordRequestDTO;
import com.frederic.clienttra.dto.update.UpdateSelfRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.User;
import com.frederic.clienttra.exceptions.AccessDeniedException;
import com.frederic.clienttra.exceptions.CantDeleteSelfException;
import com.frederic.clienttra.exceptions.InvalidPasswordException;
import com.frederic.clienttra.exceptions.UserNotFoundException;
import com.frederic.clienttra.mappers.UserMapper;
import com.frederic.clienttra.repositories.RoleRepository;
import com.frederic.clienttra.repositories.UserRepository;
import com.frederic.clienttra.security.CustomUserDetails;
import com.frederic.clienttra.security.SecurityUtils;
import com.frederic.clienttra.testutils.SecurityTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CompanyService companyService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private CustomUserDetails getCurrentUser(int idUser, int idCompany) {
        return new CustomUserDetails(
                idUser,                      // idUser
                "user",                      // username
                "pass",                      // password
                true,                        // enabled
                List.of(),                   // authorities (vacía, suficiente para test)
                idCompany,                   // idCompany
                "es",                        // preferredLanguage
                "blue",                      //preferredTheme
                "light"                      //preferredMode
        );
    }

    private CreateUserRequestDTO getCreateDTO() {
        return new CreateUserRequestDTO(
                "user",     // username
                "pass",     // password
                "email",    //email
                "es",       //preferredLanguage
                1,          //idRole
                1           //idPlan
        );
    }

    @Test
    void getAllUsers_shouldReturnListSortedByName() {
        // given
        int idCompany = 1;
        int idCurrentUser = 2;

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserCompanyId).thenReturn(idCompany);

            mockedSecurityUtils.when(SecurityUtils::getCurrentUserCompanyId).thenReturn(idCompany);
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(idCurrentUser);

            User u1 = new User();
            u1.setUserName("Carlos");
            User u2 = new User();
            u2.setUserName("Ana");
            List<User> users = Arrays.asList(u1, u2);

            when(userRepository.findAllByCompany_IdCompanyAndIdUserNot(idCompany,idCurrentUser)).thenReturn(users);

            UserForAdminDTO dto1 = new UserForAdminDTO();
            dto1.setUserName("Carlos");
            dto1.setIdUser(3);
            UserForAdminDTO dto2 = new UserForAdminDTO();
            dto2.setUserName("Ana");
            dto2.setIdUser(4);

            when(userMapper.toAdminDTO(u1)).thenReturn(dto1);
            when(userMapper.toAdminDTO(u2)).thenReturn(dto2);

            // when
            List<UserForAdminDTO> result = userService.getAllUsers();

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getUserName()).isEqualTo("Ana");
            assertThat(result.get(1).getUserName()).isEqualTo("Carlos");

            verify(userRepository).findAllByCompany_IdCompanyAndIdUserNot(idCompany,idCurrentUser);
            verify(userMapper).toAdminDTO(u1);
            verify(userMapper).toAdminDTO(u2);
        }
    }

    @Test
    void getCurrentUserDetails_shouldReturnSelfDTO() {
        int idUser = 5;
        int idCompany = 999;

        // Creamos el usuario autenticado simulando un ADMIN de esa empresa
        CustomUserDetails currentUser = getCurrentUser(idUser, idCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        User user = new User();
        user.setIdUser(idUser);
        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));

        UserSelfDTO dto = new UserSelfDTO();
        dto.setIdUser(idUser);
        when(userMapper.toSelfDTO(user)).thenReturn(dto);

        UserSelfDTO result = userService.getCurrentUserDetails();
        assertThat(result.getIdUser()).isEqualTo(idUser);
    }

    @Test
    void getCurrentUserDetails_shouldThrow_whenUserNotFound() {
        int idUser = 99;
        int idCompany = 999;

        // Creamos el usuario autenticado simulando un ADMIN de esa empresa
        CustomUserDetails currentUser = getCurrentUser(idUser, idCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(userRepository.findById(idUser)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getCurrentUserDetails())
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getCurrentUserEntity_shouldReturnUser() {
        int idUser = 3;
        int idCompany = 999;

        // Creamos el usuario autenticado simulando un ADMIN de esa empresa
        CustomUserDetails currentUser = getCurrentUser(idUser, idCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        User user = new User();
        user.setIdUser(idUser);
        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));

        User result = userService.getCurrentUserEntity();
        assertThat(result).isSameAs(user);
    }

    @Test
    void getCurrentUserEntity_shouldThrow_whenUserNotFound() {
        int idUser = 88;
        int idCompany = 999;

        // Creamos el usuario autenticado simulando un ADMIN de esa empresa
        CustomUserDetails currentUser = getCurrentUser(idUser, idCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(userRepository.findById(idUser)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getCurrentUserEntity())
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getUserById_shouldReturnDTO_whenUserExistsAndBelongsToCompany() {
        int idCompany = 1;
        int idUser = 10;

        // Creamos el usuario autenticado simulando un ADMIN de esa empresa
        CustomUserDetails currentUser = new CustomUserDetails(
                idUser, "username", "pass", true, null, idCompany, "en", "blue", "light"
        );
        SecurityTestUtils.mockSecurityContextWithUser(currentUser); // 👈 esto basta

        // No hace falta mockear SecurityUtils.getCurrentUserCompanyId()
        // Porque ya lo saca de SecurityContextHolder

        User user = new User();
        user.setIdUser(idUser);
        Company company = new Company();
        company.setIdCompany(idCompany);
        user.setCompany(company);

        when(userRepository.findByIdUserAndCompany_IdCompany(idUser, idCompany))
                .thenReturn(Optional.of(user));

        UserForAdminDTO dto = new UserForAdminDTO();
        dto.setUserName("Juan");
        when(userMapper.toAdminDTO(user)).thenReturn(dto);

        Optional<UserForAdminDTO> result = userService.getUserById(idUser);

        assertThat(result).isPresent();
        assertThat(result.get().getUserName()).isEqualTo("Juan");
    }

    @Test
    void getUserById_shouldThrow_whenUserNotFound() {
        int idCompany = 1;
        int userId = 99;

        // Creamos el usuario autenticado simulando un ADMIN de esa empresa
        CustomUserDetails currentUser = getCurrentUser(1, idCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        UserService userService = new UserService(userRepository, companyService, userMapper, passwordEncoder, roleRepository);

        when(userRepository.findByIdUserAndCompany_IdCompany(userId, idCompany))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void deleteUserById_shouldDisableUser_whenValid() {
        int idUser = 5;
        int idCompany = 1;

        // Creamos el usuario a eliminar
        User user = new User();
        user.setIdUser(idUser);
        Company company = new Company();
        company.setIdCompany(idCompany);
        user.setCompany(company);

        // Creamos el usuario autenticado simulando un ADMIN de esa empresa
        CustomUserDetails currentUser = getCurrentUser(idUser, idCompany);

        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        when(userRepository.countByCompany_IdCompanyAndRole_RoleName(idCompany, "ADMIN")).thenReturn(2);

        userService.deleteUserById(idUser);

        assertThat(user.isEnabled()).isFalse();
        verify(userRepository).save(user);
    }

    @Test
    void deleteUserById_shouldThrow_whenUserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.deleteUserById(1))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void deleteUserById_shouldThrow_whenUserFromAnotherCompany() {
        int idUser = 5;
        int idCompany = 999;
        int idCompanyWrong=111;

        User user = new User();
        user.setIdUser(idUser);
        user.setCompany(new Company());
        user.getCompany().setIdCompany(idCompany); // Diferente

        // Creamos el usuario autenticado simulando un ADMIN de esa empresa
        CustomUserDetails currentUser = getCurrentUser(idUser, idCompanyWrong);

        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.deleteUserById(idUser))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void deleteUserById_shouldThrow_whenOnlyAdminDeletesSelf() {
        int idUser = 5;
        int idCompany = 1;

        User user = new User();
        user.setIdUser(idUser);
        user.setCompany(new Company());
        user.getCompany().setIdCompany(idCompany);

        // Creamos el usuario autenticado simulando un ADMIN de esa empresa
        CustomUserDetails currentUser = getCurrentUser(idUser, idCompany);

        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        when(userRepository.countByCompany_IdCompanyAndRole_RoleName(idCompany, "ADMIN")).thenReturn(1);

        assertThatThrownBy(() -> userService.deleteUserById(idUser))
                .isInstanceOf(CantDeleteSelfException.class);
    }

    @Test
    void deleteUserById_shouldDisableUser_whenDeletingAnotherUserOfSameCompany() {
        int idUserToDelete = 10;
        int idCompany = 1;
        int currentUserId = 20;

        User userToDelete = new User();
        userToDelete.setIdUser(idUserToDelete);
        Company company = new Company();
        company.setIdCompany(idCompany);
        userToDelete.setCompany(company);

        CustomUserDetails currentUser = getCurrentUser(currentUserId, idCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(userRepository.findById(idUserToDelete)).thenReturn(Optional.of(userToDelete));

        userService.deleteUserById(idUserToDelete);

        assertThat(userToDelete.isEnabled()).isFalse();
        verify(userRepository).save(userToDelete);
    }


    @Test
    void reactivateUserById_shouldEnableUser_whenAuthorized() {
        int idUser = 10;
        int idCompany = 1;

        User user = new User();
        user.setCompany(new Company());
        user.getCompany().setIdCompany(idCompany);
        user.setEnabled(false);

        // Creamos el usuario autenticado simulando un ADMIN de esa empresa
        CustomUserDetails currentUser = getCurrentUser(idUser, idCompany);

        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));

        userService.reactivateUserById(idUser);

        assertThat(user.isEnabled()).isTrue();
        verify(userRepository).save(user);
    }

    @Test
    void reactivateUserById_shouldThrow_whenUserFromAnotherCompany() {
        int idUser = 10;
        int idCompany = 999;
        int idCompanyWrong=111;

        User user = new User();
        user.setCompany(new Company());
        user.getCompany().setIdCompany(idCompany);

        // Creamos el usuario autenticado simulando un ADMIN de esa empresa
        CustomUserDetails currentUser = getCurrentUser(idUser, idCompanyWrong);

        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.reactivateUserById(idUser))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void createUser_shouldSaveUserAndReturnDTO() {
        // Arrange
        int idCompany = 1;
        Company company = new Company();
        company.setIdCompany(idCompany);

        CreateUserRequestDTO dto = getCreateDTO();

        User userEntity = new User();
        userEntity.setCompany(company);

        UserForAdminDTO expectedDto = new UserForAdminDTO();

        when(companyService.getCurrentCompany()).thenReturn(Optional.of(company));
        when(userMapper.toEntity(dto)).thenReturn(userEntity);
        when(userMapper.toAdminDTO(userEntity)).thenReturn(expectedDto);

        // Act
        UserForAdminDTO result = userService.createUser(dto);

        // Assert
        verify(companyService).getCurrentCompany();
        verify(userMapper).toEntity(dto);
        verify(userRepository).save(userEntity);
        verify(userMapper).toAdminDTO(userEntity);

        assertThat(result).isSameAs(expectedDto);
    }

    @Test
    void updateCurrentUser_shouldUpdateUserFields() {
        // Arrange
        int userId = 10;
        CustomUserDetails currentUser = mock(CustomUserDetails.class);
        when(currentUser.getIdUser()).thenReturn(userId);

        UpdateSelfRequestDTO dto = new UpdateSelfRequestDTO();
        dto.setEmail("newemail@example.com");
        dto.setPreferredLanguage("en");
        dto.setPreferredTheme("dark");
        dto.setDarkMode(true);

        User user = new User();
        user.setIdUser(userId);

        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userService.updateCurrentUser(dto);

        // Assert
        assertThat(user.getEmail()).isEqualTo("newemail@example.com");
        assertThat(user.getPreferredLanguage()).isEqualTo("en");
        assertThat(user.getPreferredTheme()).isEqualTo("dark");
        assertThat(user.isDarkMode()).isTrue();

        verify(userRepository).save(user);
    }

    @Test
    void changePassword_shouldUpdatePassword_whenCurrentPasswordMatches() {
        // Arrange
        int userId = 10;
        CustomUserDetails currentUser = mock(CustomUserDetails.class);
        when(currentUser.getIdUser()).thenReturn(userId);

        UpdatePasswordRequestDTO dto = new UpdatePasswordRequestDTO();
        dto.setCurrentPassword("oldPass");
        dto.setNewPassword("newPass");

        User user = new User();
        user.setIdUser(userId);
        user.setPasswd("encodedOldPass");

        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        // Act
        userService.changePassword(dto);

        // Assert
        assertThat(user.getPasswd()).isEqualTo("encodedNewPass");
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_shouldThrowInvalidPasswordException_whenCurrentPasswordDoesNotMatch() {
        // Arrange
        int userId = 10;
        CustomUserDetails currentUser = mock(CustomUserDetails.class);
        when(currentUser.getIdUser()).thenReturn(userId);

        UpdatePasswordRequestDTO dto = new UpdatePasswordRequestDTO();
        dto.setCurrentPassword("wrongOldPass");
        dto.setNewPassword("newPass");

        User user = new User();
        user.setIdUser(userId);
        user.setPasswd("encodedOldPass");

        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOldPass", "encodedOldPass")).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userService.changePassword(dto))
                .isInstanceOf(InvalidPasswordException.class);

        verify(userRepository, never()).save(any());
    }

}
