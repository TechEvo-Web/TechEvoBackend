package com.backend.ecommercebackend.repository.admin.support;

import com.backend.ecommercebackend.model.admin.support.SupportStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupportStepRepository extends JpaRepository<SupportStep, Integer> {
    List<SupportStep> findAllByOrderByStepOrderAsc();

    boolean existsByStepOrder(int stepOrder);

}
