package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.RegistrationActualCompanyRequestDTO;
import com.frederic.clienttra.dto.create.RegistrationRequestDTO;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.exceptions.CompanyAlreadyExistsException;
import com.frederic.clienttra.mappers.AddressMapper;
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

/**
 * Service responsible for user registration and company creation.
 * Handles normal registrations and registration from demo company data.
 */
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AddressMapper addressMapper;
    private final CompanyService companyService;
    private final UserService userService;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PlanRepository planRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new company and its administrator user.
     *
     * <p>Steps performed:
     * <ul>
     *   <li>Checks if a company with the VAT number already exists (only root companies, no owner)</li>
     *   <li>Creates a new Company entity with addresses and default currency exchange rate</li>
     *   <li>Assigns the default FREEMIUM plan and ADMIN role to the new user</li>
     *   <li>Encrypts the admin password and saves the user linked to the new company</li>
     *   <li>Authenticates the new user automatically to log them in</li>
     * </ul>
     * </p>
     *
     * @param dto RegistrationRequestDTO containing company and user info.
     * @throws CompanyAlreadyExistsException if a company with the same VAT number already exists.
     * @throws RuntimeException if the ADMIN role or FREEMIUM plan are not found.
     */
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

        Address address = addressMapper.toEntity(dto.getAddress());

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

        // Log in the new user programmatically after registration
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getAdminUsername(), dto.getAdminPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Registers a new actual company from demo data, transferring the current user
     * to the new company, and deletes the demo company.
     *
     * <p>Steps performed:
     * <ul>
     *   <li>Checks if a company with the VAT number already exists (only root companies, no owner)</li>
     *   <li>Creates a new Company entity with addresses and default currency exchange rate</li>
     *   <li>Assigns the current logged-in user to the new company</li>
     *   <li>Deletes the demo company from the database</li>
     * </ul>
     * </p>
     *
     * @param dto RegistrationActualCompanyRequestDTO with new company info.
     * @throws CompanyAlreadyExistsException if a company with the same VAT number already exists.
     */
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

        Address address = addressMapper.toEntity(dto.getAddress());

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
