package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.CalculationUnit;
import com.ptit.asset.dto.request.CalculationUnitCreateRequestDTO;
import com.ptit.asset.repository.CalculationUnitRepository;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Class test cho CalculationUnitManagementImpl, kiểm tra các chức năng quản lý đơn vị tính (CalculationUnit).
 */
@ExtendWith(MockitoExtension.class)
class CalculationUnitManagementImplTest {

    @Mock
    private CalculationUnitRepository calculationUnitRepository;

    @Mock
    private CentralMapper centralMapper;

    @InjectMocks
    private CalculationUnitManagementImpl calculationUnitManagement;

    private CalculationUnit testCalculationUnit;
    private CalculationUnitCreateRequestDTO createDto;

    @BeforeEach
    void setUp() {
        // Khởi tạo dữ liệu cơ bản cho CalculationUnit
        testCalculationUnit = new CalculationUnit();
        testCalculationUnit.setId(1L);
        testCalculationUnit.setName("Unit 1");

        // Khởi tạo dữ liệu cho DTO
        createDto = new CalculationUnitCreateRequestDTO();
        createDto.setName("Unit 1");
    }

    // Test cho phương thức getOne
    @Test
    void testGetOne_Success() {
        // Arrange
        when(calculationUnitRepository.findById(1L)).thenReturn(Optional.of(testCalculationUnit));

        // Act
        Try<CalculationUnit> result = calculationUnitManagement.getOne(1L);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Unit 1");
        verify(calculationUnitRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(calculationUnitRepository, centralMapper);
    }

    @Test
    void testGetOne_NotFound() {
        // Arrange
        when(calculationUnitRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Try<CalculationUnit> result = calculationUnitManagement.getOne(1L);

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when get calculation unit by id: 1");
        verify(calculationUnitRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(calculationUnitRepository, centralMapper);
    }

    @Test
    void testGetOne_DatabaseError() {
        // Arrange
        when(calculationUnitRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));
        // Act
        Try<CalculationUnit> result = calculationUnitManagement.getOne(1L);

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when get calculation unit by id: 1");
        verify(calculationUnitRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(calculationUnitRepository, centralMapper);
    }

    // Test cho phương thức create
    @Test
    void testCreate_Success() {
        // Arrange
        when(centralMapper.toCalculationUnit(any(CalculationUnitCreateRequestDTO.class))).thenReturn(testCalculationUnit);
        when(calculationUnitRepository.save(any(CalculationUnit.class))).thenReturn(testCalculationUnit);

        // Act
        Try<CalculationUnit> result = calculationUnitManagement.create(createDto);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Unit 1");
        verify(centralMapper, times(1)).toCalculationUnit(any(CalculationUnitCreateRequestDTO.class));
        verify(calculationUnitRepository, times(1)).save(any(CalculationUnit.class));
        verifyNoMoreInteractions(calculationUnitRepository, centralMapper);
    }

    @Test
    void testCreate_Fail_SaveError() {
        // Arrange
        when(centralMapper.toCalculationUnit(any(CalculationUnitCreateRequestDTO.class))).thenReturn(testCalculationUnit);
        when(calculationUnitRepository.save(any(CalculationUnit.class))).thenThrow(new RuntimeException("Database error"));

        // Act
        Try<CalculationUnit> result = calculationUnitManagement.create(createDto);

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when save calculation unit");
        verify(centralMapper, times(1)).toCalculationUnit(any(CalculationUnitCreateRequestDTO.class));
        verify(calculationUnitRepository, times(1)).save(any(CalculationUnit.class));
        verifyNoMoreInteractions(calculationUnitRepository, centralMapper);
    }

    // Test cho phương thức update
    @Test
    void testUpdate_Success() {
        // Arrange
        CalculationUnit updatedUnit = new CalculationUnit();
        updatedUnit.setId(1L);
        updatedUnit.setName("Updated Unit");

        when(calculationUnitRepository.findById(1L)).thenReturn(Optional.of(testCalculationUnit));
        when(centralMapper.toCalculationUnitUpdate(any(CalculationUnit.class), any(CalculationUnit.class))).thenReturn(updatedUnit);
        when(calculationUnitRepository.save(any(CalculationUnit.class))).thenReturn(updatedUnit);

        // Act
        Try<CalculationUnit> result = calculationUnitManagement.update(updatedUnit);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Updated Unit");
        verify(calculationUnitRepository, times(1)).findById(1L);
        verify(centralMapper, times(1)).toCalculationUnitUpdate(any(CalculationUnit.class), any(CalculationUnit.class));
        verify(calculationUnitRepository, times(1)).save(any(CalculationUnit.class));
        verifyNoMoreInteractions(calculationUnitRepository, centralMapper);
    }

    @Test
    void testUpdate_NotFound() {
        // Arrange
        CalculationUnit updatedUnit = new CalculationUnit();
        updatedUnit.setId(1L);
        updatedUnit.setName("Updated Unit");

        when(calculationUnitRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Try<CalculationUnit> result = calculationUnitManagement.update(updatedUnit);

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when find calculation unit to update with id: 1");
        verify(calculationUnitRepository, times(1)).findById(1L);
        verify(centralMapper, never()).toCalculationUnitUpdate(any(CalculationUnit.class), any(CalculationUnit.class));
        verify(calculationUnitRepository, never()).save(any(CalculationUnit.class));
        verifyNoMoreInteractions(calculationUnitRepository, centralMapper);
    }

    @Test
    void testUpdate_Fail_SaveError() {
        // Arrange
        CalculationUnit updatedUnit = new CalculationUnit();
        updatedUnit.setId(1L);
        updatedUnit.setName("Updated Unit");

        when(calculationUnitRepository.findById(1L)).thenReturn(Optional.of(testCalculationUnit));
        when(centralMapper.toCalculationUnitUpdate(any(CalculationUnit.class), any(CalculationUnit.class))).thenReturn(updatedUnit);
        when(calculationUnitRepository.save(any(CalculationUnit.class))).thenThrow(new RuntimeException("Database error"));

        // Act
        Try<CalculationUnit> result = calculationUnitManagement.update(updatedUnit);

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when update calculation unit");
        verify(calculationUnitRepository, times(1)).findById(1L);
        verify(centralMapper, times(1)).toCalculationUnitUpdate(any(CalculationUnit.class), any(CalculationUnit.class));
        verify(calculationUnitRepository, times(1)).save(any(CalculationUnit.class));
        verifyNoMoreInteractions(calculationUnitRepository, centralMapper);
    }

    // Test cho phương thức delete
    @Test
    void testDelete_Success() {
        // Arrange
        doNothing().when(calculationUnitRepository).deleteById(1L);

        // Act
        Try<Boolean> result = calculationUnitManagement.delete(1L);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(calculationUnitRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(calculationUnitRepository, centralMapper);
    }

    @Test
    void testDelete_Failure() {
        // Arrange
        doThrow(new RuntimeException("Delete failed")).when(calculationUnitRepository).deleteById(1L);

        // Act
        Try<Boolean> result = calculationUnitManagement.delete(1L);

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when delete calculation unit with id: 1");
        verify(calculationUnitRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(calculationUnitRepository, centralMapper);
    }

    // Test cho phương thức fetchAll
    @Test
    void testFetchAll_Success() {
        // Arrange
        CalculationUnit unit2 = new CalculationUnit();
        unit2.setId(2L);
        unit2.setName("Unit 2");
        List<CalculationUnit> units = Arrays.asList(testCalculationUnit, unit2);
        when(calculationUnitRepository.findAll()).thenReturn(units);

        // Act
        List<CalculationUnit> result = calculationUnitManagement.fetchAll();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        verify(calculationUnitRepository, times(1)).findAll();
        verifyNoMoreInteractions(calculationUnitRepository, centralMapper);
    }

    @Test
    void testFetchAll_Empty() {
        // Arrange
        when(calculationUnitRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<CalculationUnit> result = calculationUnitManagement.fetchAll();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(calculationUnitRepository, times(1)).findAll();
        verifyNoMoreInteractions(calculationUnitRepository, centralMapper);
    }

    @Test
    void testFetchAll_Failure() {
        // Arrange
        when(calculationUnitRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> calculationUnitManagement.fetchAll(), "Database error");
        verify(calculationUnitRepository, times(1)).findAll();
        verifyNoMoreInteractions(calculationUnitRepository, centralMapper);
    }
}