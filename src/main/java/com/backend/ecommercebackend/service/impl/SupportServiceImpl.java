package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.SupportRequest;
import com.backend.ecommercebackend.model.dynamic.Support;
 import com.backend.ecommercebackend.repository.dynamic.SupportRepository;
 import com.backend.ecommercebackend.service.SupportService;
 import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SupportServiceImpl implements SupportService {

    private final SupportRepository supportRepository;
    @Override
    public Support addSupport(SupportRequest supportRequest){
        Support support=new Support();
        support.setServiceName(supportRequest.getServiceName());
        support.setServiceComponents(supportRequest.getServiceComponents());
        return  supportRepository.save(support);
    }

    @Override
    public Support updateSupport(int id, SupportRequest supportRequest) {
        Support support = supportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Support not found with id " + id));
        support.setServiceName(supportRequest.getServiceName());
        support.setServiceComponents(supportRequest.getServiceComponents());
        return supportRepository.save(support);
    }
}
