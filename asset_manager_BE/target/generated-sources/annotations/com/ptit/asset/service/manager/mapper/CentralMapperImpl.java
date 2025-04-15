package com.ptit.asset.service.manager.mapper;

import com.ptit.asset.domain.Additional;
import com.ptit.asset.domain.AdditionalProduct;
import com.ptit.asset.domain.CalculationUnit;
import com.ptit.asset.domain.Campus;
import com.ptit.asset.domain.Category;
import com.ptit.asset.domain.Department;
import com.ptit.asset.domain.Group;
import com.ptit.asset.domain.Inventory;
import com.ptit.asset.domain.InventoryMaterial;
import com.ptit.asset.domain.Liquidate;
import com.ptit.asset.domain.LiquidateMaterial;
import com.ptit.asset.domain.Material;
import com.ptit.asset.domain.Organization;
import com.ptit.asset.domain.Place;
import com.ptit.asset.domain.Product;
import com.ptit.asset.domain.TransferMaterial;
import com.ptit.asset.domain.TypePlace;
import com.ptit.asset.domain.User;
import com.ptit.asset.dto.request.AdditionalCreateRequestDTO;
import com.ptit.asset.dto.request.AdditionalProductCreateDTO;
import com.ptit.asset.dto.request.AdditionalUpdateRequestDTO;
import com.ptit.asset.dto.request.CalculationUnitCreateRequestDTO;
import com.ptit.asset.dto.request.CampusCreateRequestDTO;
import com.ptit.asset.dto.request.CategoryCreateRequestDTO;
import com.ptit.asset.dto.request.CategoryUpdateRequestDTO;
import com.ptit.asset.dto.request.DepartmentCreateRequestDTO;
import com.ptit.asset.dto.request.GroupCreateRequestDTO;
import com.ptit.asset.dto.request.InventoryCreateRequestDTO;
import com.ptit.asset.dto.request.LiquidateCreateRequestDTO;
import com.ptit.asset.dto.request.LiquidateUpdateRequestDTO;
import com.ptit.asset.dto.request.MaterialCreateRequestDTO;
import com.ptit.asset.dto.request.MaterialUpdateRequestDTO;
import com.ptit.asset.dto.request.OrganizationCreateRequestDTO;
import com.ptit.asset.dto.request.PlaceCreateRequestDTO;
import com.ptit.asset.dto.request.PlaceUpdateRequestDTO;
import com.ptit.asset.dto.request.ProductCreateRequestDTO;
import com.ptit.asset.dto.request.ProductUpdateRequestDTO;
import com.ptit.asset.dto.request.TransferMaterialCreateRequestDTO;
import com.ptit.asset.dto.request.TypePlaceCreateRequestDTO;
import com.ptit.asset.dto.request.UserUpdateRequestDTO;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-04T14:57:32+0700",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_442 (Amazon.com Inc.)"
)
@Component
public class CentralMapperImpl implements CentralMapper {

    @Override
    public Campus toCampus(CampusCreateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Campus campus = new Campus();

        campus.setName( dto.getName() );
        campus.setDescription( dto.getDescription() );
        campus.setContactPhone( dto.getContactPhone() );
        campus.setContactEmail( dto.getContactEmail() );
        campus.setCampusType( dto.getCampusType() );
        campus.setLocation( dto.getLocation() );
        campus.setMapUrl( dto.getMapUrl() );

        return campus;
    }

    @Override
    public void setForInstanceCampusUpdate(Campus campusUpdate, Campus campus) {
        if ( campus == null ) {
            return;
        }

        campusUpdate.setUpdatedAt( campus.getUpdatedAt() );
        campusUpdate.setName( campus.getName() );
        campusUpdate.setDescription( campus.getDescription() );
        campusUpdate.setContactPhone( campus.getContactPhone() );
        campusUpdate.setContactEmail( campus.getContactEmail() );
        campusUpdate.setCampusType( campus.getCampusType() );
        campusUpdate.setLocation( campus.getLocation() );
        campusUpdate.setMapUrl( campus.getMapUrl() );
    }

