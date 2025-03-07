package com.ptit.asset.service.endservice;

import com.google.zxing.WriterException;
import com.ptit.asset.domain.AdditionalProduct;
import com.ptit.asset.dto.request.AdditionalProductCreateDTO;
import com.ptit.asset.dto.request.AdditionalProductCreateInBoxNewVersionRequestDTO;
import com.ptit.asset.dto.request.AdditionalProductCreateInBoxRequestDTO;
import com.ptit.asset.exception.InvalidProductResourcesException;
import io.vavr.control.Try;

import java.io.IOException;
import java.util.List;

public interface AdditionalProductService {

    Try<AdditionalProduct> getOne(Long id);

    Try<AdditionalProduct> create(AdditionalProductCreateDTO dto);

    Try<Boolean> delete(Long id);

    List<AdditionalProduct> fetchAll();

//    Try<Boolean> createInBox(AdditionalProductCreateInBoxRequestDTO dto) throws WriterException, IOException;

    Try<Boolean> createInBox(AdditionalProductCreateInBoxNewVersionRequestDTO dto)
            throws WriterException, IOException, InvalidProductResourcesException;

}
