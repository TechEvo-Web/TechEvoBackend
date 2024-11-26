package com.backend.ecommercebackend.model.order;


import com.backend.ecommercebackend.dto.request.AddressRequest;
import com.backend.ecommercebackend.model.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Table(name = "orders")
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long orderId;
    int totalPrice;
    String deliveryType;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    List<OrderItem> orderItems;
    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @JoinColumn(name = "address_Id")
    Address address;
    String userEmail;
    Statuses orderStatus;
    int day;
    String month;
    int year;
}
