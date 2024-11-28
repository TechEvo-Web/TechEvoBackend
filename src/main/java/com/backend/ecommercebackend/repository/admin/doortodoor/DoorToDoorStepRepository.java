package com.backend.ecommercebackend.repository.admin.doortodoor;

import com.backend.ecommercebackend.model.admin.doortodoor.DoorToDoorStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoorToDoorStepRepository extends JpaRepository<DoorToDoorStep,Integer> {
    boolean existsByStepOrder(int stepOrder);

    Optional<Object> findByStepOrder(int stepOrder);
    boolean existsByStepOrderAndIdNot(int stepOrder, int id);

}
