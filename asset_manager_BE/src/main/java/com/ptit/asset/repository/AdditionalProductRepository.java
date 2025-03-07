package com.ptit.asset.repository;

import com.ptit.asset.domain.AdditionalProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalProductRepository extends JpaRepository <AdditionalProduct,Long> {

}
