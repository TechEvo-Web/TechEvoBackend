package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.DoorHeaderRequest;
import com.backend.ecommercebackend.dto.request.SupportHeaderRequest;
import com.backend.ecommercebackend.dto.request.SupportRequest;
import com.backend.ecommercebackend.dto.request.SupportStepRequest;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorHeader;
import com.backend.ecommercebackend.model.admin.support.Support;
import com.backend.ecommercebackend.model.admin.support.SupportHeader;
import com.backend.ecommercebackend.model.admin.support.SupportStep;
import com.backend.ecommercebackend.repository.admin.support.SupportHeaderRepository;
import com.backend.ecommercebackend.repository.admin.support.SupportRepository;
import com.backend.ecommercebackend.repository.admin.support.SupportStepRepository;
import com.backend.ecommercebackend.service.SupportService;
 import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SupportServiceImpl implements SupportService {

    private final SupportRepository supportRepository;
    private final SupportStepRepository supportStepRepository;
    private final SupportHeaderRepository supportHeaderRepository;

    @Override
    public Support addSupport(SupportRequest supportRequest){
        Support support=new Support();
        support.setServiceName(supportRequest.getServiceName());
        support.setServiceComponents(supportRequest.getServiceComponents());
        return  supportRepository.save(support);
    }
    @Override
    public SupportStep  addSupportStep(SupportStepRequest supportStepRequest){

        SupportStep supportStep=new SupportStep();
        supportStep.setStepName(supportStepRequest.getStepName());
        supportStep.setStepDescription(supportStepRequest.getStepDescription());
        supportStep.setStepOrder(supportStepRequest.getStepOrder());

        return  supportStepRepository.save(supportStep);
    }
    @Override
    public Support updateSupport(int id, SupportRequest supportRequest) {
        Support support = supportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Support not found with id " + id));
        support.setServiceName(supportRequest.getServiceName());
        support.setServiceComponents(supportRequest.getServiceComponents());

        return supportRepository.save(support);
    }
    @Override
    public SupportStep updateSupportStep(int id, SupportStepRequest supportStepRequest) {
        SupportStep supportStep = supportStepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SupportStep not found with id " + id));
        supportStep.setStepName(supportStepRequest.getStepName());
        supportStep.setStepDescription(supportStepRequest.getStepDescription());
        supportStep.setStepOrder(supportStepRequest.getStepOrder());
        return supportStepRepository.save(supportStep);
    }
    @Override
    public SupportHeader addHeader(SupportHeaderRequest supportHeaderRequest){
        SupportHeader supportHeader = new SupportHeader();
        supportHeader.setHeaderName(supportHeaderRequest.getHeaderName());
        supportHeader.setHeaderDescription(supportHeaderRequest.getHeaderDescription());
        return  supportHeaderRepository.save(supportHeader);

    }

}
