package com.backend.ecommercebackend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class SupportHeaderRequest {

    String headerName;
    String headerDescription;
}

