package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.UserTermRequest;
import com.backend.ecommercebackend.model.admin.term.UserTerm;


public interface UserTermService {
    public UserTerm addTerm(UserTermRequest request);
}
