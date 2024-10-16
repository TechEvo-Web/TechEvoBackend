package com.backend.ecommercebackend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @Size(min = 5, max = 10, message = "your name size is not valid")
    String firstName;
    @Min(value = 5, message = "your surname size under the minimum")
    @Max(value = 16, message = "your surname size over the max")
    String lastName;
    String email;
}


