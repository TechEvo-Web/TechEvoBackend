package com.backend.ecommercebackend.controller;


import com.backend.ecommercebackend.dto.request.SelectProductRequest;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.service.FilterService;
import com.backend.ecommercebackend.service.SelectiveProductService;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Slf4j
public class ProductFeaturesController {

  private final FilterService filterService;
  private final SelectiveProductService selectiveProductService;
  private final Random random = new Random();


  @GetMapping("/filterByPriceAndSpecs")
  @Operation(summary = "Məhsulları filter etmək üçün endpoint",
          description = "Bu endpointə min,max,və xüsusi kateqoriyaya görə gələn spesifikasiya adlarını param ilə göndərərək bu filterlərə uyğun məhsulları ala bilərik.")
  public ResponseEntity<List<Product>> getFilteringProducts(@RequestParam(required = false) Float min,
                                                            @RequestParam(required = false) Float max,
                                                            @RequestParam(required = false) Map<String, String> filterSpec,
                                                            @RequestParam(required = false) String categoryName
  )
  {
    return ResponseEntity.ok(filterService.getFilteringProducts(min, max, filterSpec,categoryName));
  }

  @GetMapping("/filterCreatePc")
  @Operation(summary = "pc yarat hissəsi üçün endpoint")
  public ResponseEntity<Map<String,Map<String,Object>>>createPcFilter(@RequestParam Map<String,String> filter){
    return ResponseEntity.ok(filterService.createPcFilter(filter));
  }


  @PostMapping("/choosePC")
  @Operation(summary = "Hazir komputer secmek ucun endpoint. Kriteriyalara (harda/niye istifade/gorunus) gore")
  public ResponseEntity<?> selectComputerByCriteria(@RequestBody SelectProductRequest request) {
    final var selectedProductList= selectiveProductService.findSelectiveProduct(request);
    if (!selectedProductList.isEmpty()) {
      int randomIndex = random.nextInt(selectedProductList.size());
      Product selectedProduct = selectedProductList.get(randomIndex);
      return ResponseEntity.status(HttpStatus.CREATED).body(selectedProduct);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Uygun mehsul tapilmadi");
    }
  }
}

