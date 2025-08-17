package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.read.ContextDTO;
import com.frederic.clienttra.entities.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContextService {

    private final CompanyService companyService;

    @Transactional(readOnly = true)
    public ContextDTO getContext(){
        ContextDTO dto = new ContextDTO();
        Company owner = companyService.getCurrentCompanyOrThrow();

        if(owner.getComName().startsWith("Test Company for")){//TODO In the future, move this condition to the Entity (ideally with a new field in the DDBB
            dto.setIsDemo(true);
        }

        return dto;
    }

}
