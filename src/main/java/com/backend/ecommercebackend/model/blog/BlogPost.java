package com.backend.ecommercebackend.model.blog;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blogPosts")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String subTitle;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    String categoryName;

    @Column(nullable = false)
    String author;

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    List<String> imageUrl;
}
