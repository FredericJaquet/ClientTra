package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.create.CreateUserRequestDTO;
import com.frederic.clienttra.dto.read.UserForAdminDTO;
import com.frederic.clienttra.dto.read.UserSelfDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Plan;
import com.frederic.clienttra.entities.Role;
import com.frederic.clienttra.entities.User;
import com.frederic.clienttra.exceptions.RoleNotFoundException;
import com.frederic.clienttra.repositories.PlanRepository;
import com.frederic.clienttra.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between User entities and various User DTOs.
 * Also handles creation of User entities from DTOs with password encoding and role/plan lookups.
 */
@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PlanRepository planRepository;

    /**
     * Converts a User entity to a UserForAdminDTO.
     * Contains limited user info for admin views.
     *
     * @param user the User entity
     * @return corresponding UserForAdminDTO
     */
    public UserForAdminDTO toAdminDTO(User user) {
        return UserForAdminDTO.builder()
                .idUser(user.getIdUser())
                .userName(user.getUserName())
                .email(user.getEmail())
                .roleName(user.getRole().getRoleName())
                .planName(user.getPlan().getPlanName())
                .build();
    }

    /**
     * Converts a User entity to a UserSelfDTO.
     * Contains detailed info for the user viewing their own profile.
     *
     * @param user the User entity
     * @return corresponding UserSelfDTO
     */
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

    /**
     * Converts a CreateUserRequestDTO into a new User entity.
     * Password is encoded, and Role and Plan are fetched from repositories.
     *
     * @param dto CreateUserRequestDTO containing user data
     * @return new User entity (without company)
     * @throws RoleNotFoundException if Role or Plan is not found by ID
     */
    public User toEntity(CreateUserRequestDTO dto){
        Role role = roleRepository.findById(dto.getIdRole())
                .orElseThrow(RoleNotFoundException::new);
        Plan plan = planRepository.findById(dto.getIdPlan())
                .orElseThrow(RoleNotFoundException::new);

        return User.builder()
                .userName(dto.getUsername())
                .passwd(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .preferredLanguage((dto.getPreferredLanguage()))
                .role(role)
                .plan(plan)
                .enabled(true)
                .build();
    }

    /**
     * Converts a CreateUserRequestDTO into a new User entity linked to a Company.
     * Password is encoded, and Role and Plan are fetched from repositories.
     *
     * @param dto CreateUserRequestDTO containing user data
     * @param company Company entity to associate the user with
     * @return new User entity linked to the given company
     * @throws RoleNotFoundException if Role or Plan is not found by ID
     */
    public User toEntity(CreateUserRequestDTO dto, Company company){
        Role role = roleRepository.findById(dto.getIdRole())
                .orElseThrow(RoleNotFoundException::new);
        Plan plan = planRepository.findById(dto.getIdPlan())
                .orElseThrow(RoleNotFoundException::new);

        return User.builder()
                .userName(dto.getUsername())
                .passwd(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .role(role)
                .plan(plan)
                .enabled(true)
                .company(company)
                .build();
    }

    /**
     * Converts a list of CreateUserRequestDTOs into a list of User entities linked to a Company.
     *
     * @param dtos List of CreateUserRequestDTOs
     * @param company Company entity to associate the users with
     * @return List of new User entities linked to the given company
     */
    public List<User> toEntities(List<CreateUserRequestDTO> dtos, Company company){
        return dtos.stream()
                .map(dto -> toEntity(dto, company))
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
