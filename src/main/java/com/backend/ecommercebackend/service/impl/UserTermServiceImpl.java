package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.UserTermRequest;
import com.backend.ecommercebackend.model.admin.term.UserTerm;
import com.backend.ecommercebackend.repository.admin.term.UserTermRepository;
import com.backend.ecommercebackend.service.UserTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserTermServiceImpl implements UserTermService {

    private final UserTermRepository userTermRepository;
    @Override
    public UserTerm addTerm(UserTermRequest request){
        userTermRepository.deleteAll();
        UserTerm userTerm = new UserTerm();
        userTerm.setTerms(request.getTerms());

        return  userTermRepository.save(userTerm);
    }
}
