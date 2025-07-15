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

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PlanRepository planRepository;

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

    public User toEntity(CreateUserRequestDTO dto){
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
                .build();
    }

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

    public List<User> toEntities(List<CreateUserRequestDTO> dtos, Company company){
        return dtos.stream()
                .map(dto -> toEntity(dto, company))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