    @Override
    public Organization toOrganization(OrganizationCreateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Organization organization = new Organization();

        organization.setName( dto.getName() );
        organization.setContact( dto.getContact() );

        return organization;
    }

    @Override
    public void setForInstanceOrganizationUpdate(Organization organizationUpdate, Organization organization) {
        if ( organization == null ) {
            return;
        }

        organizationUpdate.setUpdatedAt( organization.getUpdatedAt() );
        organizationUpdate.setName( organization.getName() );
        organizationUpdate.setContact( organization.getContact() );
    }

    @Override
    public CalculationUnit toCalculationUnit(CalculationUnitCreateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CalculationUnit calculationUnit = new CalculationUnit();

        calculationUnit.setName( dto.getName() );
        calculationUnit.setDescription( dto.getDescription() );

        return calculationUnit;
    }

    @Override
    public void setForInstanceCalculationUnitUpdate(CalculationUnit calculationUnitUpdate, CalculationUnit calculationUnit) {
        if ( calculationUnit == null ) {
            return;
        }

        calculationUnitUpdate.setUpdatedAt( calculationUnit.getUpdatedAt() );
        calculationUnitUpdate.setName( calculationUnit.getName() );
        calculationUnitUpdate.setDescription( calculationUnit.getDescription() );
    }

    @Override
    public Group toGroup(GroupCreateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Group group = new Group();

        group.setCode( dto.getCode() );
        group.setDescription( dto.getDescription() );

        return group;
    }

    @Override
    public void serForInstanceGroupUpdate(Group groupUpdate, Group group) {
        if ( group == null ) {
            return;
        }

        groupUpdate.setUpdatedAt( group.getUpdatedAt() );
        groupUpdate.setCode( group.getCode() );
        groupUpdate.setDescription( group.getDescription() );
    }

    @Override
    public Category toCategory(CategoryCreateRequestDTO dto, Group group) {
        if ( dto == null && group == null ) {
            return null;
        }

        Category category = new Category();

        if ( dto != null ) {
            category.setDescription( dto.getDescription() );
            category.setName( dto.getName() );
        }
        if ( group != null ) {
            category.setGroup( group );
        }

        return category;
    }

    @Override
    public Category toCategoryUpdate(Category categoryUpdate, CategoryUpdateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        categoryUpdate.setName( dto.getName() );
        categoryUpdate.setDescription( dto.getDescription() );

        return categoryUpdate;
    }

    @Override
    public Department toDepartment(DepartmentCreateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Department department = new Department();

        department.setName( dto.getName() );
        department.setDescription( dto.getDescription() );

        return department;
    }

    @Override
    public void setForInstanceDepartmentUpdate(Department departmentUpdate, Department department) {
        if ( department == null ) {
            return;
        }

        departmentUpdate.setUpdatedAt( department.getUpdatedAt() );
        departmentUpdate.setName( department.getName() );
        departmentUpdate.setDescription( department.getDescription() );
    }

    @Override
    public TypePlace toTypePlace(TypePlaceCreateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        TypePlace typePlace = new TypePlace();

        typePlace.setName( dto.getName() );
        typePlace.setDescription( dto.getDescription() );

        return typePlace;
    }

    @Override
    public void setForInstanceTypePlaceUpdate(TypePlace typePlaceUpdate, TypePlace typePlace) {
        if ( typePlace == null ) {
            return;
        }

        typePlaceUpdate.setUpdatedAt( typePlace.getUpdatedAt() );
        typePlaceUpdate.setName( typePlace.getName() );
        typePlaceUpdate.setDescription( typePlace.getDescription() );
    }

