package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.Product;
import com.ptit.asset.dto.request.FetchPageProductRequestDTO;
import com.ptit.asset.dto.request.ProductCreateRequestDTO;
import com.ptit.asset.dto.request.ProductUpdateRequestDTO;
import com.ptit.asset.service.endservice.ProductService;
import com.ptit.asset.service.manager.ProductManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductManagement productManagement;
    @Override
    public Try<Product> getOne(Long id) {
        return productManagement.getOne(id);
    }

    @Override
    public Try<Product> create(ProductCreateRequestDTO dto) {
        return productManagement.create(dto);
    }

    @Override
    public Try<Product> update(ProductUpdateRequestDTO dto) {
        return productManagement.update(dto);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return productManagement.delete(id);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @Override
    public List<Product> fetchAll() {
        return productManagement.fetchAll();
    }

    @Override
    public Page<Product> fetchPage(FetchPageProductRequestDTO dto) {
        return productManagement.fetchPage(dto);
    }
}
