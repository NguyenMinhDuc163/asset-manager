package com.ptit.asset.repository;

import com.ptit.asset.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository <Product,Long> {

    Page<Product> findAll(Pageable pageable);

    List<Product> findAllByCategoryId(Long id);

}
