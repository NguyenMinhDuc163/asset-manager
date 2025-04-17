package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.TypePlace;
import com.ptit.asset.dto.request.TypePlaceCreateRequestDTO;
import com.ptit.asset.repository.TypePlaceRepository;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TypePlaceManagementImplTest {

    @Mock
    private TypePlaceRepository typePlaceRepository;

    @Mock
    private CentralMapper centralMapper;

    @InjectMocks
    private TypePlaceManagementImpl typePlaceManagement;

    private TypePlace testTypePlace;
    private TypePlaceCreateRequestDTO createDto;

    @BeforeEach
    void setUp() {
        testTypePlace = new TypePlace();
        testTypePlace.setId(1L);
        testTypePlace.setName("Test Type Place");

        createDto = new TypePlaceCreateRequestDTO();
        createDto.setName("Test Type Place");
    }

    // Test create
    @Test
    void create_success() {
        // Nhánh: Thêm mới TypePlace thành công
        when(centralMapper.toTypePlace(createDto)).thenReturn(testTypePlace);
        when(typePlaceRepository.save(testTypePlace)).thenReturn(testTypePlace);

        Try<TypePlace> result = typePlaceManagement.create(createDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Test Type Place");
        verify(centralMapper, times(1)).toTypePlace(createDto);
        verify(typePlaceRepository, times(1)).save(testTypePlace);
        verifyNoMoreInteractions(typePlaceRepository, centralMapper);
    }

    @Test
    void create_fail_saveError() {
        // Nhánh: Thêm mới TypePlace thất bại do lỗi cơ sở dữ liệu
        when(centralMapper.toTypePlace(createDto)).thenReturn(testTypePlace);
        when(typePlaceRepository.save(testTypePlace)).thenThrow(new RuntimeException("DB error"));

        Try<TypePlace> result = typePlaceManagement.create(createDto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when save type place");
        verify(centralMapper, times(1)).toTypePlace(createDto);
        verify(typePlaceRepository, times(1)).save(testTypePlace);
        verifyNoMoreInteractions(typePlaceRepository, centralMapper);
    }

    // Test update
    @Test
    void update_success() {
        // Nhánh: Cập nhật TypePlace thành công khi tồn tại
        TypePlace updatedTypePlace = new TypePlace();
        updatedTypePlace.setId(1L);
        updatedTypePlace.setName("Updated Type Place");

        when(typePlaceRepository.findById(1L)).thenReturn(Optional.of(testTypePlace));
        when(centralMapper.toTypePlaceUpdate(testTypePlace, updatedTypePlace)).thenReturn(updatedTypePlace);
        when(typePlaceRepository.save(updatedTypePlace)).thenReturn(updatedTypePlace);

        Try<TypePlace> result = typePlaceManagement.update(updatedTypePlace);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Updated Type Place");
        verify(typePlaceRepository, times(1)).findById(1L);
        verify(centralMapper, times(1)).toTypePlaceUpdate(testTypePlace, updatedTypePlace);
        verify(typePlaceRepository, times(1)).save(updatedTypePlace);
        verifyNoMoreInteractions(typePlaceRepository, centralMapper);
    }

    @Test
    void update_fail_notFound() {
        // Nhánh: Cập nhật TypePlace thất bại khi không tồn tại
        TypePlace updatedTypePlace = new TypePlace();
        updatedTypePlace.setId(1L);

        when(typePlaceRepository.findById(1L)).thenReturn(Optional.empty());

        Try<TypePlace> result = typePlaceManagement.update(updatedTypePlace);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find type place to update with id: 1");
        verify(typePlaceRepository, times(1)).findById(1L);
        verifyNoInteractions(centralMapper);
        verify(typePlaceRepository, never()).save(any(TypePlace.class));
        verifyNoMoreInteractions(typePlaceRepository);
    }

    @Test
    void update_fail_saveError() {
        // Nhánh: Cập nhật TypePlace thất bại do lỗi cơ sở dữ liệu
        TypePlace updatedTypePlace = new TypePlace();
        updatedTypePlace.setId(1L);

        when(typePlaceRepository.findById(1L)).thenReturn(Optional.of(testTypePlace));
        when(centralMapper.toTypePlaceUpdate(testTypePlace, updatedTypePlace)).thenReturn(updatedTypePlace);
        when(typePlaceRepository.save(updatedTypePlace)).thenThrow(new RuntimeException("DB error"));

        Try<TypePlace> result = typePlaceManagement.update(updatedTypePlace);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when update type place");
        verify(typePlaceRepository, times(1)).findById(1L);
        verify(centralMapper, times(1)).toTypePlaceUpdate(testTypePlace, updatedTypePlace);
        verify(typePlaceRepository, times(1)).save(updatedTypePlace);
        verifyNoMoreInteractions(typePlaceRepository, centralMapper);
    }

    // Test delete
    @Test
    void delete_success() {
        // Nhánh: Xóa TypePlace thành công
        // Giả định kiểm tra tồn tại tương tự yêu cầu trước
        when(typePlaceRepository.findById(1L)).thenReturn(Optional.of(testTypePlace));
        doNothing().when(typePlaceRepository).deleteById(1L);

        Try<Boolean> result = typePlaceManagement.delete(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(typePlaceRepository, times(1)).findById(1L);
        verify(typePlaceRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(typePlaceRepository);
    }

    @Test
    void delete_fail_notFound() {
        // Nhánh: Xóa TypePlace thất bại khi không tồn tại
        // Giả định kiểm tra tồn tại
        when(typePlaceRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Boolean> result = typePlaceManagement.delete(1L);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find type place with id: 1");
        verify(typePlaceRepository, times(1)).findById(1L);
        verify(typePlaceRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(typePlaceRepository);
    }

    @Test
    void delete_fail_dbError() {
        // Nhánh: Xóa TypePlace thất bại do lỗi cơ sở dữ liệu
        when(typePlaceRepository.findById(1L)).thenReturn(Optional.of(testTypePlace));
        doThrow(new RuntimeException("DB error")).when(typePlaceRepository).deleteById(1L);

        Try<Boolean> result = typePlaceManagement.delete(1L);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when delete type place with id: 1");
        verify(typePlaceRepository, times(1)).findById(1L);
        verify(typePlaceRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(typePlaceRepository);
    }

    // Test getOne
    @Test
    void getOne_success() {
        // Nhánh: Tìm kiếm TypePlace thành công
        when(typePlaceRepository.findById(1L)).thenReturn(Optional.of(testTypePlace));

        Try<TypePlace> result = typePlaceManagement.getOne(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Test Type Place");
        verify(typePlaceRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(typePlaceRepository);
    }

    @Test
    void getOne_fail_notFound() {
        // Nhánh: Tìm kiếm TypePlace thất bại khi không tồn tại
        when(typePlaceRepository.findById(1L)).thenReturn(Optional.empty());

        Try<TypePlace> result = typePlaceManagement.getOne(1L);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when get type place by id: 1");
        verify(typePlaceRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(typePlaceRepository);
    }

    // Test fetchAll
    @Test
    void fetchAll_success() {
        // Nhánh: Lấy danh sách TypePlace thành công, không rỗng
        when(typePlaceRepository.findAll()).thenReturn(Collections.singletonList(testTypePlace));

        List<TypePlace> result = typePlaceManagement.fetchAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("Test Type Place");
        verify(typePlaceRepository, times(1)).findAll();
        verifyNoMoreInteractions(typePlaceRepository);
    }

    @Test
    void fetchAll_empty() {
        // Nhánh: Lấy danh sách TypePlace thành công, rỗng
        when(typePlaceRepository.findAll()).thenReturn(Collections.emptyList());

        List<TypePlace> result = typePlaceManagement.fetchAll();

        assertThat(result).isEmpty();
        verify(typePlaceRepository, times(1)).findAll();
        verifyNoMoreInteractions(typePlaceRepository);
    }
}