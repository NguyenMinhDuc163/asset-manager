package com.ptit.asset.repository;

import com.ptit.asset.domain.Material;
import com.ptit.asset.repository.data.Statistical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StatisticalRepository extends JpaRepository<Material,Long> {

    @Query(
            value = "EXEC STATISTICAL_INVENTORY :categoryId, :year",
            nativeQuery = true)
    @Transactional
    List<Statistical.Inventory> fetchDataStatisticalInventory(@Param("categoryId") Long categoryId,
                                                              @Param("year") int year);


    @Query(
        value = "SELECT DISTINCT CAST(full_name AS VARCHAR) AS member\n" +
                "FROM dbo.users\n" +
                "\tJOIN dbo.inventory_material\n" +
                "\tON inventory_material.user_id = users.id\n" +
                "\tJOIN dbo.inventory ON inventory.id = inventory_material.inventory_id\n" +
                "\tWHERE YEAR(dbo.inventory.time) = :year",
        nativeQuery = true
    )
    @Transactional
    List<String> getMember(@Param("year") int year);
}
