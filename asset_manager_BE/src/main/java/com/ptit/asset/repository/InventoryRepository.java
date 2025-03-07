package com.ptit.asset.repository;

import com.ptit.asset.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository <Inventory,Long> {

    @Query(
        value =
            "SELECT TOP (1) * \n" +
            "FROM dbo.inventory \n" +
            "WHERE dbo.inventory.in_check = 1 \n" +
            "ORDER BY id DESC ",
    nativeQuery = true)
    Inventory findTop();

}
