package com.ptit.asset.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Date;

@Component
public class BootstrapTaskSystem {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${spring.schema-name}")
    private String schemaName;
    @Value("${spring.device-name}")
    private String deviceName;

    // -----------------------------------------------------------------------------------------------------------------
//    @PostConstruct
//    public void onStartupCreateDevice(){
//        logger.info("==> On starting up server (Creating Device) === Current === : {}", new Date());
//        File file = new File("E:\\MyBackup\\"+deviceName+".bak");
//        if (!file.exists()){
//            jdbcTemplate.execute("USE Master " +
//                    "EXEC Sp_addumpdevice 'disk' , '" +deviceName+ "','E:\\MyBackup\\"+deviceName+".bak'");
//        }
//        jdbcTemplate.execute("USE " +schemaName);
//    }
    // -----------------------------------------------------------------------------------------------------------------
//    @PostConstruct
//    public void onStartupCreateStoreProcedure(){
//        logger.info("==> On starting up server (Creating StoreProcedure) === Current === : {}", new Date());
//        jdbcTemplate.execute("USE " +schemaName);
//        String query =
//                "IF NOT EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND OBJECT_ID = OBJECT_ID('dbo.STATISTICAL_INVENTORY'))\n" +
//                        "   exec('\n" +
//                        "   CREATE PROC STATISTICAL_INVENTORY (@categoryId BIGINT, @year INT)\n" +
//                        "AS\n" +
//                        "BEGIN\n" +
//                        "\tSELECT CAST(E1.name AS VARCHAR) AS name,\n" +
//                        "\t\t   CAST(E1.credential_code AS VARCHAR) AS code,\n" +
//                        "\t\t   CASE WHEN P.id IS NOT NULL THEN CAST(P.name_specification AS NVARCHAR) ELSE (''No_Place'') END AS place,\n" +
//                        "\t\t   A_P.price AS price,\n" +
//                        "\t\t   E1.time_allocation_type AS timeAllocationType,\n" +
//                        "\t\t   E1.allocation_duration AS allocationDuration,\n" +
//                        "\t\t   E1.depreciation_rate AS depreciationRate,\n" +
//                        "\t\t   E1.time_start_depreciation AS timeStartDepreciation,\n" +
//                        "\t\t   E1.status AS materialStatus,\n" +
//                        "\t\t   CASE WHEN U.ID_MATERIAL IS NOT NULL THEN (''Checked'') ELSE (''Un_Checked'') END AS materialCheck\n" +
//                        "\tFROM\n" +
//                        "\t(\n" +
//                        "\t\tSELECT  dbo.product.id,\n" +
//                        "\t\t\t\tdbo.product.name,\n" +
//                        "\t\t\t\tdbo.material.credential_code,\n" +
//                        "\t\t\t\tdbo.product.time_allocation_type,\n" +
//                        "\t\t\t\tdbo.product.allocation_duration,\n" +
//                        "\t\t\t\tdbo.product.depreciation_rate,\n" +
//                        "\t\t\t\tdbo.material.id AS M_ID,\n" +
//                        "\t\t\t\tdbo.material.time_start_depreciation,\n" +
//                        "\t\t\t\tdbo.material.status,\n" +
//                        "\t\t\t\tdbo.material.current_place_id\n" +
//                        "\n" +
//                        "\t\t\t\tFROM dbo.material\n" +
//                        "\t\t\t\tJOIN dbo.product\n" +
//                        "\t\t\t\tON product.id = material.product_id\n" +
//                        "\t\t\t\tJOIN dbo.category\n" +
//                        "\t\t\t\tON category.id = product.category_id\n" +
//                        "\t\t\t\tWHERE category_id = @categoryId\n" +
//                        "\t) AS E1\n" +
//                        "\tLEFT JOIN dbo.additional_product AS A_P\n" +
//                        "\t\tON A_P.product_id = E1.id\n" +
//                        "\tLEFT JOIN dbo.place AS P\n" +
//                        "\t\tON P.id = E1.current_place_id\n" +
//                        "\tLEFT JOIN \n" +
//                        "\t(SELECT dbo.inventory_material.material_id AS ID_MATERIAL\n" +
//                        "\t\tFROM dbo.inventory_material\n" +
//                        "\t\tJOIN dbo.inventory ON inventory.id = inventory_material.inventory_id\n" +
//                        "\t\tWHERE YEAR(dbo.inventory.time) = @year\n" +
//                        "\t) AS U\n" +
//                        "\tON E1.M_ID = U.ID_MATERIAL\n" +
//                        "\n" +
//                        "END')";
//        jdbcTemplate.execute(query);
//    }




}
