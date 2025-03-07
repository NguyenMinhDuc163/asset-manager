package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Product;
import com.ptit.asset.dto.request.FetchPageProductRequestDTO;
import com.ptit.asset.dto.request.ProductCreateRequestDTO;
import com.ptit.asset.dto.request.ProductUpdateRequestDTO;
import com.ptit.asset.repository.CalculationUnitRepository;
import com.ptit.asset.repository.CategoryRepository;
import com.ptit.asset.repository.ProductRepository;
import com.ptit.asset.service.manager.ProductManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductManagementImpl implements ProductManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CalculationUnitRepository calculationUnitRepository;


    @Override
    public Try<Product> getOne(Long id) {
        return Try.of(() -> productRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get product by id: "+id)));
    }

    @Override
    public Try<Product> create(ProductCreateRequestDTO dto) {
        val category = categoryRepository.findById(dto.getEmbedded().getCategoryId());
        if (!category.isPresent()){
            return Try.failure(new Exception("Failure when find category with id: "+dto.getEmbedded().getCategoryId()));
        }
        val calculationUnit = calculationUnitRepository.findById(dto.getEmbedded().getCalculationUnitId());
        if (!calculationUnit.isPresent()){
            return Try.failure(new Exception("Failure when find calculation unit with id: "+dto.getEmbedded().getCalculationUnitId()));
        }
        val product = centralMapper.toProduct(dto, category.get(), calculationUnit.get());
        double duration = dto.getAllocationDuration();
        product.setDepreciationRate(((1.0d / duration) * 100.0d));
        return Try.of(() -> productRepository.save(product))
            .orElse(() -> Try.failure(new Exception("Failure when save product")));
    }

    @Override
    public Try<Product> update(ProductUpdateRequestDTO dto) {
        val productResult = productRepository.findById(dto.getId());
        if (!productResult.isPresent()){
            return Try.failure(new Exception("Failure when find product to update with id: "+dto.getId()));
        }

        val productUpdate = centralMapper.toProductUpdate(productResult.get(), dto);

        // relationship space
        if (dto.getEmbedded() != null){
            val categoryId = dto.getEmbedded().getCategoryId();
            if (categoryId != null && !categoryId.equals(productUpdate.getCategory().getId())){
                val category = categoryRepository.findById(categoryId);
                category.ifPresent(productUpdate::setCategory);
            }

            val calculationUnitId = dto.getEmbedded().getCalculationUnitId();
            if (calculationUnitId != null && !calculationUnitId.equals(productUpdate.getCalculationUnit().getId())){
                val calculationUnit = calculationUnitRepository.findById(calculationUnitId);
                calculationUnit.ifPresent(productUpdate::setCalculationUnit);
            }
        }
        // relationship space
        double duration = dto.getAllocationDuration();
        productUpdate.setDepreciationRate(((1.0d / duration) * 100.0d));
        System.out.println();
        return Try.of(() -> productRepository.save(productUpdate))
            .orElse(() -> Try.failure(new Exception("Failure when update product")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            productRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete product with id: "+id)));
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @Override
    public List<Product> fetchAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> fetchPage(FetchPageProductRequestDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage(),dto.getSize());
        return productRepository.findAll(pageable);
    }
}
