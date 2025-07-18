package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.RegistrationActualCompanyRequestDTO;
import com.frederic.clienttra.dto.create.RegistrationRequestDTO;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.exceptions.CompanyAlreadyExistsException;
import com.frederic.clienttra.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final CompanyService companyService;
    private final UserService userService;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PlanRepository planRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public void register(RegistrationRequestDTO dto) {

        boolean exists = companyRepository.existsByVatNumberAndOwnerCompanyIsNull(dto.getVatNumber());
        if (exists) {
            throw new CompanyAlreadyExistsException();
        }

        Company company = Company.builder()
                .vatNumber(dto.getVatNumber())
                .comName(dto.getComName())
                .legalName(dto.getLegalName())
                .email(dto.getEmail())
                .web(dto.getWeb())
                .ownerCompany(null)
                .build();

        Address address = Address.builder()
                .street(dto.getAddress().getStreet())
                .stNumber(dto.getAddress().getStNumber())
                .apt(dto.getAddress().getApt())
                .cp(dto.getAddress().getCp())
                .city(dto.getAddress().getCity())
                .state(dto.getAddress().getState())
                .country(dto.getAddress().getCountry())
                .company(company)
                .build();

        ChangeRate changeRate = ChangeRate.builder()
                    .currency1("€")
                    .currency2("€")
                    .rate(1.0)
                    .date(LocalDate.of(2025,1,1))
                    .build();

        company.addAddress(address);

        company.addChangeRate(changeRate);

        company = companyRepository.save(company);

        Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

        Plan defaultPlan = planRepository.findByPlanName("FREEMIUM")
                .orElseThrow(() -> new RuntimeException("No hay planes definidos"));

        User adminUser = User.builder()
                .userName(dto.getAdminUsername())
                .email(dto.getAdminEmail())
                .passwd(passwordEncoder.encode(dto.getAdminPassword()))
                .preferredLanguage(dto.getPreferredLanguage())
                .preferredTheme(dto.getPreferredTheme())
                .darkMode(dto.getDarkMode() != null ? dto.getDarkMode() : false)
                .enabled(true)
                .company(company)
                .role(adminRole)
                .plan(defaultPlan)
                .build();

        userRepository.save(adminUser);

        //Login the new user from backend after Registration
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getAdminUsername(), dto.getAdminPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void registerFromDemo(RegistrationActualCompanyRequestDTO dto) {
        User user = userService.getCurrentUserEntity();
        Company demoCompany = companyService.getCurrentCompanyOrThrow();

        boolean exists = companyRepository.existsByVatNumberAndOwnerCompanyIsNull(dto.getVatNumber());
        if (exists) {
            throw new CompanyAlreadyExistsException();
        }

        Company company = Company.builder()
                .vatNumber(dto.getVatNumber())
                .comName(dto.getComName())
                .legalName(dto.getLegalName())
                .email(dto.getEmail())
                .web(dto.getWeb())
                .ownerCompany(null)
                .build();

        Address address = Address.builder()
                .street(dto.getAddress().getStreet())
                .stNumber(dto.getAddress().getStNumber())
                .apt(dto.getAddress().getApt())
                .cp(dto.getAddress().getCp())
                .city(dto.getAddress().getCity())
                .state(dto.getAddress().getState())
                .country(dto.getAddress().getCountry())
                .company(company)
                .build();

        ChangeRate changeRate = ChangeRate.builder()
                .currency1("€")
                .currency2("€")
                .rate(1.0)
                .date(LocalDate.of(2025,1,1))
                .build();

        company.addAddress(address);

        company.addChangeRate(changeRate);

        company.addUser(user);

        companyRepository.save(company);

        userRepository.save(user);

        companyRepository.delete(demoCompany);
    }
}
