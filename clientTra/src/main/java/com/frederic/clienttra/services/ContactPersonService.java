package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateContactPersonRequestDTO;
import com.frederic.clienttra.dto.read.ContactPersonDTO;
import com.frederic.clienttra.dto.update.UpdateContactPersonRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.ContactPerson;
import com.frederic.clienttra.exceptions.ContactPersonNotFoundException;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.mappers.ContactPersonMapper;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.repositories.ContactPersonRepository;
import com.frederic.clienttra.validators.DtoValidator;
import com.frederic.clienttra.validators.OwnerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactPersonService {

    private final ContactPersonRepository contactPersonRepository;
    private final ContactPersonMapper contactPersonMapper;
    private final OwnerValidator ownerValidator;
    private final CompanyRepository companyRepository;
    private final DtoValidator dtoValidator;

    @Transactional(readOnly = true)
    public List<ContactPersonDTO> getAllContactPersons(Integer idCompany){
        ownerValidator.checkOwner(idCompany);

        List<ContactPerson> entities=contactPersonRepository.findByCompany_IdCompany(idCompany);

        return contactPersonMapper.toContactPersonDTOList(entities);
    }

    @Transactional(readOnly = true)
    public ContactPersonDTO getContactPerson(Integer idCompany, Integer idContactPerson){
        ownerValidator.checkOwner(idCompany);

        ContactPerson entity = contactPersonRepository.findByIdContactPersonAndCompany_idCompany(idContactPerson, idCompany)
                .orElseThrow(ContactPersonNotFoundException::new);
        return contactPersonMapper.toContactPersonDTO(entity);
    }

    @Transactional
    public void deleteContactPerson(Integer idCompany, Integer idContactPerson){
        ownerValidator.checkOwner(idCompany);

        ContactPerson entity = contactPersonRepository.findByIdContactPersonAndCompany_idCompany(idContactPerson, idCompany)
                .orElseThrow(ContactPersonNotFoundException::new);
        contactPersonRepository.delete(entity);
    }

    @Transactional
    public void createContactPerson(Integer idCompany, CreateContactPersonRequestDTO dto){
        ownerValidator.checkOwner(idCompany);

        Company company=companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);
        ContactPerson entity = contactPersonMapper.toEntity(dto);
        entity.setCompany(company);
        contactPersonRepository.save(entity);
    }

    @Transactional
    public ContactPersonDTO updateContactPerson(Integer idCompany, Integer idContactPerson, UpdateContactPersonRequestDTO dto){
        ownerValidator.checkOwner(idCompany);
        ContactPerson entity = contactPersonRepository.findByIdContactPersonAndCompany_idCompany(idContactPerson, idCompany)
                .orElseThrow(ContactPersonNotFoundException::new);
        CreateContactPersonRequestDTO createDto = contactPersonMapper.toCreateContactPersonRequestDTO(dto,entity);
        dtoValidator.validate(createDto);

        contactPersonMapper.updateEntity(entity, dto);
        ContactPerson updated = contactPersonRepository.save(entity);

        return contactPersonMapper.toContactPersonDTO(updated);
    }
}
