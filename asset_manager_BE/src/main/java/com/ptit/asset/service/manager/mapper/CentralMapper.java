package com.ptit.asset.service.manager.mapper;

import com.ptit.asset.domain.*;
import com.ptit.asset.dto.request.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CentralMapper {

    /*************************** Campus Space ***************************/
    // to create
    Campus toCampus(CampusCreateRequestDTO dto);

    // to update
    default Campus toCampusUpdate(Campus campusUpdate,Campus campus){
        setForInstanceCampusUpdate(campusUpdate,campus);
        return campusUpdate;
    }

    @Mapping(target = "id", ignore = true) @Mapping(target = "createdAt", ignore = true)
    void setForInstanceCampusUpdate(@MappingTarget Campus campusUpdate, Campus campus);
    /*************************** Campus Space ***************************/


    /*************************** Organization Space ***************************/
    // to create
    Organization toOrganization(OrganizationCreateRequestDTO dto);

    // to update
    default Organization toOrganizationUpdate(Organization organizationUpdate, Organization organization){
        setForInstanceOrganizationUpdate(organizationUpdate,organization);
        return organizationUpdate;
    }

    @Mapping(target = "id", ignore = true) @Mapping(target = "createdAt", ignore = true)
    void setForInstanceOrganizationUpdate(@MappingTarget Organization organizationUpdate, Organization organization);
    /*************************** Organization Space ***************************/


    /*************************** CalculationUnit Space ***************************/
    // to create
    CalculationUnit toCalculationUnit(CalculationUnitCreateRequestDTO dto);

    // to update
    default CalculationUnit toCalculationUnitUpdate(CalculationUnit calculationUnitUpdate, CalculationUnit calculationUnit){
        setForInstanceCalculationUnitUpdate(calculationUnitUpdate,calculationUnit);
        return calculationUnitUpdate;
    }

    @Mapping(target = "id", ignore = true) @Mapping(target = "createdAt", ignore = true)
    void setForInstanceCalculationUnitUpdate(@MappingTarget CalculationUnit calculationUnitUpdate, CalculationUnit calculationUnit);
    /*************************** CalculationUnit Space ***************************/

    /*************************** Group Space ***************************/
    // to create
    Group toGroup(GroupCreateRequestDTO dto);

    // to update
    default Group toGroupUpdate(Group groupUpdate, Group group){
        serForInstanceGroupUpdate(groupUpdate,group);
        return groupUpdate;
    }

    @Mapping(target = "id", ignore = true) @Mapping(target = "createdAt", ignore = true)
    void serForInstanceGroupUpdate(@MappingTarget Group groupUpdate, Group group);
    /*************************** Group Space ***************************/

    /*************************** Categories Space ***************************/
    // to create
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "dto.description",target = "description")
    @Mapping(target = "createdAt", ignore = true)@Mapping(target = "updatedAt", ignore = true)
    Category toCategory(CategoryCreateRequestDTO dto, Group group);

    // to update
    @Mapping(target = "id",ignore = true) @Mapping(target = "createdAt", ignore = true)
    Category toCategoryUpdate(@MappingTarget Category categoryUpdate, CategoryUpdateRequestDTO dto);
    /*************************** Categories Space ***************************/


    /*************************** Department Space ***************************/
    // to create
    Department toDepartment(DepartmentCreateRequestDTO dto);

    // to update
    default Department toDepartmentUpdate(Department departmentUpdate, Department department){
        setForInstanceDepartmentUpdate(departmentUpdate,department);
        return departmentUpdate;
    }

    @Mapping(target = "id", ignore = true) @Mapping(target = "createdAt", ignore = true)
    void setForInstanceDepartmentUpdate(@MappingTarget Department departmentUpdate, Department department);
    /*************************** Department Space ***************************/


    /*************************** TypePlace Space ***************************/
    // to create
    TypePlace toTypePlace(TypePlaceCreateRequestDTO dto);

    // to update
    default TypePlace toTypePlaceUpdate(TypePlace typePlaceUpdate, TypePlace typePlace){
        setForInstanceTypePlaceUpdate(typePlaceUpdate,typePlace);
        return typePlaceUpdate;
    }

    @Mapping(target = "id", ignore = true) @Mapping(target = "createdAt", ignore = true)
    void setForInstanceTypePlaceUpdate(@MappingTarget TypePlace typePlaceUpdate, TypePlace typePlace);
    /*************************** TypePlace Space ***************************/


    /*************************** Product Space ***************************/
    // to create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "dto.name",target = "name") @Mapping(source = "dto.description",target = "description")
    Product toProduct(ProductCreateRequestDTO dto, Category category, CalculationUnit calculationUnit);

    // to update
    @Mapping(target = "id", ignore = true) @Mapping(target = "createdAt", ignore = true)
    Product toProductUpdate(@MappingTarget Product productUpdate, ProductUpdateRequestDTO dto);
    /*************************** Product Space ***************************/


    /*************************** Additional Space ***************************/
    // to create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true)
    Additional toAdditional(AdditionalCreateRequestDTO dto, User user, Organization organization);

    // to update
    @Mapping(target = "id", ignore = true) @Mapping(target = "createdAt", ignore = true)
    Additional toAdditionalUpdate(@MappingTarget Additional additionalUpdate, AdditionalUpdateRequestDTO dto);

    /*************************** Additional Space ***************************/



    /*************************** Liquidate Space ***************************/
    // to create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true)
    Liquidate toLiquidate(LiquidateCreateRequestDTO dto, User user);

    // to update
    @Mapping(target = "id", ignore = true) @Mapping(target = "createdAt", ignore = true)
    Liquidate toLiquidateUpdate(@MappingTarget Liquidate liquidateUpdate, LiquidateUpdateRequestDTO dto);
    /*************************** Liquidate Space ***************************/



    /*************************** Place Space ***************************/
    // to create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "dto.description",target = "description")
    Place toPlace(PlaceCreateRequestDTO dto, TypePlace typePlace, Campus campus, Department department);

    // to update
    @Mapping(target = "id", ignore = true) @Mapping(target = "createdAt", ignore = true)
    Place toPlaceUpdate(@MappingTarget Place placeUpdate, PlaceUpdateRequestDTO dto);
    /*************************** Place Space ***************************/



    /*************************** Inventory Space ***************************/
    // to create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true)
    Inventory toInventory(InventoryCreateRequestDTO dto);
    /*************************** Inventory Space ***************************/



    /*************************** AdditionalProduct Space ***************************/
    // to create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true)
    AdditionalProduct toAdditionalProduct(AdditionalProductCreateDTO dto, Additional additional, Product product);
    /*************************** AdditionalProduct Space ***************************/



    /*************************** Material Space ***************************/
    // to create
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "additional",target = "additional")
    @Mapping(source = "user",target = "user")
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true)
    Material toMaterial(MaterialCreateRequestDTO dto,Additional additional, Product product, Place currentPlace, User user);

    // to update
    @Mapping(target = "id", ignore = true) @Mapping(target = "createdAt", ignore = true)
    Material toMaterialUpdate(@MappingTarget Material materialUpdate, MaterialUpdateRequestDTO dto);
    /*************************** Material Space ***************************/


    /*************************** Liquidate Material Space ***************************/
    // to create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true)
    LiquidateMaterial toLiquidateMaterial(Liquidate liquidate, Material material);
    /*************************** Liquidate Material Space ***************************/


    /*************************** Transfer Material Space ***************************/
    // to create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "user",target = "user")
    TransferMaterial toTransferMaterial(TransferMaterialCreateRequestDTO dto, Place placeFrom, Place placeTarget, Material material, User user);
    /*************************** Transfer Material Space ***************************/


    /*************************** Inventory Material Space ***************************/
    // to create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "user",target = "user")
    InventoryMaterial toInventoryMaterial(Inventory inventory, Material material, User user);
    /*************************** Inventory Material Space ***************************/



    /*************************** User Space ***************************/

    // to update
    @Mapping(target = "id", ignore = true) @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "roles", ignore = true)@Mapping(target = "password", ignore = true)
    User toUserUpdate(@MappingTarget User userUpdate, UserUpdateRequestDTO dto);
    /*************************** User Space ***************************/

}