    @Override
    public Product toProduct(ProductCreateRequestDTO dto, Category category, CalculationUnit calculationUnit) {
        if ( dto == null && category == null && calculationUnit == null ) {
            return null;
        }

        Product product = new Product();

        if ( dto != null ) {
            product.setName( dto.getName() );
            product.setDescription( dto.getDescription() );
            product.setOrigin( dto.getOrigin() );
            product.setType( dto.getType() );
            product.setTimeAllocationType( dto.getTimeAllocationType() );
            product.setAllocationDuration( dto.getAllocationDuration() );
        }
        if ( category != null ) {
            product.setCategory( category );
        }
        if ( calculationUnit != null ) {
            product.setCalculationUnit( calculationUnit );
        }

        return product;
    }

    @Override
    public Product toProductUpdate(Product productUpdate, ProductUpdateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        productUpdate.setName( dto.getName() );
        productUpdate.setDescription( dto.getDescription() );
        productUpdate.setOrigin( dto.getOrigin() );
        productUpdate.setType( dto.getType() );
        productUpdate.setTimeAllocationType( dto.getTimeAllocationType() );
        productUpdate.setAllocationDuration( dto.getAllocationDuration() );

        return productUpdate;
    }

    @Override
    public Additional toAdditional(AdditionalCreateRequestDTO dto, User user, Organization organization) {
        if ( dto == null && user == null && organization == null ) {
            return null;
        }

        Additional additional = new Additional();

        if ( dto != null ) {
            additional.setTime( dto.getTime() );
        }
        if ( user != null ) {
            additional.setUser( user );
        }
        if ( organization != null ) {
            additional.setOrganization( organization );
        }

        return additional;
    }

    @Override
    public Additional toAdditionalUpdate(Additional additionalUpdate, AdditionalUpdateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        additionalUpdate.setTime( dto.getTime() );

        return additionalUpdate;
    }

    @Override
    public Liquidate toLiquidate(LiquidateCreateRequestDTO dto, User user) {
        if ( dto == null && user == null ) {
            return null;
        }

        Liquidate liquidate = new Liquidate();

        if ( dto != null ) {
            liquidate.setTime( dto.getTime() );
        }
        if ( user != null ) {
            liquidate.setUser( user );
        }

        return liquidate;
    }

    @Override
    public Liquidate toLiquidateUpdate(Liquidate liquidateUpdate, LiquidateUpdateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        liquidateUpdate.setTime( dto.getTime() );

        return liquidateUpdate;
    }

    @Override
    public Place toPlace(PlaceCreateRequestDTO dto, TypePlace typePlace, Campus campus, Department department) {
        if ( dto == null && typePlace == null && campus == null && department == null ) {
            return null;
        }

        Place place = new Place();

        if ( dto != null ) {
            place.setDescription( dto.getDescription() );
            place.setCode( dto.getCode() );
            place.setNameSpecification( dto.getNameSpecification() );
            place.setFloor( dto.getFloor() );
            place.setDirection( dto.getDirection() );
        }
        if ( typePlace != null ) {
            place.setTypePlace( typePlace );
        }
        if ( campus != null ) {
            place.setCampus( campus );
        }
        if ( department != null ) {
            place.setDepartment( department );
        }

        return place;
    }

    @Override
    public Place toPlaceUpdate(Place placeUpdate, PlaceUpdateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        placeUpdate.setCode( dto.getCode() );
        placeUpdate.setNameSpecification( dto.getNameSpecification() );
        placeUpdate.setDescription( dto.getDescription() );
        placeUpdate.setFloor( dto.getFloor() );
        placeUpdate.setDirection( dto.getDirection() );

        return placeUpdate;
    }

    @Override
    public Inventory toInventory(InventoryCreateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Inventory inventory = new Inventory();

        inventory.setTime( dto.getTime() );
        inventory.setStartTime( dto.getStartTime() );
        inventory.setEndTime( dto.getEndTime() );

        return inventory;
    }

