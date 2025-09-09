package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.update.UpdateCompanyOwnerRequestDTO;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.exceptions.CompanyNotFoundForUserException;
import com.frederic.clienttra.mappers.CompanyMapper;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.security.CustomUserDetails;
import com.frederic.clienttra.testutils.SecurityTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class CompanyServiceTest {

    @Mock
    CompanyMapper companyMapper;
    @Mock
    CompanyRepository companyRepository;
    @InjectMocks
    private CompanyServiceImpl companyService;

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

    private Optional<Company> getCurrentCompany(int idCompany){
        Company company = Company.builder()
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
        return Optional.of(company);
    }

    @Test
    void getCurrentCompany_ShouldReturnCompany_WhenUserAuthenticated(){
        int idUser = 5;
        int idCompany = 999;

        // Creamos el usuario autenticado simulando un ADMIN de esa empresa
        CustomUserDetails currentUser = getCurrentUser(idUser, idCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(companyRepository.findById(idCompany)).thenReturn(getCurrentCompany(idCompany));

        Optional<Company> result = companyService.getCurrentCompany();
        assertThat(result.get().getIdCompany()).isEqualTo(idCompany);
    }

    @Test
    void getCurrentCompany_ShouldReturnEmpty_WhenUserNotAuthenticated(){

        SecurityContextHolder.clearContext();

        Optional<Company> result = companyService.getCurrentCompany();
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void getCurrentCompanyOrThrow_ShouldThrow_WhenNoCompany() {
        int idUser = 5;
        int idCompany = 999;

        CustomUserDetails currentUser = getCurrentUser(idUser, idCompany);
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(companyRepository.findById(idCompany)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.getCurrentCompanyOrThrow())
                .isInstanceOf(CompanyNotFoundForUserException.class);
    }

    @Test
    void getCompanyById_shouldReturnCompany_whenExists() {
        int idCompany = 10;

        when(companyRepository.findByIdCompany(idCompany)).thenReturn(getCurrentCompany(idCompany));

        Company result = companyService.getCompanyById(idCompany);

        assertThat(result.getIdCompany()).isEqualTo(idCompany);
    }

    @Test
    void getCompanyById_shouldThrow_whenNotFound() {
        int idCompany = 10;

        when(companyRepository.findByIdCompany(idCompany)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.getCompanyById(idCompany))
                .isInstanceOf(CompanyNotFoundException.class);
    }

    @Test
    void updateCompanyOwner_shouldUpdateAndSave() {
        UpdateCompanyOwnerRequestDTO dto = new UpdateCompanyOwnerRequestDTO();

        Company company = getCurrentCompany(1).get();

        when(companyRepository.findById(anyInt())).thenReturn(Optional.of(company));

        doNothing().when(companyMapper).updateEntity(company, dto);

        // Simula contexto seguridad para que use getCurrentCompanyOrThrow()
        CustomUserDetails currentUser = getCurrentUser(1, company.getIdCompany());
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        companyService.updateCompanyOwner(dto);

        verify(companyMapper).updateEntity(company, dto);
        verify(companyRepository).save(company);
    }

    @Test
    void uploadCompanyLogo_shouldSaveLogoPath_whenFileValid() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("logo.png");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("dummy content".getBytes()));

        Company company = getCurrentCompany(1).get();

        CustomUserDetails currentUser = getCurrentUser(1, company.getIdCompany());
        SecurityTestUtils.mockSecurityContextWithUser(currentUser);

        when(companyRepository.findById(company.getIdCompany())).thenReturn(Optional.of(company));

        companyService.uploadCompanyLogo(file);

        assertThat(company.getLogoPath()).contains("uploads/logos/logo_");
        verify(companyRepository).save(company);
    }

    @Test
    void uploadCompanyLogo_shouldThrowIllegalArgumentException_whenFileEmpty() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        assertThatThrownBy(() -> companyService.uploadCompanyLogo(file))
                .isInstanceOf(IllegalArgumentException.class);
    }





}
