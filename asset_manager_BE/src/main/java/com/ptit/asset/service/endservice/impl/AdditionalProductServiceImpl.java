package com.ptit.asset.service.endservice.impl;

import com.google.zxing.WriterException;
import com.ptit.asset.domain.AdditionalProduct;
import com.ptit.asset.dto.request.AdditionalProductCreateDTO;
import com.ptit.asset.dto.request.AdditionalProductCreateInBoxNewVersionRequestDTO;
import com.ptit.asset.dto.request.AdditionalProductCreateInBoxRequestDTO;
import com.ptit.asset.exception.InvalidProductResourcesException;
import com.ptit.asset.service.endservice.AdditionalProductService;
import com.ptit.asset.service.manager.AdditionalProductManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AdditionalProductServiceImpl implements AdditionalProductService {

    @Autowired
    private AdditionalProductManagement additionalProductManagement;


    @Override
    public Try<AdditionalProduct> getOne(Long id) {
        return additionalProductManagement.getOne(id);
    }

    @Override
    public Try<AdditionalProduct> create(AdditionalProductCreateDTO dto) {
        return additionalProductManagement.create(dto);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return additionalProductManagement.delete(id);
    }

    @Override
    public List<AdditionalProduct> fetchAll() {
        return additionalProductManagement.fetchAll();
    }

//    @Override
//    public Try<Boolean> createInBox(AdditionalProductCreateInBoxRequestDTO dto) throws WriterException, IOException {
//        return additionalProductManagement.createInBox(dto);
//    }

    @Override
    public Try<Boolean> createInBox(AdditionalProductCreateInBoxNewVersionRequestDTO dto)
            throws WriterException, IOException, InvalidProductResourcesException {
        return additionalProductManagement.createInBox(dto);
    }
}
