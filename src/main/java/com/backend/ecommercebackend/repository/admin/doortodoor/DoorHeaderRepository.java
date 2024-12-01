package com.backend.ecommercebackend.repository.admin.doortodoor;

import com.backend.ecommercebackend.model.admin.credit.CreditCard;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoorHeaderRepository extends JpaRepository<DoorHeader,Integer> {
    DoorHeader findFirstByOrderByIdAsc();

}
