package com.frederic.clienttra.services;


import com.frederic.clienttra.dto.create.CreatePhoneRequestDTO;
import com.frederic.clienttra.dto.read.PhoneDTO;
import com.frederic.clienttra.dto.update.UpdatePhoneRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Phone;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.exceptions.PhoneNotFoundException;
import com.frederic.clienttra.mappers.PhoneMapper;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.repositories.PhoneRepository;
import com.frederic.clienttra.validators.DtoValidator;
import com.frederic.clienttra.validators.OwnerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PhoneServiceTest {
    @Mock
    private PhoneRepository phoneRepository;
    @Mock
    private PhoneMapper phoneMapper;
    @Mock
    private OwnerValidator ownerValidator;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private DtoValidator dtoValidator;
    @InjectMocks
    private PhoneService phoneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- getAllPhones ---
    @Test
    void getAllPhones_shouldReturnPhoneDTO(){
        int idCompany = 1;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        Phone p1 = new Phone();
        p1.setPhoneNumber("123456789");
        Phone p2 = new Phone();
        p2.setPhoneNumber("987654321");
        Phone p3 = new Phone();
        p3.setPhoneNumber("789123456");
        List<Phone> entities = Arrays.asList(p1,p2,p3);

        when(phoneRepository.findByCompany_IdCompany(idCompany)).thenReturn(entities);

        PhoneDTO dto1 = new PhoneDTO();
        dto1.setPhoneNumber("123456789");
        PhoneDTO dto2 = new PhoneDTO();
        dto2.setPhoneNumber("987654321");
        PhoneDTO dto3 = new PhoneDTO();
        dto3.setPhoneNumber("789123456");
        List<PhoneDTO> dtos = Arrays.asList(dto1,dto2,dto3);

        when(phoneMapper.toPhoneDTOList(entities)).thenReturn(dtos);

        // El método retorna lista
        List<PhoneDTO> result = phoneService.getAllPhones(idCompany);

        verify(ownerValidator).checkOwner(idCompany);
        verify(phoneRepository).findByCompany_IdCompany(idCompany);
        verify(phoneMapper).toPhoneDTOList(entities);

        assertThat(result).hasSize(3);

        assertThat(result.get(0).getPhoneNumber()).isEqualTo("123456789");
        assertThat(result.get(1).getPhoneNumber()).isEqualTo("987654321");
        assertThat(result.get(2).getPhoneNumber()).isEqualTo("789123456");
    }

    // --- getPhone ---
    @Test
    void getPhone_shouldReturnPhoneDTO_whenFound(){
        int idCompany = 1;
        int idPhone = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        Phone entity = new Phone();
        when(phoneRepository.findByIdPhoneAndCompany_idCompany(idPhone, idCompany))
                .thenReturn(Optional.of(entity));

        PhoneDTO dto = new PhoneDTO();
        when(phoneMapper.toPhoneDTO(entity)).thenReturn(dto);

        PhoneDTO result = phoneService.getPhone(idCompany, idPhone);

        verify(ownerValidator).checkOwner(idCompany);
        verify(phoneRepository).findByIdPhoneAndCompany_idCompany(idPhone, idCompany);
        verify(phoneMapper).toPhoneDTO(entity);

        assertThat(result).isSameAs(dto);
    }

    @Test
    void getPhone_shouldThrowPhoneReturnPhoneNotFoundException_whenNotFound() {
        int idCompany = 1;
        int idPhone = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(phoneRepository.findByIdPhoneAndCompany_idCompany(idPhone, idCompany))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> phoneService.getPhone(idCompany, idPhone))
                .isInstanceOf(PhoneNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(phoneRepository).findByIdPhoneAndCompany_idCompany(idPhone, idCompany);
        verify(phoneMapper, never()).toPhoneDTO(any());

    }

    // --- deletePhone ---
    @Test
    void deletePhone_shouldDelete(){
        int idCompany = 1;
        int idPhone = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);
        Phone entity = new Phone();
        when(phoneRepository.findByIdPhoneAndCompany_idCompany(idPhone, idCompany))
                .thenReturn(Optional.of(entity));

        phoneService.deletePhone(idCompany, idPhone);

        verify(ownerValidator).checkOwner(idCompany);
        verify(phoneRepository).findByIdPhoneAndCompany_idCompany(idPhone, idCompany);
        verify(phoneRepository).delete(entity);
    }

    @Test
    void deletePhone_shouldThrowPhoneReturnPhoneNotFoundException_whenNotFound(){
        int idCompany = 1;
        int idPhone = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(phoneRepository.findByIdPhoneAndCompany_idCompany(idPhone, idCompany))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> phoneService.deletePhone(idCompany, idPhone))
                .isInstanceOf(PhoneNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(phoneRepository).findByIdPhoneAndCompany_idCompany(idPhone, idCompany);
        verify(phoneRepository, never()).delete(any());
    }

    @Test
    void createPhone_shouldSavePhone_whenCompanyExists(){
        int idCompany = 1;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        Company company = new Company();
        when(companyRepository.findByIdCompany(idCompany)).thenReturn(Optional.of(company));

        CreatePhoneRequestDTO dto = new CreatePhoneRequestDTO();

        Phone entity = new Phone();
        when(phoneMapper.toEntity(dto)).thenReturn(entity);

        phoneService.createPhone(idCompany, dto);

        verify(ownerValidator).checkOwner(idCompany);
        verify(companyRepository).findByIdCompany(idCompany);
        verify(phoneMapper).toEntity(dto);
        assertThat(entity.getCompany()).isSameAs(company);
        verify(phoneRepository).save(entity);
    }

    @Test
    void createPhone_shouldThrowCompanyNotFoundException_whenCompanyNotFound(){
        int idCompany = 1;
        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(companyRepository.findByIdCompany(idCompany)).thenReturn(Optional.empty());

        CreatePhoneRequestDTO dto = new CreatePhoneRequestDTO();
        assertThatThrownBy(() -> phoneService.createPhone(idCompany, dto))
                .isInstanceOf(CompanyNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(companyRepository).findByIdCompany(idCompany);
        verify(phoneMapper, never()).toEntity(any());
        verify(phoneRepository, never()).save(any());
    }

    // --- updatePhone ---
    @Test
    void updatePhone_sholdUpdateAndReturnDTO_whenPhoneFound(){
        int idCompany = 1;
        int idPhone = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);
        Phone entity = new Phone();
        when(phoneRepository.findByIdPhoneAndCompany_idCompany(idPhone, idCompany))
                .thenReturn(Optional.of(entity));

        UpdatePhoneRequestDTO updateDTO = new UpdatePhoneRequestDTO();
        CreatePhoneRequestDTO createDTO = new CreatePhoneRequestDTO();

        when(phoneMapper.toCreatePhoneRequestDTO(updateDTO, entity)).thenReturn(createDTO);

        doNothing().when(dtoValidator).validate(createDTO);
        doAnswer(invocation -> {
            return null;
        }).when(phoneMapper).updateEntity(entity, updateDTO);

        Phone updatedEntity = new Phone();
        when(phoneRepository.save(entity)).thenReturn(updatedEntity);

        PhoneDTO resultDTO = new PhoneDTO();
        when(phoneMapper.toPhoneDTO(updatedEntity)).thenReturn(resultDTO);

        PhoneDTO result =  phoneService.updatePhone(idCompany, idPhone, updateDTO);

        verify(ownerValidator).checkOwner(idCompany);
        verify(phoneRepository).findByIdPhoneAndCompany_idCompany(idPhone, idCompany);
        verify(phoneMapper).toCreatePhoneRequestDTO(updateDTO, entity);
        verify(dtoValidator).validate(createDTO);
        verify(phoneMapper).updateEntity(entity, updateDTO);
        verify(phoneRepository).save(entity);
        verify(phoneMapper).toPhoneDTO(updatedEntity);

        assertThat(result).isSameAs(resultDTO);
    }

    @Test
    void updatePhone_shouldThrowPhoneReturnPhoneNotFoundException_whenNotFound() {
        int idCompany = 1;
        int idPhone = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(phoneRepository.findByIdPhoneAndCompany_idCompany(idPhone, idCompany))
                .thenReturn(Optional.empty());

        UpdatePhoneRequestDTO updateDto = new UpdatePhoneRequestDTO();

        assertThatThrownBy(() -> phoneService.deletePhone(idCompany, idPhone))
                .isInstanceOf(PhoneNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(phoneRepository).findByIdPhoneAndCompany_idCompany(idPhone, idCompany);
        verify(phoneMapper, never()).toCreatePhoneRequestDTO(any(), any());
        verify(dtoValidator, never()).validate(any());
        verify(phoneMapper, never()).updateEntity(any(), any());
        verify(phoneRepository, never()).save(any());
        verify(phoneMapper, never()).toPhoneDTO(any());
    }

}
