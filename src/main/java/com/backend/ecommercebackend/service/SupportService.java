package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.SupportRequest;
import com.backend.ecommercebackend.dto.request.SupportStepRequest;
import com.backend.ecommercebackend.model.admin.support.Support;
import com.backend.ecommercebackend.model.admin.support.SupportStep;

public interface SupportService {

    Support addSupport(SupportRequest supportRequest);
    Support updateSupport(int id,SupportRequest supportRequest);
    SupportStep addSupportStep(SupportStepRequest supportStepRequest);
    SupportStep updateSupportStep(int id,SupportStepRequest supportStepRequest);
}
