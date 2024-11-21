package com.backend.ecommercebackend.dto.request;

import jakarta.persistence.ElementCollection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupportRequest {
    String serviceName;
    @ElementCollection
    List<String> serviceComponents;
}
