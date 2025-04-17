package com.ptit.asset.service.manager.impl;

// Import các package cần thiết
import com.ptit.asset.dto.request.FetchPagePlaceRequestDTO;
import com.ptit.asset.dto.request.PlaceCreateRequestDTO;
import com.ptit.asset.dto.request.PlaceUpdateRequestDTO;
import com.ptit.asset.domain.Campus;
import com.ptit.asset.domain.Department;
import com.ptit.asset.domain.Place;
import com.ptit.asset.domain.TypePlace;
import com.ptit.asset.repository.CampusRepository;
import com.ptit.asset.repository.DepartmentRepository;
import com.ptit.asset.repository.PlaceRepository;
import com.ptit.asset.repository.TypePlaceRepository;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Class test cho PlaceManagementImpl, kiểm tra các chức năng quản lý vị trí (Place).
 */
@ExtendWith(MockitoExtension.class)
class PlaceManagementImplTest {

    // Mock các repository và mapper để giả lập hành vi
    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private TypePlaceRepository typePlaceRepository;

    @Mock
    private CampusRepository campusRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private CentralMapper centralMapper;

    // Inject các mock vào đối tượng PlaceManagementImpl
    @InjectMocks
    private PlaceManagementImpl placeManagement;

    private Place testPlace;
    private PlaceCreateRequestDTO createDto;

    /**
     * Phương thức setup chạy trước mỗi test case.
     * Khởi tạo các mock và dữ liệu cơ bản để sử dụng trong các test.
     */
    @BeforeEach
    void setUp() {
        // Khởi tạo các mock bằng MockitoAnnotations
        MockitoAnnotations.initMocks(this);

        // Thiết lập dữ liệu cơ bản cho Place
        testPlace = new Place();
        testPlace.setId(1L);

        TypePlace typePlace = new TypePlace();
        typePlace.setId(1L);
        testPlace.setTypePlace(typePlace);

        Campus campus = new Campus();
        campus.setId(1L);
        testPlace.setCampus(campus);

        Department department = new Department();
        department.setId(1L);
        testPlace.setDepartment(department);

        // Thiết lập dữ liệu cơ bản cho PlaceCreateRequestDTO
        createDto = new PlaceCreateRequestDTO();
        PlaceCreateRequestDTO.Embedded embedded = new PlaceCreateRequestDTO.Embedded();
        embedded.setTypePlaceId(1L);
        embedded.setCampusId(1L);
        embedded.setDepartmentId(1L);
        createDto.setEmbedded(embedded);
    }

    /**
     * Test case kiểm tra chức năng tạo mới Place thành công.
     */
    @Test
    void testCreatePlace_Success() {
        // Arrange: Chuẩn bị dữ liệu cho test
        when(typePlaceRepository.findById(1L)).thenReturn(Optional.of(testPlace.getTypePlace()));
        when(campusRepository.findById(1L)).thenReturn(Optional.of(testPlace.getCampus()));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(testPlace.getDepartment()));
        when(centralMapper.toPlace(any(PlaceCreateRequestDTO.class), any(TypePlace.class), any(Campus.class), any(Department.class)))
                .thenReturn(testPlace);
        when(placeRepository.save(any(Place.class))).thenReturn(testPlace);

        // Act: Gọi phương thức create
        Try<Place> result = placeManagement.create(createDto);

