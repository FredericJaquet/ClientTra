package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreatePhoneRequestDTO;
import com.frederic.clienttra.dto.read.PhoneDTO;
import com.frederic.clienttra.dto.update.UpdatePhoneRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Phone;
import com.frederic.clienttra.exceptions.AddressNotFoundException;
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

@Service
@RequiredArgsConstructor
public class PhoneService {

    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;
    private final OwnerValidator ownerValidator;
    private final CompanyRepository companyRepository;
    private final DtoValidator dtoValidator;

    @Transactional(readOnly = true)
    public List<PhoneDTO> getAllPhones(Integer idCompany){
        ownerValidator.checkOwner(idCompany);

        List<Phone> entities=phoneRepository.findByCompany_IdCompany(idCompany);

        return phoneMapper.toPhoneDTOList(entities);
    }

    @Transactional(readOnly = true)
    public PhoneDTO getPhone(Integer idCompany, Integer idPhone){
        ownerValidator.checkOwner(idCompany);

        Phone entity = phoneRepository.findByIdPhoneAndCompany_idCompany(idPhone, idCompany)
                .orElseThrow(PhoneNotFoundException::new);
        return phoneMapper.toPhoneDTO(entity);
    }

    @Transactional
    public void deletePhone(Integer idCompany, Integer idPhone){
        ownerValidator.checkOwner(idCompany);

        Phone entity = phoneRepository.findByIdPhoneAndCompany_idCompany(idPhone, idCompany)
                .orElseThrow(AddressNotFoundException::new);
        phoneRepository.delete(entity);
    }

    @Transactional
    public void createPhone(Integer idCompany, CreatePhoneRequestDTO dto){
        ownerValidator.checkOwner(idCompany);

        Company company=companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);
        Phone entity = phoneMapper.toEntity(dto);
        entity.setCompany(company);
        phoneRepository.save(entity);
    }

    @Transactional
    public PhoneDTO updatePhone(Integer idCompany, Integer idPhone, UpdatePhoneRequestDTO dto){
        ownerValidator.checkOwner(idCompany);
        Phone entity = phoneRepository.findByIdPhoneAndCompany_idCompany(idPhone, idCompany)
                .orElseThrow(PhoneNotFoundException::new);
        CreatePhoneRequestDTO createDto = phoneMapper.toCreatePhoneRequestDTO(dto,entity);
        dtoValidator.validate(createDto);

        phoneMapper.updateEntity(entity, dto);
        Phone updated = phoneRepository.save(entity);

        return phoneMapper.toPhoneDTO(updated);
    }

}
