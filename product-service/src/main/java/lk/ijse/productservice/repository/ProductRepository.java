package lk.ijse.productservice.repository;

import lk.ijse.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    @Query("""
            select p from Product p where
            (:query = '' or lower(p.name) like lower(concat('%', :query, '%'))
             or lower(coalesce(p.description, '')) like lower(concat('%', :query, '%')))
            and (:category = '' or lower(coalesce(p.category, '')) = lower(:category))
            and (:brand = '' or lower(coalesce(p.brand, '')) = lower(:brand))""")
    Page<Product> search(@Param("query") String query, @Param("category") String category,
                         @Param("brand") String brand, Pageable pageable);
    List<Product> findByCategory(String category);
    List<Product> findByBrand(String brand);

}
