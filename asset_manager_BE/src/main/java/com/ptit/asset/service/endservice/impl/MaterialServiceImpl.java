package com.ptit.asset.service.endservice.impl;

import com.google.zxing.WriterException;
import com.ptit.asset.domain.Material;
import com.ptit.asset.dto.request.FetchPageMaterialRequestDTO;
import com.ptit.asset.dto.request.MaterialCreateRequestDTO;
import com.ptit.asset.dto.request.MaterialFilterRequestDTO;
import com.ptit.asset.dto.request.MaterialUpdateRequestDTO;
import com.ptit.asset.dto.response.MaterialResponseDTO;
import com.ptit.asset.repository.data.Statistical;
import com.ptit.asset.service.endservice.MaterialService;
import com.ptit.asset.service.manager.MaterialManagement;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialManagement materialManagement;


    @Override
    public Try<Material> getOne(Long id) {
        return materialManagement.getOne(id);
    }

    @Override
    public Try<Material> create(MaterialCreateRequestDTO dto) throws WriterException, IOException {
        return materialManagement.create(dto);
    }

    @Override
    public Try<Material> update(MaterialUpdateRequestDTO dto) throws WriterException, IOException {
        return materialManagement.update(dto);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return materialManagement.delete(id);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @Override
    public List<MaterialResponseDTO> fetchAll() {
        return materialManagement.fetchAll();
    }

    @Override
    public List<Material> fetchByCategory(Long categoryId) {
        return materialManagement.fetchByCategory(categoryId);
    }

    @Override
    public List<MaterialResponseDTO> filter(MaterialFilterRequestDTO dto) {
        return materialManagement.filter(dto);
    }

    @Override
    public Page<Material> fetchPage(FetchPageMaterialRequestDTO dto) {
        return materialManagement.fetchPage(dto);
    }

    @Override
    public List<Statistical.HistoryTransfer> fetchHistoryTransfer(Long id) {
        return materialManagement.fetchHistoryTransfer(id);
    }
}
