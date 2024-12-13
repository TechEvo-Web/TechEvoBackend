package com.backend.ecommercebackend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogPostResponse {
    private Long id;
    private String title;
    private String subTitle;
    private String description;
    private String categoryName;
    private String author;
    LocalDate publishedDate;
    private List<String> imageUrl;
}
