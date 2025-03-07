package com.ptit.asset.repository;

import com.ptit.asset.domain.LiquidateMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiquidateMaterialRepository extends JpaRepository <LiquidateMaterial,Long> {

    List<LiquidateMaterial> findByLiquidateId(Long id);

}
