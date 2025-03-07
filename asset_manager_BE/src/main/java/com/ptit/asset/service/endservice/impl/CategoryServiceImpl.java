package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.Category;
import com.ptit.asset.dto.request.CategoryCreateRequestDTO;
import com.ptit.asset.dto.request.CategoryUpdateRequestDTO;
import com.ptit.asset.dto.request.FetchPageCategoryRequestDTO;
import com.ptit.asset.service.endservice.CategoryService;
import com.ptit.asset.service.manager.CategoryManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryManagement categoryManagement;

    @Override
    public Try<Category> getOne(Long id) {
        return categoryManagement.getOne(id);
    }

    @Override
    public Try<Category> create(CategoryCreateRequestDTO dto) {
        return categoryManagement.create(dto);
    }

    @Override
    public Try<Category> update(CategoryUpdateRequestDTO dto) {
        return categoryManagement.update(dto);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return categoryManagement.delete(id);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @Override
    public List<Category> fetchAll() {
        return categoryManagement.fetchAll();
    }

    @Override
    public Page<Category> fetchPage(FetchPageCategoryRequestDTO dto) {
        return categoryManagement.fetchPage(dto);
    }
}
