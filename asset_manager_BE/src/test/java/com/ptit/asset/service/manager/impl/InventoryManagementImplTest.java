package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Inventory;
import com.ptit.asset.dto.request.InventoryChangeStatusRequestDTO;
import com.ptit.asset.dto.request.InventoryCreateRequestDTO;
import com.ptit.asset.repository.InventoryRepository;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryManagementImplTest {

    @Mock private CentralMapper centralMapper;
    @Mock private InventoryRepository inventoryRepository;
    @InjectMocks private InventoryManagementImpl inventoryManagement;

    private Inventory testInventory;
    private InventoryCreateRequestDTO validDto;

    @BeforeEach
    void setUp() {
        testInventory = new Inventory();
        testInventory.setId(1L);
        testInventory.setInCheck(false);
        testInventory.setStartTime(Instant.now());
        testInventory.setEndTime(Instant.now().plusSeconds(3600));

        validDto = new InventoryCreateRequestDTO();
        validDto.setStartTime(Instant.now());
        validDto.setEndTime(Instant.now().plusSeconds(3600));
    }

    // STT 1: Tests for getOne method
    @Test
    // Mô tả: Kiểm tra lấy Inventory thành công khi ID tồn tại
    void getOne_success() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(testInventory));
        Try<Inventory> result = inventoryManagement.getOne(1L);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(testInventory);
        verify(inventoryRepository).findById(1L);
    }

    @Test
        // Mô tả: Kiểm tra lấy Inventory thất bại khi ID không tồn tại
    void getOne_failure_notFound() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());
        Try<Inventory> result = inventoryManagement.getOne(1L);
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when get inventory by id: 1");
        verify(inventoryRepository).findById(1L);
    }

    // STT 2: Tests for create method
    @Test
        // Mô tả: Kiểm tra tạo Inventory thành công khi có Inventory khác với inCheck = false
    void create_success_withNotInCheckInventory() {
        Inventory existing = new Inventory();
        existing.setId(2L);
        existing.setInCheck(false);

        when(inventoryRepository.findAll()).thenReturn(Collections.singletonList(existing));
        when(centralMapper.toInventory(validDto)).thenReturn(testInventory);
        when(inventoryRepository.save(testInventory)).thenReturn(testInventory);

        Try<Inventory> result = inventoryManagement.create(validDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(testInventory);
        assertThat(result.get().getInCheck()).isTrue();
        verify(inventoryRepository).findAll();
        verify(centralMapper).toInventory(validDto);
        verify(inventoryRepository).save(testInventory);
    }

    @Test
        // Mô tả: Kiểm tra tạo Inventory thất bại khi startTime lớn hơn endTime
    void create_failure_invalidTime() {
        InventoryCreateRequestDTO invalidDto = new InventoryCreateRequestDTO();
        invalidDto.setStartTime(Instant.now().plusSeconds(3600));
        invalidDto.setEndTime(Instant.now());

        Try<Inventory> result = inventoryManagement.create(invalidDto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Start time must less than End time");
        verifyNoInteractions(inventoryRepository, centralMapper);
    }

    @Test
        // Mô tả: Kiểm tra tạo Inventory thất bại khi đã có Inventory đang inCheck
    void create_failure_existingInCheck() {
        Inventory existing = new Inventory();
        existing.setId(2L);
        existing.setInCheck(true);

        when(inventoryRepository.findAll()).thenReturn(Collections.singletonList(existing));

        Try<Inventory> result = inventoryManagement.create(validDto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Already existing inventory with status (IN_CHECK)");
        verify(inventoryRepository).findAll();
        verifyNoInteractions(centralMapper);
        verify(inventoryRepository, never()).save(any());
    }


    // STT 3: Tests for delete method
    @Test
    // Mô tả: Kiểm tra xóa Inventory thành công
    void delete_success() {
        doNothing().when(inventoryRepository).deleteById(1L);
        Try<Boolean> result = inventoryManagement.delete(1L);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(inventoryRepository).deleteById(1L);
    }
    @Test
// Mô tả: Kiểm tra xóa Inventory thất bại khi có cơ sở vật chất đã được kiểm kê
    void delete_failure_hasCheckedMaterials() {
        doNothing().when(inventoryRepository).deleteById(1L);

        Try<Boolean> result = inventoryManagement.delete(1L);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Cannot delete inventory with checked materials");
        verify(inventoryRepository).deleteById(1L);
    }

    // STT 4: Tests for fetchAll method
    @Test
    // Mô tả: Kiểm tra lấy danh sách Inventory khi không có bản ghi
    void fetchAll_success_empty() {
        when(inventoryRepository.findAll()).thenReturn(Collections.emptyList());
        List<Inventory> result = inventoryManagement.fetchAll();
        assertThat(result).isEmpty();
        verify(inventoryRepository).findAll();
    }

    @Test
        // Mô tả: Kiểm tra lấy danh sách Inventory khi có bản ghi
    void fetchAll_success_nonEmpty() {
        when(inventoryRepository.findAll()).thenReturn(Collections.singletonList(testInventory));
        List<Inventory> result = inventoryManagement.fetchAll();
        assertThat(result).containsExactly(testInventory);
        verify(inventoryRepository).findAll();
    }

    // STT 5: Tests for changeStatus method
    @Test
    // Mô tả: Kiểm tra thay đổi trạng thái Inventory thành inCheck thành công khi không có Inventory khác inCheck
    void changeStatus_success_toInCheck() {
        InventoryChangeStatusRequestDTO dto = new InventoryChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(true);

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.findAll()).thenReturn(Collections.emptyList());
        when(inventoryRepository.save(testInventory)).thenReturn(testInventory);

        Try<Boolean> result = inventoryManagement.changeStatus(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        assertThat(testInventory.getInCheck()).isTrue();
        verify(inventoryRepository).findById(1L);
        verify(inventoryRepository).findAll();
        verify(inventoryRepository).save(testInventory);
    }

    @Test
        // Mô tả: Kiểm tra thay đổi trạng thái Inventory thành inCheck thành công khi có Inventory khác với inCheck = false
    void changeStatus_success_toInCheck_withNotInCheckInventory() {
        InventoryChangeStatusRequestDTO dto = new InventoryChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(true);

        Inventory existing = new Inventory();
        existing.setId(2L);
        existing.setInCheck(false);

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.findAll()).thenReturn(Collections.singletonList(existing));
        when(inventoryRepository.save(testInventory)).thenReturn(testInventory);

        Try<Boolean> result = inventoryManagement.changeStatus(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        assertThat(testInventory.getInCheck()).isTrue();
        verify(inventoryRepository).findById(1L);
        verify(inventoryRepository).findAll();
        verify(inventoryRepository).save(testInventory);
    }

    @Test
        // Mô tả: Kiểm tra thay đổi trạng thái Inventory thành not inCheck thành công khi Inventory đang inCheck
    void changeStatus_success_toNotInCheck() {
        testInventory.setInCheck(true);
        InventoryChangeStatusRequestDTO dto = new InventoryChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(false);

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.save(testInventory)).thenReturn(testInventory);

        Try<Boolean> result = inventoryManagement.changeStatus(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        assertThat(testInventory.getInCheck()).isFalse();
        verify(inventoryRepository).findById(1L);
        verify(inventoryRepository).save(testInventory);
        verify(inventoryRepository, never()).findAll();
    }

    @Test
        // Mô tả: Kiểm tra thay đổi trạng thái Inventory thất bại khi không tìm thấy Inventory
    void changeStatus_failure_notFound() {
        InventoryChangeStatusRequestDTO dto = new InventoryChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(true);

        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Boolean> result = inventoryManagement.changeStatus(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find inventory with id:1");
        verify(inventoryRepository).findById(1L);
        verifyNoMoreInteractions(inventoryRepository);
    }

    @Test
        // Mô tả: Kiểm tra thay đổi trạng thái Inventory thất bại khi trạng thái không thay đổi thành inCheck
    void changeStatus_failure_sameStatus_toInCheck() {
        testInventory.setInCheck(true);
        InventoryChangeStatusRequestDTO dto = new InventoryChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(true);

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(testInventory));

        Try<Boolean> result = inventoryManagement.changeStatus(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Nothing change with current status");
        verify(inventoryRepository).findById(1L);
        verify(inventoryRepository, never()).findAll();
    }

    @Test
        // Mô tả: Kiểm tra thay đổi trạng thái Inventory thất bại khi trạng thái không thay đổi thành not inCheck
    void changeStatus_failure_sameStatus_toNotInCheck() {
        InventoryChangeStatusRequestDTO dto = new InventoryChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(false);

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(testInventory));

        Try<Boolean> result = inventoryManagement.changeStatus(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Nothing change with current status");
        verify(inventoryRepository).findById(1L);
        verify(inventoryRepository, never()).findAll();
    }

    @Test
        // Mô tả: Kiểm tra thay đổi trạng thái Inventory thất bại khi đã có Inventory khác inCheck
    void changeStatus_failure_existingInCheck() {
        InventoryChangeStatusRequestDTO dto = new InventoryChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(true);

        Inventory existing = new Inventory();
        existing.setId(2L);
        existing.setInCheck(true);

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.findAll()).thenReturn(Collections.singletonList(existing));

        Try<Boolean> result = inventoryManagement.changeStatus(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Just only one inventory allow in check");
        verify(inventoryRepository).findById(1L);
        verify(inventoryRepository).findAll();
        verify(inventoryRepository, never()).save(any());
    }

}