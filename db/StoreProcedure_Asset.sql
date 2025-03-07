USE [springboot_asset_dev]

-- WORK OKAY
CREATE PROC STATISTICAL_INVENTORY(@categoryId BIGINT, @year INT)
AS
BEGIN
	SELECT CAST(E1.name AS VARCHAR) AS name,
		   CAST(E1.credential_code AS VARCHAR) AS code,
		   CASE WHEN P.id IS NOT NULL THEN CAST(P.name_specification AS NVARCHAR) ELSE ('No_Place') END AS place,
		   A_P.price AS price,
		   E1.time_allocation_type AS timeAllocationType,
		   E1.allocation_duration AS allocationDuration,
		   E1.depreciation_rate AS depreciationRate,
		   E1.time_start_depreciation AS timeStartDepreciation,
		   E1.status AS materialStatus,
		   CASE WHEN U.ID_MATERIAL IS NOT NULL THEN ('Checked') ELSE ('Un_Checked') END AS materialCheck
	FROM
	(
		SELECT  dbo.product.id,
				dbo.product.name,
				dbo.material.credential_code,
				dbo.product.time_allocation_type,
				dbo.product.allocation_duration,
				dbo.product.depreciation_rate,
				dbo.material.id AS M_ID,
				dbo.material.time_start_depreciation,
				dbo.material.status,
				dbo.material.current_place_id

				FROM dbo.material
				JOIN dbo.product
				ON product.id = material.product_id
				JOIN dbo.category
				ON category.id = product.category_id
				WHERE category_id = @categoryId
	) AS E1
	LEFT JOIN dbo.additional_product AS A_P
		ON A_P.product_id = E1.id
	LEFT JOIN dbo.place AS P
		ON P.id = E1.current_place_id
	LEFT JOIN 
	(SELECT dbo.inventory_material.material_id AS ID_MATERIAL
		FROM dbo.inventory_material
		JOIN dbo.inventory ON inventory.id = inventory_material.inventory_id
		WHERE YEAR(dbo.inventory.time) = @year
	) AS U
	ON E1.M_ID = U.ID_MATERIAL

END --------------------------------

----------------------------------
EXEC STATISTICAL_INVENTORY 5, 2020
----------------------------------








IF NOT EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND OBJECT_ID = OBJECT_ID('dbo.STATISTICAL_INVENTORY'))
   exec('
   CREATE PROC STATISTICAL_INVENTORY (@categoryId BIGINT, @year INT)
AS
BEGIN
	SELECT CAST(E1.name AS VARCHAR) AS name,
		   CAST(E1.credential_code AS VARCHAR) AS code,
		   CASE WHEN P.id IS NOT NULL THEN CAST(P.name_specification AS NVARCHAR) ELSE (''No_Place'') END AS place,
		   A_P.price AS price,
		   E1.time_allocation_type AS timeAllocationType,
		   E1.allocation_duration AS allocationDuration,
		   E1.depreciation_rate AS depreciationRate,
		   E1.time_start_depreciation AS timeStartDepreciation,
		   E1.status AS materialStatus,
		   CASE WHEN U.ID_MATERIAL IS NOT NULL THEN (''Checked'') ELSE (''Un_Checked'') END AS materialCheck
	FROM
	(
		SELECT  dbo.product.id,
				dbo.product.name,
				dbo.material.credential_code,
				dbo.product.time_allocation_type,
				dbo.product.allocation_duration,
				dbo.product.depreciation_rate,
				dbo.material.id AS M_ID,
				dbo.material.time_start_depreciation,
				dbo.material.status,
				dbo.material.current_place_id

				FROM dbo.material
				JOIN dbo.product
				ON product.id = material.product_id
				JOIN dbo.category
				ON category.id = product.category_id
				WHERE category_id = @categoryId
	) AS E1
	LEFT JOIN dbo.additional_product AS A_P
		ON A_P.product_id = E1.id
	LEFT JOIN dbo.place AS P
		ON P.id = E1.current_place_id
	LEFT JOIN 
	(SELECT dbo.inventory_material.material_id AS ID_MATERIAL
		FROM dbo.inventory_material
		JOIN dbo.inventory ON inventory.id = inventory_material.inventory_id
		WHERE YEAR(dbo.inventory.time) = @year
	) AS U
	ON E1.M_ID = U.ID_MATERIAL

END')






------------------------------------------------- version 2
CREATE PROC STATISTICAL_INVENTORY (@categoryId BIGINT)
AS
BEGIN
	SELECT CAST(dbo.product.name AS VARCHAR) AS name,
		   CAST(dbo.material.credential_code AS VARCHAR) AS code,
		   CASE WHEN dbo.place.id IS NOT NULL THEN CAST(dbo.place.name_specification AS NVARCHAR) ELSE ('No_Place') END AS place,

		   dbo.additional_product.price AS price,
		   dbo.product.time_allocation_type AS timeAllocationType,
		   dbo.product.allocation_duration AS allocationDuration,
		   dbo.product.depreciation_rate AS depreciationRate,
		   dbo.material.time_start_depreciation AS timeStartDepreciation,

		   dbo.material.status AS materialStatus,
		   dbo.inventory_material.material_id AS materialCheck

	FROM dbo.material
	JOIN dbo.product
	ON product.id = material.product_id
	JOIN dbo.category
	ON category.id = product.category_id
	
	LEFT JOIN dbo.additional_product
		ON additional_product.product_id = dbo.product.id
	LEFT JOIN dbo.place
		ON place.id = dbo.material.current_place_id
	LEFT JOIN dbo.inventory_material
		ON dbo.inventory_material.material_id = dbo.material.id
	WHERE category_id = @categoryId
END


EXEC STATISTICAL_INVENTORY 5


-------------------------------------------------------------------
SELECT
		CASE WHEN P1.code IS NOT NULL THEN CAST(P1.code AS NVARCHAR) ELSE ('No_Place') END AS placeFrom,
		CAST(P2.code AS VARCHAR) AS placeTarget,
		CAST(dbo.transfer_material.reason AS VARCHAR) AS reason,
		CAST(dbo.transfer_material.time AS VARCHAR) AS timeAction,
		CAST(dbo.users.full_name AS VARCHAR) AS Person
FROM dbo.transfer_material
LEFT JOIN dbo.place AS P1 ON P1.id = transfer_material.place_from_id
LEFT JOIN dbo.place AS P2 ON P2.id = transfer_material.place_target_id
JOIN dbo.users ON users.id = transfer_material.user_id
WHERE dbo.transfer_material.material_id = 1
ORDER BY dbo.transfer_material.id DESC
-------------------------------------------------------------------

SELECT DISTINCT CAST(full_name AS VARCHAR) AS member
FROM dbo.users
	JOIN dbo.inventory_material
	ON inventory_material.user_id = users.id
	JOIN dbo.inventory ON inventory.id = inventory_material.inventory_id
	WHERE YEAR(dbo.inventory.time) = 2020

------------------------------------------------------------------ FETCH PRICE OF MATERIAL

SELECT
	E1.price AS Price
	FROM dbo.material
LEFT JOIN
(
	SELECT dbo.additional.id AS ADD_ID,
		dbo.additional_product.product_id AS PROD_ID,
		dbo.additional_product.price
		FROM dbo.additional_product
	JOIN dbo.additional
	ON additional.id = additional_product.additional_id
) AS E1
ON dbo.material.additional_id = E1.ADD_ID
AND dbo.material.product_id = E1.PROD_ID
WHERE dbo.material.id = 6
------------------------------------------------------------------ FETCH MATERIAL BY PlACE