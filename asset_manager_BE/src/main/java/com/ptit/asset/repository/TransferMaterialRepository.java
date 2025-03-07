package com.ptit.asset.repository;

import com.ptit.asset.domain.TransferMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferMaterialRepository extends JpaRepository <TransferMaterial,Long> {

    TransferMaterial findTopByMaterialIdOrderByIdDesc(Long materialId);
}
