package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateContactPersonRequestDTO;
import com.frederic.clienttra.dto.read.ContactPersonDTO;
import com.frederic.clienttra.dto.update.UpdateContactPersonRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.ContactPerson;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.exceptions.ContactPersonNotFoundException;
import com.frederic.clienttra.mappers.ContactPersonMapper;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.repositories.ContactPersonRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ContactPersonServiceTest {

    @Mock
    private ContactPersonRepository contactPersonRepository;
    @Mock
    private ContactPersonMapper contactPersonMapper;
    @Mock
    private OwnerValidator ownerValidator;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private DtoValidator dtoValidator;
    @InjectMocks
    private ContactPersonService contactPersonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllContactPersons_shouldReturnContactPersonDOT(){
        int idCompany = 1;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        ContactPerson c1 = new ContactPerson();
        c1.setFirstname("Nombre1");
        ContactPerson c2 = new ContactPerson();
        c2.setFirstname("Nombre2");
        ContactPerson c3 = new ContactPerson();
        c3.setFirstname("Nombre1");
        List<ContactPerson> entities = Arrays.asList(c1,c2,c3);

        when(contactPersonRepository.findByCompany_IdCompany(idCompany)).thenReturn(entities);

        ContactPersonDTO dto1 = new ContactPersonDTO();
        dto1.setFirstname("Nombre1");
        ContactPersonDTO dto2 = new ContactPersonDTO();
        dto2.setFirstname("Nombre2");
        ContactPersonDTO dto3 = new ContactPersonDTO();
        dto3.setFirstname("Nombre3");
        List<ContactPersonDTO> dtos = Arrays.asList(dto1,dto2,dto3);

        when(contactPersonMapper.toContactPersonDTOList(entities)).thenReturn(dtos);

        List<ContactPersonDTO> result = contactPersonService.getAllContactPersons(idCompany);

        verify(ownerValidator).checkOwner(idCompany);
        verify(contactPersonRepository).findByCompany_IdCompany(idCompany);
        verify(contactPersonMapper).toContactPersonDTOList(entities);

        assertThat(result).hasSize(3);

        assertThat(result.get(0).getFirstname()).isEqualTo("Nombre1");
        assertThat(result.get(1).getFirstname()).isEqualTo("Nombre2");
        assertThat(result.get(2).getFirstname()).isEqualTo("Nombre3");
    }

    @Test
    void getContactPerson_shouldReturnContactPersonDTO_whenFound(){
        int idCompany = 1;
        int idContactPerson = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        ContactPerson entity = new ContactPerson();
        when(contactPersonRepository.findByIdContactPersonAndCompany_idCompany(idContactPerson, idCompany))
                .thenReturn(Optional.of(entity));

        ContactPersonDTO dto = new ContactPersonDTO();
        when(contactPersonMapper.toContactPersonDTO(entity)).thenReturn(dto);

        ContactPersonDTO result = contactPersonService.getContactPerson(idCompany, idContactPerson);

        verify(ownerValidator).checkOwner(idCompany);
        verify(contactPersonRepository).findByIdContactPersonAndCompany_idCompany(idContactPerson, idCompany);
        verify(contactPersonMapper).toContactPersonDTO(entity);

        assertThat(result).isSameAs(dto);
    }

    @Test
    void getContactPerson_shouldThrowContactPersonNotFoundException_whenNotFound(){
        int idCompany = 1;
        int idContactPerson = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(contactPersonRepository.findByIdContactPersonAndCompany_idCompany(idContactPerson,idCompany))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> contactPersonService.getContactPerson(idCompany, idContactPerson))
                .isInstanceOf(ContactPersonNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(contactPersonRepository).findByIdContactPersonAndCompany_idCompany(idContactPerson, idCompany);
        verify(contactPersonMapper, never()).toContactPersonDTO(any());
    }

    @Test
    void deleteContactPerson_shouldDelete(){
        int idCompany = 1;
        int idContactPerson = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);
        ContactPerson entity = new ContactPerson();
        when(contactPersonRepository.findByIdContactPersonAndCompany_idCompany(idContactPerson, idCompany))
                .thenReturn(Optional.of(entity));

        contactPersonService.deleteContactPerson(idCompany, idContactPerson);

        verify(ownerValidator).checkOwner(idCompany);
        verify(contactPersonRepository).findByIdContactPersonAndCompany_idCompany(idContactPerson, idCompany);
        verify(contactPersonRepository).delete(entity);
    }

    @Test
    void deleContactPerson_shouldThrowContactPersonNotFoundException_whenNotFound(){
        int idCompany = 1;
        int idContactPerson = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(contactPersonRepository.findByIdContactPersonAndCompany_idCompany(idContactPerson,idCompany))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> contactPersonService.deleteContactPerson(idCompany, idContactPerson))
                .isInstanceOf(ContactPersonNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(contactPersonRepository).findByIdContactPersonAndCompany_idCompany(idContactPerson, idCompany);
        verify(contactPersonRepository, never()).delete(any());
    }

    @Test
    void createContactPerson_shouldSaveContact_whenCompanyExists(){
        int idCompany = 1;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        Company company = new Company();
        when(companyRepository.findByIdCompany(idCompany)).thenReturn(Optional.of(company));

        CreateContactPersonRequestDTO dto = new CreateContactPersonRequestDTO();
        ContactPerson entity = new ContactPerson();
        when(contactPersonMapper.toEntity(dto)).thenReturn(entity);

        contactPersonService.createContactPerson(idCompany, dto);

        verify(ownerValidator).checkOwner(idCompany);
        verify(companyRepository).findByIdCompany(idCompany);
        verify(contactPersonMapper).toEntity(dto);
        assertThat(entity.getCompany()).isSameAs(company);
        verify(contactPersonRepository).save(entity);
    }

    @Test
    void createContactPerson_shouldThrowCompanyNotFoundException_whenCompanyNotFound(){
        int idCompany = 1;
        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(companyRepository.findByIdCompany(idCompany)).thenReturn(Optional.empty());

        CreateContactPersonRequestDTO dto = new CreateContactPersonRequestDTO();
        assertThatThrownBy(() -> contactPersonService.createContactPerson(idCompany, dto))
                .isInstanceOf(CompanyNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(companyRepository).findByIdCompany(idCompany);
        verify(contactPersonMapper, never()).toEntity(any());
        verify(contactPersonRepository, never()).save(any());
    }

    @Test
    void updateContactPerson_shouldUpdateAndReturnDTO_whenContactPersonFound(){
        int idCompany = 1;
        int idContactPerson = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        ContactPerson entity = new ContactPerson();
        when(contactPersonRepository.findByIdContactPersonAndCompany_idCompany(idContactPerson, idCompany))
                .thenReturn(Optional.of(entity));

        UpdateContactPersonRequestDTO updateDTO = new UpdateContactPersonRequestDTO();
        CreateContactPersonRequestDTO createDTO = new CreateContactPersonRequestDTO();

        when(contactPersonMapper.toCreateContactPersonRequestDTO(updateDTO, entity)).thenReturn(createDTO);

        doNothing().when(dtoValidator).validate(createDTO);
        doAnswer(invocation -> {
            return null;
        }).when(contactPersonMapper).updateEntity(entity, updateDTO);

        ContactPerson updatedEntity = new ContactPerson();
        when(contactPersonRepository.save(entity)).thenReturn(updatedEntity);

        ContactPersonDTO resultDTO = new ContactPersonDTO();
        when(contactPersonMapper.toContactPersonDTO(updatedEntity)).thenReturn(resultDTO);

        ContactPersonDTO result = contactPersonService.updateContactPerson(idCompany, idContactPerson, updateDTO);

        verify(ownerValidator).checkOwner(idCompany);
        verify(contactPersonRepository).findByIdContactPersonAndCompany_idCompany(idContactPerson, idCompany);
        verify(contactPersonMapper).toCreateContactPersonRequestDTO(updateDTO, entity);
        verify(dtoValidator).validate(createDTO);
        verify(contactPersonMapper).updateEntity(entity, updateDTO);
        verify(contactPersonRepository).save(entity);
        verify(contactPersonMapper).toContactPersonDTO(updatedEntity);

        assertThat(result).isSameAs(resultDTO);
    }

    @Test
    void updateContactPerson_shouldThrowContactPersonNotFoundException_whenNotFound(){
        int idCompany = 1;
        int idContactPerson = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        UpdateContactPersonRequestDTO dto = new UpdateContactPersonRequestDTO();
        when(contactPersonRepository.findByIdContactPersonAndCompany_idCompany(idContactPerson,idCompany))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> contactPersonService.updateContactPerson(idCompany, idContactPerson, dto))
                .isInstanceOf(ContactPersonNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(contactPersonRepository).findByIdContactPersonAndCompany_idCompany(idContactPerson, idCompany);
        verify(contactPersonMapper, never()).toCreateContactPersonRequestDTO(any(), any());
        verify(dtoValidator, never()).validate(any());
        verify(contactPersonMapper, never()).updateEntity(any(), any());
        verify(contactPersonRepository, never()).save(any());
        verify(contactPersonMapper, never()).toContactPersonDTO(any());
    }
}
