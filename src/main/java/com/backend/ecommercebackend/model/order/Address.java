package com.backend.ecommercebackend.model.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Entity
@Table
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String street;
    String city;
    String building;
    String area;
    @OneToMany(mappedBy = "address",cascade = CascadeType.ALL)
    List<Order> order;
}
