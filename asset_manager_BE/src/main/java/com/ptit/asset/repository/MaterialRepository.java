package com.ptit.asset.repository;

import com.ptit.asset.domain.Material;
import com.ptit.asset.domain.enumeration.MaterialStatus;
import com.ptit.asset.repository.data.Statistical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository <Material,Long> {

    Page<Material> findAll(Pageable pageable);

    @Query(value =
        "SELECT * FROM\n" +
            "dbo.material JOIN dbo.product\n" +
            "ON product.id = material.product_id\n" +
            "JOIN dbo.category ON category.id = product.category_id\n" +
            "WHERE category_id = :categoryId ",
        nativeQuery = true)
    List<Material> findAllByCategoryId(@Param("categoryId") Long categoryId);


    @Query(value =
        "SELECT *\n" +
            "FROM dbo.material\n" +
            "WHERE dbo.material.current_place_id IN\n" +
            "\t(\n" +
            "\tSELECT dbo.place.id FROM dbo.place JOIN dbo.department\n" +
            "\tON department.id = place.department_id\n" +
            "\tWHERE ((:departmentId = 0 OR :departmentId IS NULL) OR (department_id = :departmentId))\n" +
            "\tAND ((:placeId = 0 OR :placeId IS NULL) OR (dbo.place.id = :placeId))\n" +
            "\t) AND dbo.material.product_id IN (\n" +
            "\t\tSELECT product_id FROM\n" +
            "\t\tdbo.product WHERE ((:productId = 0 OR :productId IS NULL) OR (product_id = :productId))\n" +
            "\t\tAND ((:categoryId = 0 OR :categoryId IS NULL) OR (dbo.product.category_id = :categoryId))\n" +
            "\t)\n" +
            "\tAND ((:materialStatus = '' OR :materialStatus IS NULL) OR (dbo.material.status = :materialStatus))"
//            "\tAND dbo.material.status LIKE :materialStatus"
        , nativeQuery = true)
    List<Material> findByFilter(@Param("materialStatus")String materialStatus,
                                @Param("categoryId") Long categoryId,
                                @Param("placeId") Long placeId,
                                @Param("departmentId") Long departmentId,
                                @Param("productId") Long productId);

    @Query(value =
            "SELECT CASE WHEN P1.code IS NOT NULL THEN CAST(P1.code AS NVARCHAR) ELSE ('No_Place') END AS placeFrom," +
                    "CAST(P2.code AS VARCHAR) AS placeTarget," +
                    "CAST(dbo.transfer_material.reason AS NVARCHAR) AS reason," +
                    "CAST(dbo.transfer_material.time AS VARCHAR) AS timeAction," +
                    "CAST(dbo.users.full_name AS VARCHAR) AS PersonAction \n" +
                "FROM dbo.transfer_material\n" +
                "LEFT JOIN dbo.place AS P1 ON P1.id = transfer_material.place_from_id\n" +
                "LEFT JOIN dbo.place AS P2 ON P2.id = transfer_material.place_target_id\n" +
                "JOIN dbo.users ON users.id = transfer_material.user_id\n" +
                "WHERE dbo.transfer_material.material_id = :materialId\n" +
                "ORDER BY dbo.transfer_material.id DESC",
    nativeQuery = true)
    List<Statistical.HistoryTransfer> fetchHistoryTransfer(@Param("materialId") Long materialId);


    @Query
        (value = "SELECT \n" +
                "\tE1.price AS Price\n" +
                "\tFROM dbo.material\n" +
                "LEFT JOIN\n" +
                "(\n" +
                "\tSELECT dbo.additional.id AS ADD_ID,\n" +
                "\t\tdbo.additional_product.product_id AS PROD_ID,\n" +
                "\t\tdbo.additional_product.price\n" +
                "\t\tFROM dbo.additional_product\n" +
                "\tJOIN dbo.additional\n" +
                "\tON additional.id = additional_product.additional_id\n" +
                ") AS E1\n" +
                "ON dbo.material.additional_id = E1.ADD_ID\n" +
                "AND dbo.material.product_id = E1.PROD_ID\n" +
                "WHERE dbo.material.id = :materialId",
        nativeQuery = true)
    Float getPrice(@Param("materialId") Long id);


//    @Query
//        (value =
//            "SELECT \n" +
//            "\tE1.price AS Price\n" +
//            "\tdbo.material.time_start_depreciation AS timeStartDepreciation\n" +
//            "\tFROM dbo.material\n" +
//            "LEFT JOIN\n" +
//            "(\n" +
//            "\tSELECT dbo.additional.id AS ADD_ID,\n" +
//            "\t\tdbo.additional_product.product_id AS PROD_ID,\n" +
//            "\t\tdbo.additional_product.price\n" +
//            "\t\tFROM dbo.additional_product\n" +
//            "\tJOIN dbo.additional\n" +
//            "\tON additional.id = additional_product.additional_id\n" +
//            ") AS E1\n" +
//            "ON dbo.material.additional_id = E1.ADD_ID\n" +
//            "AND dbo.material.product_id = E1.PROD_ID\n" +
//            "WHERE dbo.material.id = :materialId",
//            nativeQuery = true)
//    ExtendedInfo getExtenedInfo(@Param("materialId") Long id);
//
//    public interface ExtendedInfo {
//        Float getPrice();
//        Instant getTimeStartDepreciation();
//    }

}