    @Override
    public AdditionalProduct toAdditionalProduct(AdditionalProductCreateDTO dto, Additional additional, Product product) {
        if ( dto == null && additional == null && product == null ) {
            return null;
        }

        AdditionalProduct additionalProduct = new AdditionalProduct();

        if ( dto != null ) {
            additionalProduct.setPrice( dto.getPrice() );
        }
        if ( additional != null ) {
            additionalProduct.setAdditional( additional );
        }
        if ( product != null ) {
            additionalProduct.setProduct( product );
        }

        return additionalProduct;
    }

    @Override
    public Material toMaterial(MaterialCreateRequestDTO dto, Additional additional, Product product, Place currentPlace, User user) {
        if ( dto == null && additional == null && product == null && currentPlace == null && user == null ) {
            return null;
        }

        Material material = new Material();

        if ( dto != null ) {
            material.setCredentialCode( dto.getCredentialCode() );
            material.setStatus( dto.getStatus() );
            material.setTimeStartDepreciation( dto.getTimeStartDepreciation() );
            material.setHaveInclude( dto.getHaveInclude() );
            material.setParentCode( dto.getParentCode() );
        }
        if ( additional != null ) {
            material.setAdditional( additional );
        }
        if ( product != null ) {
            material.setProduct( product );
        }
        if ( currentPlace != null ) {
            material.setCurrentPlace( currentPlace );
        }
        if ( user != null ) {
            material.setUser( user );
        }

        return material;
    }

    @Override
    public Material toMaterialUpdate(Material materialUpdate, MaterialUpdateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        materialUpdate.setCredentialCode( dto.getCredentialCode() );
        materialUpdate.setStatus( dto.getStatus() );
        materialUpdate.setTimeStartDepreciation( dto.getTimeStartDepreciation() );
        materialUpdate.setHaveInclude( dto.getHaveInclude() );
        materialUpdate.setParentCode( dto.getParentCode() );

        return materialUpdate;
    }

    @Override
    public LiquidateMaterial toLiquidateMaterial(Liquidate liquidate, Material material) {
        if ( liquidate == null && material == null ) {
            return null;
        }

        LiquidateMaterial liquidateMaterial = new LiquidateMaterial();

        if ( liquidate != null ) {
            liquidateMaterial.setLiquidate( liquidate );
        }
        if ( material != null ) {
            liquidateMaterial.setMaterial( material );
        }

        return liquidateMaterial;
    }

    @Override
    public TransferMaterial toTransferMaterial(TransferMaterialCreateRequestDTO dto, Place placeFrom, Place placeTarget, Material material, User user) {
        if ( dto == null && placeFrom == null && placeTarget == null && material == null && user == null ) {
            return null;
        }

        TransferMaterial transferMaterial = new TransferMaterial();

        if ( dto != null ) {
            transferMaterial.setTime( dto.getTime() );
            transferMaterial.setReason( dto.getReason() );
        }
        if ( placeFrom != null ) {
            transferMaterial.setPlaceFrom( placeFrom );
        }
        if ( placeTarget != null ) {
            transferMaterial.setPlaceTarget( placeTarget );
        }
        if ( material != null ) {
            transferMaterial.setMaterial( material );
        }
        if ( user != null ) {
            transferMaterial.setUser( user );
        }

        return transferMaterial;
    }

    @Override
    public InventoryMaterial toInventoryMaterial(Inventory inventory, Material material, User user) {
        if ( inventory == null && material == null && user == null ) {
            return null;
        }

        InventoryMaterial inventoryMaterial = new InventoryMaterial();

        if ( inventory != null ) {
            inventoryMaterial.setInventory( inventory );
        }
        if ( material != null ) {
            inventoryMaterial.setMaterial( material );
        }
        if ( user != null ) {
            inventoryMaterial.setUser( user );
        }

        return inventoryMaterial;
    }

    @Override
    public User toUserUpdate(User userUpdate, UserUpdateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        userUpdate.setFullName( dto.getFullName() );
        userUpdate.setPhone( dto.getPhone() );
        userUpdate.setEmail( dto.getEmail() );
        userUpdate.setUsername( dto.getUsername() );
        userUpdate.setActive( dto.getActive() );

        return userUpdate;
    }
}