        // Assert: Kiểm tra kết quả
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getTypePlace().getId()).isEqualTo(1L);
        assertThat(result.get().getCampus().getId()).isEqualTo(1L);
        assertThat(result.get().getDepartment().getId()).isEqualTo(1L);
        verify(typePlaceRepository, times(1)).findById(1L);
        verify(campusRepository, times(1)).findById(1L);
        verify(departmentRepository, times(1)).findById(1L);
        verify(placeRepository, times(1)).save(any(Place.class));
        verifyNoMoreInteractions(typePlaceRepository, campusRepository, departmentRepository, placeRepository, centralMapper);
    }

    @Test
    void testCreatePlace_TypePlaceNotFound() {
        // Arrange: Chuẩn bị dữ liệu cho test
        when(typePlaceRepository.findById(1L)).thenReturn(Optional.empty());

        // Act: Gọi phương thức create
        Try<Place> result = placeManagement.create(createDto);

        // Assert: Kiểm tra kết quả
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when find typePlace by id: 1");
        verify(typePlaceRepository, times(1)).findById(1L);
        verify(campusRepository, never()).findById(anyLong());
        verify(departmentRepository, never()).findById(anyLong());
        verify(placeRepository, never()).save(any(Place.class));
        verifyNoMoreInteractions(typePlaceRepository, campusRepository, departmentRepository, placeRepository, centralMapper);
    }

    /**
     * Test case kiểm tra chức năng tạo mới Place thất bại khi không tìm thấy Campus.
     */
    @Test
    void testCreatePlace_CampusNotFound() {
        // Arrange: Chuẩn bị dữ liệu cho test
        when(typePlaceRepository.findById(1L)).thenReturn(Optional.of(testPlace.getTypePlace()));
        when(campusRepository.findById(1L)).thenReturn(Optional.empty());

        // Act: Gọi phương thức create
        Try<Place> result = placeManagement.create(createDto);

        // Assert: Kiểm tra kết quả
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when find campus by id: 1");
        verify(typePlaceRepository, times(1)).findById(1L);
        verify(campusRepository, times(1)).findById(1L);
        verify(departmentRepository, never()).findById(anyLong());
        verify(placeRepository, never()).save(any(Place.class));
        verifyNoMoreInteractions(typePlaceRepository, campusRepository, departmentRepository, placeRepository, centralMapper);
    }

    /**
     * Test case kiểm tra chức năng tạo mới Place khi không tìm thấy Department.
     */
    @Test
    void testCreatePlace_DepartmentNotFound() {
        // Arrange: Chuẩn bị dữ liệu cho test
        when(typePlaceRepository.findById(1L)).thenReturn(Optional.of(testPlace.getTypePlace()));
        when(campusRepository.findById(1L)).thenReturn(Optional.of(testPlace.getCampus()));
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Gọi phương thức create và mong đợi ngoại lệ NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> {
            placeManagement.create(createDto);
        }, "Phải ném NoSuchElementException vì Department không tồn tại và code gốc không kiểm tra Optional");

        // Verify: Kiểm tra các tương tác
        verify(typePlaceRepository, times(1)).findById(1L);
        verify(campusRepository, times(1)).findById(1L);
        verify(departmentRepository, times(1)).findById(1L);
        verify(placeRepository, never()).save(any(Place.class));
        verifyNoMoreInteractions(typePlaceRepository, campusRepository, departmentRepository, placeRepository, centralMapper);
    }

    /**
     * Test case kiểm tra chức năng tạo mới Place khi departmentId là null.
     */
    @Test
    void testCreatePlace_DepartmentIdNull() {
        // Arrange: Chuẩn bị dữ liệu cho test
        createDto.getEmbedded().setDepartmentId(null); // Đặt departmentId là null

        when(typePlaceRepository.findById(1L)).thenReturn(Optional.of(testPlace.getTypePlace()));
        when(campusRepository.findById(1L)).thenReturn(Optional.of(testPlace.getCampus()));
        when(centralMapper.toPlace(any(PlaceCreateRequestDTO.class), any(TypePlace.class), any(Campus.class), eq(null)))
                .thenReturn(testPlace);
        when(placeRepository.save(any(Place.class))).thenReturn(testPlace);

        // Act: Gọi phương thức create
        Try<Place> result = placeManagement.create(createDto);

        // Assert: Kiểm tra kết quả
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getTypePlace().getId()).isEqualTo(1L);
        assertThat(result.get().getCampus().getId()).isEqualTo(1L);
        assertThat(result.get().getDepartment()).isNull();
        verify(typePlaceRepository, times(1)).findById(1L);
        verify(campusRepository, times(1)).findById(1L);
        verify(departmentRepository, never()).findById(anyLong());
        verify(placeRepository, times(1)).save(any(Place.class));
        verifyNoMoreInteractions(typePlaceRepository, campusRepository, departmentRepository, placeRepository, centralMapper);
    }

    /**
     * Test case kiểm tra chức năng tạo mới Place khi embedded là null, để công cụ coverage nhận diện nhánh rõ ràng.
     */
    @Test
    void testCreatePlace_EmbeddedNull() {
        // Arrange: Chuẩn bị dữ liệu cho test
        createDto.setEmbedded(null);

        // Act & Assert: Gọi phương thức create và mong đợi ngoại lệ NullPointerException
        assertThrows(NullPointerException.class, () -> {
            placeManagement.create(createDto);
        }, "Phải ném NullPointerException vì embedded là null");

        // Verify: Kiểm tra các tương tác
        verifyNoInteractions(typePlaceRepository, campusRepository, departmentRepository, placeRepository, centralMapper);
    }

    /**
     * Test case kiểm tra chức năng cập nhật Place thành công với các trường hợp:
     * - embedded không null
     * - typePlaceId khác với ID hiện tại và TypePlace tồn tại
     * - campusId khác với ID hiện tại và Campus tồn tại
     * - departmentId không null và Department tồn tại
     */
    @Test
    void testUpdatePlace_Success() {
        // Arrange: Chuẩn bị dữ liệu cho test
        PlaceUpdateRequestDTO dto = new PlaceUpdateRequestDTO();
        dto.setId(1L);
        PlaceUpdateRequestDTO.Embedded embedded = new PlaceUpdateRequestDTO.Embedded();
        embedded.setTypePlaceId(2L);
        embedded.setCampusId(2L);
        embedded.setDepartmentId(2L);
        dto.setEmbedded(embedded);

        Place existingPlace = new Place();
        existingPlace.setId(1L);
        TypePlace oldTypePlace = new TypePlace();
        oldTypePlace.setId(1L);
        Campus oldCampus = new Campus();
        oldCampus.setId(1L);
        existingPlace.setTypePlace(oldTypePlace);
        existingPlace.setCampus(oldCampus);

        TypePlace newTypePlace = new TypePlace();
        newTypePlace.setId(2L);
        Campus newCampus = new Campus();
        newCampus.setId(2L);
        Department newDepartment = new Department();
        newDepartment.setId(2L);

        Place updatedPlace = new Place();
        updatedPlace.setId(1L);
        updatedPlace.setTypePlace(oldTypePlace);
        updatedPlace.setCampus(oldCampus);
        updatedPlace.setDepartment(null);

        Place finalPlace = new Place();
        finalPlace.setId(1L);
        finalPlace.setTypePlace(newTypePlace);
        finalPlace.setCampus(newCampus);
        finalPlace.setDepartment(newDepartment);

        when(placeRepository.findById(1L)).thenReturn(Optional.of(existingPlace));
        when(centralMapper.toPlaceUpdate(any(Place.class), any(PlaceUpdateRequestDTO.class))).thenReturn(updatedPlace);
        when(typePlaceRepository.findById(2L)).thenReturn(Optional.of(newTypePlace));
        when(campusRepository.findById(2L)).thenReturn(Optional.of(newCampus));
        when(departmentRepository.findById(2L)).thenReturn(Optional.of(newDepartment));
        when(placeRepository.save(any(Place.class))).thenReturn(finalPlace);

        // Act: Gọi phương thức update
        Try<Place> result = placeManagement.update(dto);

        // Assert: Kiểm tra kết quả
        assertThat(result.isSuccess()).isTrue();
        Place returnedPlace = result.get();
        assertThat(returnedPlace).isNotNull();
        assertThat(returnedPlace.getTypePlace()).isNotNull();
        assertThat(returnedPlace.getCampus()).isNotNull();
        assertThat(returnedPlace.getDepartment()).isNotNull();
        assertThat(returnedPlace.getTypePlace().getId()).isEqualTo(2L);
        assertThat(returnedPlace.getCampus().getId()).isEqualTo(2L);
        assertThat(returnedPlace.getDepartment().getId()).isEqualTo(2L);
        verify(placeRepository, times(1)).findById(1L);
        verify(typePlaceRepository, times(1)).findById(2L);
        verify(campusRepository, times(1)).findById(2L);
        verify(departmentRepository, times(1)).findById(2L);
        verify(placeRepository, times(1)).save(any(Place.class));
        verifyNoMoreInteractions(typePlaceRepository, campusRepository, departmentRepository, placeRepository, centralMapper);
    }

    /**
     * Test case kiểm tra chức năng cập nhật Place thất bại khi không tìm thấy Place.
     */
    @Test
    void testUpdatePlace_NotFound() {
        // Arrange: Chuẩn bị dữ liệu cho test
        PlaceUpdateRequestDTO dto = new PlaceUpdateRequestDTO();
        dto.setId(1L);

        when(placeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act: Gọi phương thức update
        Try<Place> result = placeManagement.update(dto);

        // Assert: Kiểm tra kết quả
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when find place to update with id: 1");
        verify(placeRepository, times(1)).findById(1L);
        verify(typePlaceRepository, never()).findById(anyLong());
        verify(campusRepository, never()).findById(anyLong());
        verify(departmentRepository, never()).findById(anyLong());
        verify(placeRepository, never()).save(any(Place.class));
        verifyNoMoreInteractions(typePlaceRepository, campusRepository, departmentRepository, placeRepository, centralMapper);
    }

    /**
     * Test case kiểm tra chức năng cập nhật Place khi typePlaceId và campusId đều là null.
     * Đảm bảo bao phủ nhánh typePlaceId == null và campusId == null trong phương thức update.
     */
    @Test
    void testUpdatePlace_TypePlaceIdAndCampusIdNull() {
        // Arrange: Chuẩn bị dữ liệu cho test
        PlaceUpdateRequestDTO dto = new PlaceUpdateRequestDTO();
        dto.setId(1L);
        PlaceUpdateRequestDTO.Embedded embedded = new PlaceUpdateRequestDTO.Embedded();
        embedded.setTypePlaceId(null); // Đặt typePlaceId là null
        embedded.setCampusId(null);    // Đặt campusId là null
        embedded.setDepartmentId(null);
        dto.setEmbedded(embedded);

        Place existingPlace = new Place();
        existingPlace.setId(1L);
        TypePlace oldTypePlace = new TypePlace();
        oldTypePlace.setId(1L);
        Campus oldCampus = new Campus();
        oldCampus.setId(1L);
        existingPlace.setTypePlace(oldTypePlace);
        existingPlace.setCampus(oldCampus);

        when(placeRepository.findById(1L)).thenReturn(Optional.of(existingPlace));
        when(centralMapper.toPlaceUpdate(any(Place.class), any(PlaceUpdateRequestDTO.class))).thenReturn(existingPlace);
        when(placeRepository.save(any(Place.class))).thenReturn(existingPlace);

        // Act: Gọi phương thức update
        Try<Place> result = placeManagement.update(dto);

        // Assert: Kiểm tra kết quả
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getTypePlace().getId()).isEqualTo(1L); // TypePlace không thay đổi
        assertThat(result.get().getCampus().getId()).isEqualTo(1L);    // Campus không thay đổi
        assertThat(result.get().getDepartment()).isNull();
        verify(placeRepository, times(1)).findById(1L);
        verify(typePlaceRepository, never()).findById(anyLong()); // Đảm bảo không gọi typePlaceRepository
        verify(campusRepository, never()).findById(anyLong());    // Đảm bảo không gọi campusRepository
        verify(departmentRepository, never()).findById(anyLong());
        verify(placeRepository, times(1)).save(any(Place.class));
        verifyNoMoreInteractions(typePlaceRepository, campusRepository, departmentRepository, placeRepository, centralMapper);
    }
    /**
     * Test case kiểm tra chức năng cập nhật Place khi không cập nhật quan hệ (embedded == null).
     */
    @Test
    void testUpdatePlace_EmbeddedNull() {
        // Arrange: Chuẩn bị dữ liệu cho test
        PlaceUpdateRequestDTO dto = new PlaceUpdateRequestDTO();
        dto.setId(1L);
        dto.setEmbedded(null);

        Place existingPlace = new Place();
        existingPlace.setId(1L);
        TypePlace oldTypePlace = new TypePlace();
        oldTypePlace.setId(1L);
        Campus oldCampus = new Campus();
        oldCampus.setId(1L);
        existingPlace.setTypePlace(oldTypePlace);
        existingPlace.setCampus(oldCampus);

        when(placeRepository.findById(1L)).thenReturn(Optional.of(existingPlace));
        when(centralMapper.toPlaceUpdate(any(Place.class), any(PlaceUpdateRequestDTO.class))).thenReturn(existingPlace);
        when(placeRepository.save(any(Place.class))).thenReturn(existingPlace);

        // Act: Gọi phương thức update
        Try<Place> result = placeManagement.update(dto);

        // Assert: Kiểm tra kết quả
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getTypePlace().getId()).isEqualTo(1L);
        assertThat(result.get().getCampus().getId()).isEqualTo(1L);
        assertThat(result.get().getDepartment()).isNull();
        verify(placeRepository, times(1)).findById(1L);
        verify(typePlaceRepository, never()).findById(anyLong());
        verify(campusRepository, never()).findById(anyLong());
        verify(departmentRepository, never()).findById(anyLong());
        verify(placeRepository, times(1)).save(any(Place.class));
        verifyNoMoreInteractions(typePlaceRepository, campusRepository, departmentRepository, placeRepository, centralMapper);
    }

    /**
     * Test case kiểm tra chức năng cập nhật Place khi không tìm thấy TypePlace mới.
     */
    @Test
    void testUpdatePlace_TypePlaceNotFound() {
        // Arrange: Chuẩn bị dữ liệu cho test
        PlaceUpdateRequestDTO dto = new PlaceUpdateRequestDTO();
        dto.setId(1L);
        PlaceUpdateRequestDTO.Embedded embedded = new PlaceUpdateRequestDTO.Embedded();
        embedded.setTypePlaceId(2L);
        embedded.setCampusId(1L);
        embedded.setDepartmentId(null);
        dto.setEmbedded(embedded);

        Place existingPlace = new Place();
        existingPlace.setId(1L);
        TypePlace oldTypePlace = new TypePlace();
        oldTypePlace.setId(1L);
        Campus oldCampus = new Campus();
        oldCampus.setId(1L);
        existingPlace.setTypePlace(oldTypePlace);
        existingPlace.setCampus(oldCampus);

        when(placeRepository.findById(1L)).thenReturn(Optional.of(existingPlace));
        when(typePlaceRepository.findById(2L)).thenReturn(Optional.empty());
        when(centralMapper.toPlaceUpdate(any(Place.class), any(PlaceUpdateRequestDTO.class))).thenReturn(existingPlace);
        when(placeRepository.save(any(Place.class))).thenReturn(existingPlace);

        // Act: Gọi phương thức update
        Try<Place> result = placeManagement.update(dto);

        // Assert: Kiểm tra kết quả
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getTypePlace().getId()).isEqualTo(1L);
        assertThat(result.get().getCampus().getId()).isEqualTo(1L);
        assertThat(result.get().getDepartment()).isNull();
        verify(placeRepository, times(1)).findById(1L);
        verify(typePlaceRepository, times(1)).findById(2L);
        verify(campusRepository, never()).findById(anyLong());
        verify(departmentRepository, never()).findById(anyLong());
        verify(placeRepository, times(1)).save(any(Place.class));
        verifyNoMoreInteractions(typePlaceRepository, campusRepository, departmentRepository, placeRepository, centralMapper);
    }

    /**
     * Test case kiểm tra chức năng cập nhật Place khi không tìm thấy Campus mới.
     */
    @Test
    void testUpdatePlace_CampusNotFound() {
        // Arrange: Chuẩn bị dữ liệu cho test
        PlaceUpdateRequestDTO dto = new PlaceUpdateRequestDTO();
        dto.setId(1L);
        PlaceUpdateRequestDTO.Embedded embedded = new PlaceUpdateRequestDTO.Embedded();
        embedded.setTypePlaceId(1L);
        embedded.setCampusId(2L);
        embedded.setDepartmentId(null);
        dto.setEmbedded(embedded);

        Place existingPlace = new Place();
        existingPlace.setId(1L);
        TypePlace oldTypePlace = new TypePlace();
        oldTypePlace.setId(1L);
        Campus oldCampus = new Campus();
        oldCampus.setId(1L);
        existingPlace.setTypePlace(oldTypePlace);
        existingPlace.setCampus(oldCampus);

        when(placeRepository.findById(1L)).thenReturn(Optional.of(existingPlace));
        when(campusRepository.findById(2L)).thenReturn(Optional.empty());
        when(centralMapper.toPlaceUpdate(any(Place.class), any(PlaceUpdateRequestDTO.class))).thenReturn(existingPlace);
        when(placeRepository.save(any(Place.class))).thenReturn(existingPlace);

        // Act: Gọi phương thức update
        Try<Place> result = placeManagement.update(dto);

        // Assert: Kiểm tra kết quả
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getTypePlace().getId()).isEqualTo(1L);
        assertThat(result.get().getCampus().getId()).isEqualTo(1L);
        assertThat(result.get().getDepartment()).isNull();
        verify(placeRepository, times(1)).findById(1L);
        verify(typePlaceRepository, never()).findById(anyLong());
        verify(campusRepository, times(1)).findById(2L);
        verify(departmentRepository, never()).findById(anyLong());
        verify(placeRepository, times(1)).save(any(Place.class));
        verifyNoMoreInteractions(typePlaceRepository, campusRepository, departmentRepository, placeRepository, centralMapper);
    }

    /**
     * Test case kiểm tra chức năng cập nhật Place khi không tìm thấy Department mới.
     */
    @Test
    void testUpdatePlace_DepartmentNotFound() {
        // Arrange: Chuẩn bị dữ liệu cho test
        PlaceUpdateRequestDTO dto = new PlaceUpdateRequestDTO();
        dto.setId(1L);
        PlaceUpdateRequestDTO.Embedded embedded = new PlaceUpdateRequestDTO.Embedded();
        embedded.setTypePlaceId(1L);
        embedded.setCampusId(1L);
        embedded.setDepartmentId(2L);
        dto.setEmbedded(embedded);

        Place existingPlace = new Place();
        existingPlace.setId(1L);
        TypePlace oldTypePlace = new TypePlace();
        oldTypePlace.setId(1L);
        Campus oldCampus = new Campus();
        oldCampus.setId(1L);
        existingPlace.setTypePlace(oldTypePlace);
        existingPlace.setCampus(oldCampus);

        when(placeRepository.findById(1L)).thenReturn(Optional.of(existingPlace));
        when(departmentRepository.findById(2L)).thenReturn(Optional.empty());
        when(centralMapper.toPlaceUpdate(any(Place.class), any(PlaceUpdateRequestDTO.class))).thenReturn(existingPlace);
        when(placeRepository.save(any(Place.class))).thenReturn(existingPlace);

        // Act: Gọi phương thức update
        Try<Place> result = placeManagement.update(dto);

        // Assert: Kiểm tra kết quả
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getTypePlace().getId()).isEqualTo(1L);
        assertThat(result.get().getCampus().getId()).isEqualTo(1L);
        assertThat(result.get().getDepartment()).isNull();
        verify(placeRepository, times(1)).findById(1L);
        verify(typePlaceRepository, never()).findById(anyLong());
        verify(campusRepository, never()).findById(anyLong());
        verify(departmentRepository, times(1)).findById(2L);
        verify(placeRepository, times(1)).save(any(Place.class));
        verifyNoMoreInteractions(typePlaceRepository, campusRepository, departmentRepository, placeRepository, centralMapper);
    }

    /**
     * Test case kiểm tra chức năng xóa Place thành công.
     */
    @Test
    void testDeletePlace_Success() {
        // Arrange: Chuẩn bị dữ liệu cho test
        Long id = 1L;
        doNothing().when(placeRepository).deleteById(id);

        // Act: Gọi phương thức delete
        Try<Boolean> result = placeManagement.delete(id);

        // Assert: Kiểm tra kết quả
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(placeRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(placeRepository);
    }

    /**
     * Test case kiểm tra chức năng xóa Place thất bại khi có lỗi xảy ra.
     */
    @Test
    void testDeletePlace_Failure() {
        // Arrange: Chuẩn bị dữ liệu cho test
        Long id = 1L;
        doThrow(new RuntimeException("Delete failed")).when(placeRepository).deleteById(id);

        // Act: Gọi phương thức delete
        Try<Boolean> result = placeManagement.delete(id);

        // Assert: Kiểm tra kết quả
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when delete place by id: 1");
        verify(placeRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(placeRepository);
    }

    /**
     * Test case kiểm tra chức năng lấy một Place theo ID thành công.
     */
    @Test
    void testGetOnePlace_Success() {
        // Arrange: Chuẩn bị dữ liệu cho test
        Long id = 1L;
        when(placeRepository.findById(id)).thenReturn(Optional.of(testPlace));

        // Act: Gọi phương thức getOne
        Try<Place> result = placeManagement.getOne(id);

        // Assert: Kiểm tra kết quả
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getId()).isEqualTo(id);
        verify(placeRepository, times(1)).findById(id);
        verifyNoMoreInteractions(placeRepository);
    }

    /**
     * Test case kiểm tra chức năng lấy một Place theo ID thất bại khi không tìm thấy.
     */
    @Test
    void testGetOnePlace_NotFound() {
        // Arrange: Chuẩn bị dữ liệu cho test
        Long id = 1L;
        when(placeRepository.findById(id)).thenReturn(Optional.empty());

        // Act: Gọi phương thức getOne
        Try<Place> result = placeManagement.getOne(id);

        // Assert: Kiểm tra kết quả
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when get place by id: 1");
        verify(placeRepository, times(1)).findById(id);
        verifyNoMoreInteractions(placeRepository);
    }

    /**
     * Test case kiểm tra chức năng lấy tất cả Place thành công (danh sách không rỗng).
     */
    @Test
    void testFetchAllPlaces_Success() {
        // Arrange: Chuẩn bị dữ liệu cho test
        List<Place> places = Arrays.asList(testPlace, new Place());
        when(placeRepository.findAll()).thenReturn(places);

        // Act: Gọi phương thức fetchAll
        List<Place> result = placeManagement.fetchAll();

        // Assert: Kiểm tra kết quả
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(placeRepository, times(1)).findAll();
        verifyNoMoreInteractions(placeRepository);
    }

    /**
     * Test case kiểm tra chức năng lấy tất cả Place thành công (danh sách rỗng).
     */
    @Test
    void testFetchAllPlaces_Empty() {
        // Arrange: Chuẩn bị dữ liệu cho test
        when(placeRepository.findAll()).thenReturn(Collections.emptyList());

        // Act: Gọi phương thức fetchAll
        List<Place> result = placeManagement.fetchAll();

        // Assert: Kiểm tra kết quả
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(placeRepository, times(1)).findAll();
        verifyNoMoreInteractions(placeRepository);
    }

    /**
     * Test case kiểm tra chức năng lấy tất cả Place thất bại khi repository ném lỗi.
     */
    @Test
    void testFetchAllPlaces_Failure() {
        // Arrange: Chuẩn bị dữ liệu cho test
        when(placeRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act: Gọi phương thức fetchAll và kiểm tra ngoại lệ
        try {
            placeManagement.fetchAll();
            fail("Phải ném RuntimeException");
        } catch (RuntimeException e) {
            // Assert: Kiểm tra kết quả
            assertThat(e.getMessage()).isEqualTo("Database error");
        }

        // Xác minh phương thức findAll được gọi
        verify(placeRepository, times(1)).findAll();
        verifyNoMoreInteractions(placeRepository);
    }

    /**
     * Test case kiểm tra chức năng lấy danh sách Place theo phân trang thành công (trang không rỗng).
     */
    @Test
    void testFetchPagePlaces_Success() {
        // Arrange: Chuẩn bị dữ liệu cho test
        FetchPagePlaceRequestDTO dto = new FetchPagePlaceRequestDTO();
        dto.setPage(0);
        dto.setSize(10);

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
        List<Place> places = Arrays.asList(testPlace, new Place());
        Page<Place> page = new PageImpl<>(places, pageable, places.size());

        when(placeRepository.findAll(pageable)).thenReturn(page);

        // Act: Gọi phương thức fetchPage
        Page<Place> result = placeManagement.fetchPage(dto);

        // Assert: Kiểm tra kết quả
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        verify(placeRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(placeRepository);
    }

    /**
     * Test case kiểm tra chức năng lấy danh sách Place theo phân trang thành công (trang rỗng).
     */
    @Test
    void testFetchPagePlaces_Empty() {
        // Arrange: Chuẩn bị dữ liệu cho test
        FetchPagePlaceRequestDTO dto = new FetchPagePlaceRequestDTO();
        dto.setPage(0);
        dto.setSize(10);

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
        Page<Place> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(placeRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act: Gọi phương thức fetchPage
        Page<Place> result = placeManagement.fetchPage(dto);

        // Assert: Kiểm tra kết quả
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        verify(placeRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(placeRepository);
    }

    /**
     * Test case kiểm tra chức năng lấy danh sách Place theo phân trang thất bại khi repository ném lỗi.
     */
    @Test
    void testFetchPagePlaces_Failure() {
        // Arrange: Chuẩn bị dữ liệu cho test
        FetchPagePlaceRequestDTO dto = new FetchPagePlaceRequestDTO();
        dto.setPage(0);
        dto.setSize(10);

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
        when(placeRepository.findAll(pageable)).thenThrow(new RuntimeException("Database error during pagination"));

        // Act: Gọi phương thức fetchPage và kiểm tra ngoại lệ
        try {
            placeManagement.fetchPage(dto);
            fail("Phải ném RuntimeException");
        } catch (RuntimeException e) {
            // Assert: Kiểm tra kết quả
            assertThat(e.getMessage()).isEqualTo("Database error during pagination");
        }

        // Xác minh phương thức findAll được gọi
        verify(placeRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(placeRepository);
    }
}