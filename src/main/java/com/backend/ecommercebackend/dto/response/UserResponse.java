package com.backend.ecommercebackend.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String firstName;
    String lastName;
    String email;
    String profileImg;
    String cityName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<Long> favoriteProductIds;
}
