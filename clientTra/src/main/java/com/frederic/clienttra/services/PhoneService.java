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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing Phone entities linked to companies.
 * Provides CRUD operations with ownership validation.
 */
@Service
@RequiredArgsConstructor
public class PhoneService {

    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;
    private final OwnerValidator ownerValidator;
    private final CompanyRepository companyRepository;
    private final DtoValidator dtoValidator;

    /**
     * Retrieves all phones for a given company.
     *
     * @param idCompany the company ID to fetch phones for.
     * @return list of PhoneDTOs belonging to the company.
     * @throws CompanyNotFoundException if the current user does not own the company.
     */
    @Transactional(readOnly = true)
    public List<PhoneDTO> getAllPhones(Integer idCompany){
        ownerValidator.checkOwner(idCompany);
        List<Phone> entities = phoneRepository.findByCompany_IdCompany(idCompany);
        return phoneMapper.toPhoneDTOList(entities);
    }

    /**
     * Retrieves a single phone by its ID and company ID.
     *
     * @param idCompany the owning company ID.
     * @param idPhone the phone ID.
     * @return PhoneDTO of the requested phone.
     * @throws PhoneNotFoundException if no phone matches the given IDs.
     * @throws CompanyNotFoundException if the current user does not own the company.
     */
    @Transactional(readOnly = true)
    public PhoneDTO getPhone(Integer idCompany, Integer idPhone){
        ownerValidator.checkOwner(idCompany);
        Phone entity = phoneRepository.findByIdPhoneAndCompany_idCompany(idPhone, idCompany)
                .orElseThrow(PhoneNotFoundException::new);
        return phoneMapper.toPhoneDTO(entity);
    }

    /**
     * Deletes a phone by its ID and company ID.
     *
     * @param idCompany the owning company ID.
     * @param idPhone the phone ID to delete.
     * @throws PhoneNotFoundException if the phone does not exist.
     * @throws CompanyNotFoundException if the current user does not own the company.
     */
    @Transactional
    public void deletePhone(Integer idCompany, Integer idPhone){
        ownerValidator.checkOwner(idCompany);
        Phone entity = phoneRepository.findByIdPhoneAndCompany_idCompany(idPhone, idCompany)
                .orElseThrow(PhoneNotFoundException::new);
        phoneRepository.delete(entity);
    }

    /**
     * Creates a new phone linked to the specified company.
     *
     * @param idCompany the company ID to link the phone.
     * @param dto the phone creation data transfer object.
     * @throws CompanyNotFoundException if the company does not exist.
     * @throws CompanyNotFoundException if the current user does not own the company.
     */
    @Transactional
    public void createPhone(Integer idCompany, CreatePhoneRequestDTO dto){
        ownerValidator.checkOwner(idCompany);
        Company company = companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);
        Phone entity = phoneMapper.toEntity(dto);
        entity.setCompany(company);
        phoneRepository.save(entity);
    }

    /**
     * Updates an existing phone with the given data.
     *
     * @param idCompany the owning company ID.
     * @param idPhone the phone ID to update.
     * @param dto the phone update data transfer object.
     * @return updated PhoneDTO.
     * @throws PhoneNotFoundException if the phone does not exist.
     * @throws CompanyNotFoundException if the current user does not own the company.
     */
    @Transactional
    public PhoneDTO updatePhone(Integer idCompany, Integer idPhone, UpdatePhoneRequestDTO dto){
        ownerValidator.checkOwner(idCompany);
        Phone entity = phoneRepository.findByIdPhoneAndCompany_idCompany(idPhone, idCompany)
                .orElseThrow(PhoneNotFoundException::new);
        CreatePhoneRequestDTO createDto = phoneMapper.toCreatePhoneRequestDTO(dto, entity);
        dtoValidator.validate(createDto);

        phoneMapper.updateEntity(entity, dto);
        Phone updated = phoneRepository.save(entity);

        return phoneMapper.toPhoneDTO(updated);
    }

}
