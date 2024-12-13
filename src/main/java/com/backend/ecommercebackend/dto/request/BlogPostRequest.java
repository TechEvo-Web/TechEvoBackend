package com.backend.ecommercebackend.dto.request;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogPostRequest {
    String title;
    String subTitle;
    String description;
    String categoryName;
    String author;
    LocalDate publishedDate;
}
