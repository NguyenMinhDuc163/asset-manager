package com.ptit.asset.service.endservice;

import com.ptit.asset.domain.Category;
import com.ptit.asset.dto.request.CategoryCreateRequestDTO;
import com.ptit.asset.dto.request.CategoryUpdateRequestDTO;
import com.ptit.asset.dto.request.FetchPageCategoryRequestDTO;
import io.vavr.control.Try;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    Try<Category> getOne(Long id);

    Try<Category> create(CategoryCreateRequestDTO dto);

    Try<Category> update(CategoryUpdateRequestDTO dto);

    Try<Boolean> delete(Long id);

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    List<Category> fetchAll();

    Page<Category> fetchPage(FetchPageCategoryRequestDTO dto);


}
