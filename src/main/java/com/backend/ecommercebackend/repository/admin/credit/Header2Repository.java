package com.backend.ecommercebackend.repository.admin.credit;

import com.backend.ecommercebackend.model.admin.credit.CreditCard;
import com.backend.ecommercebackend.model.admin.credit.Header2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Header2Repository extends JpaRepository<Header2,Integer> {
    Header2 findFirstByOrderByIdAsc();

}
