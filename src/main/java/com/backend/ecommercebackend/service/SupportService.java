package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.SupportRequest;
import com.backend.ecommercebackend.model.dynamic.Support;

public interface SupportService {

    Support addSupport(SupportRequest supportRequest);
    Support updateSupport(int id,SupportRequest supportRequest);
}
