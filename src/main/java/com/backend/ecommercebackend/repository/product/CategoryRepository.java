package com.backend.ecommercebackend.repository.product;

import com.backend.ecommercebackend.model.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Query("""
select p.specificationName from Category c inner join ProductSpecification p on p.categoryId=c.categoryId where c.categoryId=:categoryId
""")
    List<String> findSpecificationNameByCategoryId(int categoryId);
}
