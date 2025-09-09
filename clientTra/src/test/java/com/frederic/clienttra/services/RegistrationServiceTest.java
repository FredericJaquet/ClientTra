package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.RegistrationActualCompanyRequestDTO;
import com.frederic.clienttra.dto.create.RegistrationRequestDTO;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.exceptions.CompanyAlreadyExistsException;
import com.frederic.clienttra.mappers.AddressMapper;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.repositories.PlanRepository;
import com.frederic.clienttra.repositories.RoleRepository;
import com.frederic.clienttra.repositories.UserRepository;
import com.frederic.clienttra.security.CustomUserDetails;
import com.frederic.clienttra.testutils.SecurityTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {

    @Mock
    private AddressMapper addressMapper;
    @Mock
    private CompanyService companyService;
    @Mock
    private UserService userService;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PlanRepository planRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private RegistrationService registrationService;

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
                "blue",
                "light"
        );
    }

    private Company getCurrentCompany(int idCompany){
        return Company.builder()
                .idCompany(idCompany)
                .vatNumber("vatNumber")
                .legalName("legalName")
                .comName("comName")
                .email("email")
                .web("web")
                .logoPath("logoPath")
                .addresses(new ArrayList<Address>())
                .phones(new ArrayList<Phone>())
                .contactPersons(new ArrayList<ContactPerson>())
                .bankAccounts(new ArrayList<BankAccount>())
                .changeRates(new ArrayList<ChangeRate>())
                .users(new ArrayList<User>())
                .customers(new ArrayList<Customer>())
                .providers(new ArrayList<Provider>())
                .build();
    }

    private RegistrationRequestDTO.RegistrationRequestDTOBuilder baseDemoCompanyDTO(){
        return RegistrationRequestDTO.builder()
                .vatNumber("ABC123")
                .comName("My Company")
                .legalName("My Company SL")
                .email("test@email.com")
                .web("www.test.com")
                .adminUsername("admin")
                .adminEmail("test@email.com")
                .adminPassword("password")
                .preferredLanguage("es")
                .preferredTheme("light");
    }

    private RegistrationActualCompanyRequestDTO.RegistrationActualCompanyRequestDTOBuilder baseActualCompanyDTO(){
        return RegistrationActualCompanyRequestDTO.builder()
                .vatNumber("ABC123")
                .comName("My Company")
                .legalName("My Company SL")
                .email("test@email.com")
                .web("www.test.com");
    }

    @Test
    void register_ShouldSaveNewCompanyAndAddressAndChangeRateAndNewAdmin_IfVatNumberIsUnique() {
        // Arrange
        RegistrationRequestDTO dto = baseDemoCompanyDTO().build();

        Address address = new Address();
        Role adminRole = new Role();
        Plan defaultPlan = new Plan();

        when(companyRepository.existsByVatNumberAndOwnerCompanyIsNull(dto.getVatNumber())).thenReturn(false);
        when(addressMapper.toEntity(dto.getAddress())).thenReturn(address);
        when(roleRepository.findByRoleName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
        when(planRepository.findByPlanName("FREEMIUM")).thenReturn(Optional.of(defaultPlan));
        when(passwordEncoder.encode(dto.getAdminPassword())).thenReturn("encodedPassword");

        ArgumentCaptor<Company> companyCaptor = ArgumentCaptor.forClass(Company.class);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(companyRepository.save(companyCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        registrationService.register(dto);

        // Assert
        Company capturedCompany = companyCaptor.getValue();

        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getUserName()).isEqualTo(dto.getAdminUsername());
        assertThat(capturedUser.getEmail()).isEqualTo(dto.getAdminEmail());
        assertThat(capturedUser.getPasswd()).isEqualTo("encodedPassword");
        assertThat(capturedUser.getRole()).isEqualTo(adminRole);

        assertThat(capturedCompany.getVatNumber()).isEqualTo(dto.getVatNumber());
        assertThat(capturedCompany.getAddresses()).hasSize(1);
        assertThat(capturedCompany.getChangeRates()).hasSize(1);

        ChangeRate cr = capturedCompany.getChangeRates().get(0);
        assertThat(cr.getCurrency1()).isEqualTo(cr.getCurrency2());
        assertThat(cr.getRate()).isEqualTo(1.0);
        assertThat(capturedCompany.getOwnerCompany()).isNull();

        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldThrowCompanyAlreadyExistsException_IfVatNumberAlreadyExists(){
        RegistrationRequestDTO dto = baseDemoCompanyDTO().build();

        when(companyRepository.existsByVatNumberAndOwnerCompanyIsNull(dto.getVatNumber())).thenReturn(true);

        assertThatThrownBy(() -> registrationService.register(dto))
                .isInstanceOf(CompanyAlreadyExistsException.class);

        verify(companyRepository).existsByVatNumberAndOwnerCompanyIsNull(dto.getVatNumber());
        verifyNoMoreInteractions(companyRepository, userRepository, addressMapper, roleRepository, planRepository);
    }

    @Test
    void registerFromDemo_ShouldSaveNewCompanyAndAddressAndChangeRateAndNewAdmin_IfVatNumberIsUnique(){
        int idUser = 1;
        int idCompany = 99;
        CustomUserDetails currentUser = getCurrentUser(idUser, idCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);
        User user = User.builder()
                .userName(currentUser.getUsername())
                .build();
        Company company=getCurrentCompany(idCompany);

        RegistrationActualCompanyRequestDTO dto=baseActualCompanyDTO().build();
        Address address = new Address();

        when(userService.getCurrentUserEntity()).thenReturn(user);
        when(companyService.getCurrentCompanyOrThrow()).thenReturn(company);
        when(companyRepository.existsByVatNumberAndOwnerCompanyIsNull(dto.getVatNumber())).thenReturn(false);
        when(addressMapper.toEntity(dto.getAddress())).thenReturn(address);

        ArgumentCaptor<Company> companyCaptor = ArgumentCaptor.forClass(Company.class);
        when(companyRepository.save(companyCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        registrationService.registerFromDemo(dto);

        Company capturedCompany = companyCaptor.getValue();
        User expectedUser = userService.getCurrentUserEntity();
        User capturedUser=capturedCompany.getUsers().get(0);
        assertThat(capturedUser).isSameAs(expectedUser);
        verify(userRepository).save(capturedUser);

        assertThat(capturedCompany.getVatNumber()).isEqualTo(dto.getVatNumber());
        assertThat(capturedCompany.getAddresses()).hasSize(1);
        assertThat(capturedCompany.getChangeRates()).hasSize(1);

        ChangeRate cr = capturedCompany.getChangeRates().get(0);
        assertThat(cr.getCurrency1()).isEqualTo(cr.getCurrency2());
        assertThat(cr.getRate()).isEqualTo(1.0);
        assertThat(capturedCompany.getOwnerCompany()).isNull();

        verify(companyRepository).delete(company);
    }

    @Test
    void registerFromDemo_ShouldThrowCompanyAlreadyExistsException_IfVatNumberAlreadyExists(){
        int idUser = 1;
        int idCompany = 99;
        CustomUserDetails currentUser = getCurrentUser(idUser, idCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        User user = User.builder()
                .userName(currentUser.getUsername())
                .build();
        Company demoCompany=getCurrentCompany(idCompany);
        RegistrationActualCompanyRequestDTO dto=baseActualCompanyDTO().build();

        when(userService.getCurrentUserEntity()).thenReturn(user);
        when(companyService.getCurrentCompanyOrThrow()).thenReturn(demoCompany);
        when(companyRepository.existsByVatNumberAndOwnerCompanyIsNull(dto.getVatNumber())).thenReturn(true);

        assertThatThrownBy(() -> registrationService.registerFromDemo(dto))
                .isInstanceOf(CompanyAlreadyExistsException.class);

        verify(companyRepository).existsByVatNumberAndOwnerCompanyIsNull(dto.getVatNumber());
        verifyNoMoreInteractions(companyRepository, userRepository, addressMapper, roleRepository, planRepository);
    }



}
