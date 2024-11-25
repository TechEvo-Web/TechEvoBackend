package com.backend.ecommercebackend.repository.admin.credit;

import com.backend.ecommercebackend.model.admin.credit.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository <CreditCard, Integer> {
}
