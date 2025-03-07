package com.ptit.asset.repository;

import com.ptit.asset.domain.InventoryMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryMaterialRepository extends JpaRepository <InventoryMaterial,Long> {

    InventoryMaterial findByInventoryIdAndMaterialId(Long inventoryId, Long userId);

    InventoryMaterial findByInventoryIdAndMaterialIdAndUserId(Long inventoryId, Long materialId, Long userId);

    List<InventoryMaterial> findAllByInventoryIdAndUserId(Long inventoryId, Long userId);

}
