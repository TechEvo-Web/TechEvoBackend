package com.backend.ecommercebackend.dto.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
  private Long id;
  private String name;
  private String modelNumber;
  private String description;
  private double price;
  private double discountPrice;
  private float rating;
  private List<String> imageUrl;
  private String categoryName;
  private Map<String, String> specifications;
  private Boolean isFav;
}
