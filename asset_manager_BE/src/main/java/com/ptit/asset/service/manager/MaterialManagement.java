package com.ptit.asset.service.manager;

import com.google.zxing.WriterException;
import com.ptit.asset.domain.Material;
import com.ptit.asset.dto.request.FetchPageMaterialRequestDTO;
import com.ptit.asset.dto.request.MaterialCreateRequestDTO;
import com.ptit.asset.dto.request.MaterialFilterRequestDTO;
import com.ptit.asset.dto.request.MaterialUpdateRequestDTO;
import com.ptit.asset.dto.response.MaterialResponseDTO;
import com.ptit.asset.repository.data.Statistical;
import io.vavr.control.Try;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface MaterialManagement {

    Try<Material> getOne(Long id);

    Try<Material> create(MaterialCreateRequestDTO dto) throws WriterException, IOException;

    Try<Material> update(MaterialUpdateRequestDTO dto) throws WriterException, IOException;

    Try<Boolean> delete(Long id);

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    List<MaterialResponseDTO> fetchAll();

    List<Material> fetchByCategory(Long categoryId);

    List<MaterialResponseDTO> filter(MaterialFilterRequestDTO dto);

    Page<Material> fetchPage(FetchPageMaterialRequestDTO dto);

    List<Statistical.HistoryTransfer> fetchHistoryTransfer(Long id);

}
