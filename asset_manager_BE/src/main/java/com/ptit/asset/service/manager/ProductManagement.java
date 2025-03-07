package com.ptit.asset.service.manager;

import com.ptit.asset.domain.Product;
import com.ptit.asset.dto.request.FetchPageProductRequestDTO;
import com.ptit.asset.dto.request.ProductCreateRequestDTO;
import com.ptit.asset.dto.request.ProductUpdateRequestDTO;
import io.vavr.control.Try;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductManagement {

    Try<Product> getOne(Long id);

    Try<Product> create(ProductCreateRequestDTO dto);

    Try<Product> update(ProductUpdateRequestDTO dto);

    Try<Boolean> delete(Long id);

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    List<Product> fetchAll();

    Page<Product> fetchPage(FetchPageProductRequestDTO dto);
}
