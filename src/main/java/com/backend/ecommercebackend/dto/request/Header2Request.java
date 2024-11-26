package com.backend.ecommercebackend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Header2Request {
    String headerName;
    String headerDescription;
}

