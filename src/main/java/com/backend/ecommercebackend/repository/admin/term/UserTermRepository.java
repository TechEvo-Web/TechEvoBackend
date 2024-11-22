package com.backend.ecommercebackend.repository.admin.term;

import com.backend.ecommercebackend.model.admin.term.UserTerm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTermRepository extends JpaRepository<UserTerm, Integer> {
}
