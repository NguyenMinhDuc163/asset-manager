package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Liquidate;
import com.ptit.asset.domain.LiquidateMaterial;
import com.ptit.asset.domain.Material;
import com.ptit.asset.domain.User;
import com.ptit.asset.domain.enumeration.MaterialStatus;
import com.ptit.asset.dto.request.LiquidateChangeStatusRequestDTO;
import com.ptit.asset.dto.request.LiquidateCreateRequestDTO;
import com.ptit.asset.dto.request.LiquidateUpdateRequestDTO;
import com.ptit.asset.repository.LiquidateMaterialRepository;
import com.ptit.asset.repository.LiquidateRepository;
import com.ptit.asset.repository.MaterialRepository;
import com.ptit.asset.repository.UserRepository;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LiquidateManagementImplTest {

    @Mock private CentralMapper centralMapper;
    @Mock private LiquidateRepository liquidateRepository;
    @Mock private UserRepository userRepository;
    @Mock private MaterialRepository materialRepository;
    @Mock private LiquidateMaterialRepository liquidateMaterialRepository;
    @InjectMocks private LiquidateManagementImpl liquidateManagement;

    private Liquidate testLiquidate;
    private User testUser;
    private Material testMaterial;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        testLiquidate = new Liquidate();
        testLiquidate.setId(1L);
        testLiquidate.setDone(false);
        testLiquidate.setTime(Instant.now());
        testLiquidate.setUser(testUser);

        testMaterial = new Material();
        testMaterial.setId(1L);
        testMaterial.setCredentialCode("MAT001");
        testMaterial.setStatus(MaterialStatus.IN_USED);
        testMaterial.setTimeStartDepreciation(Instant.now());
    }

    // STT 1: Tests for getOne method
    @Test
    // Mô tả: Kiểm tra lấy Liquidate thành công khi ID tồn tại
    void getOne_success() {
        when(liquidateRepository.findById(1L)).thenReturn(Optional.of(testLiquidate));
        Try<Liquidate> result = liquidateManagement.getOne(1L);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(testLiquidate);
        verify(liquidateRepository).findById(1L);
    }

    @Test
        // Mô tả: Kiểm tra lấy Liquidate thất bại khi ID không tồn tại
    void getOne_failure_notFound() {
        when(liquidateRepository.findById(1L)).thenReturn(Optional.empty());
        Try<Liquidate> result = liquidateManagement.getOne(1L);
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when get liquidate by id: 1");
        verify(liquidateRepository).findById(1L);
    }

    // STT 2: Tests for create method
    @Test
    // Mô tả: Kiểm tra tạo Liquidate thành công với dữ liệu hợp lệ
    void create_success() {
        LiquidateCreateRequestDTO dto = new LiquidateCreateRequestDTO();
        LiquidateCreateRequestDTO.Embedded embedded = new LiquidateCreateRequestDTO.Embedded();
        embedded.setUserId(1L);
        dto.setEmbedded(embedded);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(liquidateRepository.findAll()).thenReturn(Collections.emptyList());
        when(centralMapper.toLiquidate(dto, testUser)).thenReturn(testLiquidate);
        when(liquidateRepository.save(testLiquidate)).thenReturn(testLiquidate);

        Try<Liquidate> result = liquidateManagement.create(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(testLiquidate);
        assertThat(result.get().getDone()).isFalse();
        verify(userRepository).findById(1L);
        verify(liquidateRepository).findAll();
        verify(centralMapper).toLiquidate(dto, testUser);
        verify(liquidateRepository).save(testLiquidate);
    }

    @Test
        // Mô tả: Kiểm tra tạo Liquidate thất bại khi user không tồn tại
    void create_failure_userNotFound() {
        LiquidateCreateRequestDTO dto = new LiquidateCreateRequestDTO();
        LiquidateCreateRequestDTO.Embedded embedded = new LiquidateCreateRequestDTO.Embedded();
        embedded.setUserId(1L);
        dto.setEmbedded(embedded);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Liquidate> result = liquidateManagement.create(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find user by id: 1");
        verify(userRepository).findById(1L);
    }

    @Test
        // Mô tả: Kiểm tra tạo Liquidate thất bại khi có Liquidate khác chưa hoàn tất
    void create_failure_validationData_notDone() {
        LiquidateCreateRequestDTO dto = new LiquidateCreateRequestDTO();
        LiquidateCreateRequestDTO.Embedded embedded = new LiquidateCreateRequestDTO.Embedded();
        embedded.setUserId(1L);
        dto.setEmbedded(embedded);

        Liquidate existing = new Liquidate();
        existing.setId(2L);
        existing.setDone(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(liquidateRepository.findAll()).thenReturn(Collections.singletonList(existing));

        Try<Liquidate> result = liquidateManagement.create(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Already existing liquidate not done");
        verify(userRepository).findById(1L);
        verify(liquidateRepository).findAll();
    }

    @Test
        // Mô tả: Kiểm tra tạo Liquidate thành công khi tất cả Liquidate khác đã hoàn tất
    void create_success_validationData_allDone() {
        LiquidateCreateRequestDTO dto = new LiquidateCreateRequestDTO();
        LiquidateCreateRequestDTO.Embedded embedded = new LiquidateCreateRequestDTO.Embedded();
        embedded.setUserId(1L);
        dto.setEmbedded(embedded);

        Liquidate existing = new Liquidate();
        existing.setId(2L);
        existing.setDone(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(liquidateRepository.findAll()).thenReturn(Collections.singletonList(existing));
        when(centralMapper.toLiquidate(dto, testUser)).thenReturn(testLiquidate);
        when(liquidateRepository.save(testLiquidate)).thenReturn(testLiquidate);

        Try<Liquidate> result = liquidateManagement.create(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(testLiquidate);
        assertThat(result.get().getDone()).isFalse();
        verify(userRepository).findById(1L);
        verify(liquidateRepository).findAll();
        verify(centralMapper).toLiquidate(dto, testUser);
        verify(liquidateRepository).save(testLiquidate);
    }

    // STT 3: Tests for update method
/*
    @Test
    // Mô tả: Kiểm tra cập nhật Liquidate thành công khi không có embedded
    void update_success_noEmbedded() {
        LiquidateUpdateRequestDTO dto = new LiquidateUpdateRequestDTO();
        dto.setId(1L);

        when(liquidateRepository.findById(1L)).thenReturn(Optional.of(testLiquidate));
        when(centralMapper.toLiquidateUpdate(testLiquidate, dto)).thenReturn(testLiquidate);
        when(liquidateRepository.save(testLiquidate)).thenReturn(testLiquidate);

        Try<Liquidate> result = liquidateManagement.update(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(testLiquidate);
        verify(liquidateRepository).findById(1L);
        verify(centralMapper).toLiquidateUpdate(testLiquidate, dto);
        verify(liquidateRepository).save(testLiquidate);
        verifyNoInteractions(userRepository);
    }
*/

    @Test
        // Mô tả: Kiểm tra cập nhật Liquidate thành công khi thay đổi user
    void update_success_changeUser() {
        LiquidateUpdateRequestDTO dto = new LiquidateUpdateRequestDTO();
        dto.setId(1L);
        LiquidateUpdateRequestDTO.Embedded embedded = new LiquidateUpdateRequestDTO.Embedded();
        embedded.setUserId(2L);
        dto.setEmbedded(embedded);

        User newUser = new User();
        newUser.setId(2L);

        when(liquidateRepository.findById(1L)).thenReturn(Optional.of(testLiquidate));
        when(centralMapper.toLiquidateUpdate(testLiquidate, dto)).thenReturn(testLiquidate);
        when(userRepository.findById(2L)).thenReturn(Optional.of(newUser));
        when(liquidateRepository.save(testLiquidate)).thenReturn(testLiquidate);

        Try<Liquidate> result = liquidateManagement.update(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(testLiquidate);
        verify(liquidateRepository).findById(1L);
        verify(centralMapper).toLiquidateUpdate(testLiquidate, dto);
        verify(userRepository).findById(2L);
        verify(liquidateRepository).save(testLiquidate);
    }


    @Test
        // Mô tả: Kiểm tra cập nhật Liquidate thành công khi userId không thay đổi
    void update_success_sameUser() {
        LiquidateUpdateRequestDTO dto = new LiquidateUpdateRequestDTO();
        dto.setId(1L);
        LiquidateUpdateRequestDTO.Embedded embedded = new LiquidateUpdateRequestDTO.Embedded();
        embedded.setUserId(1L);
        dto.setEmbedded(embedded);

        when(liquidateRepository.findById(1L)).thenReturn(Optional.of(testLiquidate));
        when(centralMapper.toLiquidateUpdate(testLiquidate, dto)).thenReturn(testLiquidate);
        when(liquidateRepository.save(testLiquidate)).thenReturn(testLiquidate);

        Try<Liquidate> result = liquidateManagement.update(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(testLiquidate);
        verify(liquidateRepository).findById(1L);
        verify(centralMapper).toLiquidateUpdate(testLiquidate, dto);
        verify(liquidateRepository).save(testLiquidate);
    }

    @Test
        // Mô tả: Kiểm tra cập nhật Liquidate thất bại khi không tìm thấy Liquidate
    void update_failure_notFound() {
        LiquidateUpdateRequestDTO dto = new LiquidateUpdateRequestDTO();
        dto.setId(1L);

        when(liquidateRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Liquidate> result = liquidateManagement.update(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find liquidate to update with id: 1");
        verify(liquidateRepository).findById(1L);
    }

    @Test
        // Mô tả: Kiểm tra cập nhật Liquidate thành công khi embedded null
    void update_success_nullUserId() {
        LiquidateUpdateRequestDTO dto = new LiquidateUpdateRequestDTO();
        dto.setId(1L);
        dto.setEmbedded(null);

        when(liquidateRepository.findById(1L)).thenReturn(Optional.of(testLiquidate));
        when(centralMapper.toLiquidateUpdate(testLiquidate, dto)).thenReturn(testLiquidate);
        when(liquidateRepository.save(testLiquidate)).thenReturn(testLiquidate);

        Try<Liquidate> result = liquidateManagement.update(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(testLiquidate);
        verify(liquidateRepository).findById(1L);
        verify(centralMapper).toLiquidateUpdate(testLiquidate, dto);
        verify(liquidateRepository).save(testLiquidate);
        verifyNoInteractions(userRepository);
    }

    // STT 4: Tests for delete method
    @Test
    // Mô tả: Kiểm tra xóa Liquidate thành công
    void delete_success() {
        doNothing().when(liquidateRepository).deleteById(1L);
        Try<Boolean> result = liquidateManagement.delete(1L);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(liquidateRepository).deleteById(1L);
    }

    @Test
        // Mô tả: Kiểm tra xóa Liquidate khi có liên kết LiquidateMaterial
    void delete_fail_has_materials() {

        Liquidate liquidate = new Liquidate();
        liquidate.setId(1L);
        liquidate.setDone(false);
        liquidate.setTime(Instant.now());

        Material material1 = new Material();
        material1.setId(1L);
        material1.setCredentialCode("MAT001");
        material1.setStatus(MaterialStatus.IN_USED);
        material1.setTimeStartDepreciation(Instant.now());

        Material material2 = new Material();
        material2.setId(2L);
        material2.setCredentialCode("MAT002");
        material2.setStatus(MaterialStatus.IN_USED);
        material2.setTimeStartDepreciation(Instant.now());

        LiquidateMaterial lm1 = new LiquidateMaterial();
        lm1.setId(1L);
        lm1.setLiquidate(liquidate);
        lm1.setMaterial(material1);

        LiquidateMaterial lm2 = new LiquidateMaterial();
        lm2.setId(2L);
        lm2.setLiquidate(liquidate);
        lm2.setMaterial(material2);

        doNothing().when(liquidateRepository).deleteById(1L);

        Try<Boolean> result = liquidateManagement.delete(1L);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.get()).isTrue();
        verify(liquidateRepository).deleteById(1L);
    }

    // STT 5: Tests for fetchAll method
    @Test
    // Mô tả: Kiểm tra lấy danh sách Liquidate khi không có bản ghi
    void fetchAll_success_empty() {
        when(liquidateRepository.findAll()).thenReturn(Collections.emptyList());
        List<Liquidate> result = liquidateManagement.fetchAll();
        assertThat(result).isEmpty();
        verify(liquidateRepository).findAll();
    }

    @Test
        // Mô tả: Kiểm tra lấy danh sách Liquidate khi có bản ghi
    void fetchAll_success_nonEmpty() {
        when(liquidateRepository.findAll()).thenReturn(Collections.singletonList(testLiquidate));
        List<Liquidate> result = liquidateManagement.fetchAll();
        assertThat(result).containsExactly(testLiquidate);
        verify(liquidateRepository).findAll();
    }

    // STT 6: Tests for changeStatus method
    @Test
    // Mô tả: Kiểm tra thay đổi trạng thái Liquidate thành done thành công
    void changeStatus_success() {
        LiquidateChangeStatusRequestDTO dto = new LiquidateChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(true);

        LiquidateMaterial lm = new LiquidateMaterial();
        lm.setId(1L);
        lm.setLiquidate(testLiquidate);
        lm.setMaterial(testMaterial);

        when(liquidateRepository.findById(1L)).thenReturn(Optional.of(testLiquidate));
        when(liquidateMaterialRepository.findByLiquidateId(1L)).thenReturn(Collections.singletonList(lm));
        when(materialRepository.save(testMaterial)).thenReturn(testMaterial);
        when(liquidateRepository.save(testLiquidate)).thenReturn(testLiquidate);

        Try<Boolean> result = liquidateManagement.changeStatus(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        assertThat(testLiquidate.getDone()).isTrue();
        assertThat(testMaterial.getStatus()).isEqualTo(MaterialStatus.NO_LONGER);
        verify(liquidateRepository).findById(1L);
        verify(liquidateMaterialRepository).findByLiquidateId(1L);
        verify(materialRepository).save(testMaterial);
        verify(liquidateRepository).save(testLiquidate);
    }

    @Test
        // Mô tả: Kiểm tra thay đổi trạng thái Liquidate thất bại khi không tìm thấy Liquidate
    void changeStatus_failure_notFound() {
        LiquidateChangeStatusRequestDTO dto = new LiquidateChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(true);

        when(liquidateRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Boolean> result = liquidateManagement.changeStatus(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find liquidate with id:1");
        verify(liquidateRepository).findById(1L);
    }

    @Test
        // Mô tả: Kiểm tra thay đổi trạng thái Liquidate thất bại khi trạng thái không thay đổi
    void changeStatus_failure_sameStatus() {
        LiquidateChangeStatusRequestDTO dtoNotDone = new LiquidateChangeStatusRequestDTO();
        dtoNotDone.setId(1L);
        dtoNotDone.setStatus(false);

        LiquidateChangeStatusRequestDTO dtoDone = new LiquidateChangeStatusRequestDTO();
        dtoDone.setId(1L);
        dtoDone.setStatus(true);

        // Test done = false, status = false
        when(liquidateRepository.findById(1L)).thenReturn(Optional.of(testLiquidate));
        Try<Boolean> resultNotDone = liquidateManagement.changeStatus(dtoNotDone);
        assertThat(resultNotDone.isFailure()).isTrue();
        assertThat(resultNotDone.getCause().getMessage()).contains("Nothing change with current status");

        // Test done = true, status = true
        testLiquidate.setDone(true);
        when(liquidateRepository.findById(1L)).thenReturn(Optional.of(testLiquidate));
        Try<Boolean> resultDone = liquidateManagement.changeStatus(dtoDone);
        assertThat(resultDone.isFailure()).isTrue();
        assertThat(resultDone.getCause().getMessage()).contains("Nothing change with current status");

        verify(liquidateRepository, times(2)).findById(1L);
        verifyNoInteractions(liquidateMaterialRepository, materialRepository);
    }

    @Test
        // Mô tả: Kiểm tra thay đổi trạng thái Liquidate thất bại khi cố chuyển từ done về not done
    void changeStatus_fail_FalsetoTrue() {
        LiquidateChangeStatusRequestDTO dto = new LiquidateChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(false);

        testLiquidate.setDone(true);
        when(liquidateRepository.findById(1L)).thenReturn(Optional.of(testLiquidate));

        Try<Boolean> result = liquidateManagement.changeStatus(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Something wrong with business rule");
        verify(liquidateRepository).findById(1L);
    }

}