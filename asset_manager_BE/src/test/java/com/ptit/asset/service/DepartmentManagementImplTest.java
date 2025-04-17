package com.ptit.asset.service;

import com.ptit.asset.domain.Department;
import com.ptit.asset.dto.request.DepartmentCreateRequestDTO;
import com.ptit.asset.dto.request.FetchPageDepartmentRequestDTO;
import com.ptit.asset.repository.DepartmentRepository;
import com.ptit.asset.service.manager.impl.DepartmentManagementImpl;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentManagementImplTest {

    @Mock
    private CentralMapper centralMapper;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentManagementImpl departmentManagement;

    private Department itDepartment;
    private Department hrDepartment;
    private Department financeDepartment;
    private Department educationDepartment;
    private Department facilitiesDepartment;
    private DepartmentCreateRequestDTO createDTO;

    @BeforeEach
    void setUp() {
        // Khởi tạo các đối tượng test với dữ liệu thực tế của trường học
        itDepartment = new Department();
        itDepartment.setId(1L);
        itDepartment.setName("Phòng CNTT");
        itDepartment.setDescription("Phòng Công nghệ thông tin quản lý hệ thống IT của trường");

        hrDepartment = new Department();
        hrDepartment.setId(2L);
        hrDepartment.setName("Phòng Nhân sự");
        hrDepartment.setDescription("Quản lý nhân sự và tuyển dụng");

        financeDepartment = new Department();
        financeDepartment.setId(3L);
        financeDepartment.setName("Phòng Tài chính");
        financeDepartment.setDescription("Quản lý tài chính và ngân sách");

        educationDepartment = new Department();
        educationDepartment.setId(4L);
        educationDepartment.setName("Phòng Đào tạo");
        educationDepartment.setDescription("Quản lý đào tạo và chương trình giảng dạy");

        facilitiesDepartment = new Department();
        facilitiesDepartment.setId(5L);
        facilitiesDepartment.setName("Phòng Cơ sở vật chất");
        facilitiesDepartment.setDescription("Quản lý cơ sở vật chất và trang thiết bị");

        createDTO = new DepartmentCreateRequestDTO();
        createDTO.setName("Phòng Quản lý Sinh viên");
        createDTO.setDescription("Quản lý hoạt động và đời sống sinh viên");
    }

    @Nested
    @DisplayName("getOne method tests")
    class GetOneTests {

        @Test
        @DisplayName("Trả về department khi tìm thấy ID")
        void getOne_ShouldReturnDepartment_WhenIdExists() {
            // Arrange
            when(departmentRepository.findById(1L)).thenReturn(Optional.of(itDepartment));

            // Act
            Try<Department> result = departmentManagement.getOne(1L);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals(itDepartment, result.get());
            assertEquals("Phòng CNTT", result.get().getName());
            verify(departmentRepository).findById(1L);
        }

        @Test
        @DisplayName("Lấy thông tin Phòng Đào tạo theo ID")
        void getOne_ShouldReturnEducationDepartment_WhenIdIs4() {
            // Arrange
            when(departmentRepository.findById(4L)).thenReturn(Optional.of(educationDepartment));

            // Act
            Try<Department> result = departmentManagement.getOne(4L);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals("Phòng Đào tạo", result.get().getName());
            assertEquals("Quản lý đào tạo và chương trình giảng dạy", result.get().getDescription());
            verify(departmentRepository).findById(4L);
        }

        @Test
        @DisplayName("Trả về failure khi không tìm thấy ID phòng ban")
        void getOne_ShouldReturnFailure_WhenIdNotExists() {
            // Arrange
            when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Try<Department> result = departmentManagement.getOne(999L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when get department by id: 999", result.getCause().getMessage());
            verify(departmentRepository).findById(999L);
        }

        @Test
        @DisplayName("Trả về failure khi ID là số âm")
        void getOne_ShouldReturnFailure_WhenIdIsNegative() {
            // Arrange
            when(departmentRepository.findById(-1L)).thenThrow(new IllegalArgumentException("ID không được là số âm"));

            // Act
            Try<Department> result = departmentManagement.getOne(-1L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when get department by id: -1", result.getCause().getMessage());
            verify(departmentRepository).findById(-1L);
        }
    }

    @Nested
    @DisplayName("create method tests")
    class CreateTests {

        @Test
        @DisplayName("Tạo Phòng Quản lý Sinh viên thành công")
        void create_ShouldCreateStudentAffairsDepartment_WhenValidDTO() {
            // Arrange
            Department studentAffairsDepartment = new Department();
            studentAffairsDepartment.setId(6L);
            studentAffairsDepartment.setName("Phòng Quản lý Sinh viên");
            studentAffairsDepartment.setDescription("Quản lý hoạt động và đời sống sinh viên");

            when(centralMapper.toDepartment(any(DepartmentCreateRequestDTO.class))).thenReturn(studentAffairsDepartment);
            when(departmentRepository.save(any(Department.class))).thenReturn(studentAffairsDepartment);

            // Act
            Try<Department> result = departmentManagement.create(createDTO);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals(studentAffairsDepartment, result.get());
            assertEquals("Phòng Quản lý Sinh viên", result.get().getName());
            verify(centralMapper).toDepartment(createDTO);
            verify(departmentRepository).save(studentAffairsDepartment);
        }

        @Test
        @DisplayName("Tạo phòng ban với tên quá dài (trên 30 ký tự)")
        void create_ShouldReturnFailure_WhenNameTooLong() {
            // Arrange
            DepartmentCreateRequestDTO invalidDTO = new DepartmentCreateRequestDTO();
            invalidDTO.setName("Phòng Quản lý Chất lượng và Kiểm định Chất lượng Giáo dục Đại học"); // > 30 ký tự
            invalidDTO.setDescription("Quản lý chất lượng giáo dục");

            Department invalidDepartment = new Department();
            invalidDepartment.setName(invalidDTO.getName());
            invalidDepartment.setDescription(invalidDTO.getDescription());

            when(centralMapper.toDepartment(any(DepartmentCreateRequestDTO.class))).thenReturn(invalidDepartment);
            when(departmentRepository.save(any(Department.class))).thenThrow(
                    new DataIntegrityViolationException("Value too long for column 'name' SQLCODE=42622"));

            // Act
            Try<Department> result = departmentManagement.create(invalidDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when save department", result.getCause().getMessage());
            verify(centralMapper).toDepartment(invalidDTO);
            verify(departmentRepository).save(invalidDepartment);
        }

        @Test
        @DisplayName("Tạo phòng ban với tên đã tồn tại")
        void create_ShouldReturnFailure_WhenNameAlreadyExists() {
            // Arrange
            DepartmentCreateRequestDTO duplicateDTO = new DepartmentCreateRequestDTO();
            duplicateDTO.setName("Phòng CNTT"); // Tên đã tồn tại
            duplicateDTO.setDescription("Mô tả mới");

            Department duplicateDepartment = new Department();
            duplicateDepartment.setName(duplicateDTO.getName());
            duplicateDepartment.setDescription(duplicateDTO.getDescription());

            when(centralMapper.toDepartment(any(DepartmentCreateRequestDTO.class))).thenReturn(duplicateDepartment);
            when(departmentRepository.save(any(Department.class))).thenThrow(
                    new DataIntegrityViolationException("Unique index or primary key violation"));

            // Act
            Try<Department> result = departmentManagement.create(duplicateDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when save department", result.getCause().getMessage());
            verify(centralMapper).toDepartment(duplicateDTO);
            verify(departmentRepository).save(duplicateDepartment);
        }

        @Test
        @DisplayName("Tạo phòng ban với tên rỗng")
        void create_ShouldReturnFailure_WhenNameIsEmpty() {
            // Arrange
            DepartmentCreateRequestDTO emptyNameDTO = new DepartmentCreateRequestDTO();
            emptyNameDTO.setName("");
            emptyNameDTO.setDescription("Mô tả phòng ban");

            Department invalidDepartment = new Department();
            invalidDepartment.setName("");
            invalidDepartment.setDescription(emptyNameDTO.getDescription());

            when(centralMapper.toDepartment(any(DepartmentCreateRequestDTO.class))).thenReturn(invalidDepartment);
            when(departmentRepository.save(any(Department.class))).thenThrow(
                    new DataIntegrityViolationException("NOT NULL check constraint"));

            // Act
            Try<Department> result = departmentManagement.create(emptyNameDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when save department", result.getCause().getMessage());
            verify(centralMapper).toDepartment(emptyNameDTO);
            verify(departmentRepository).save(invalidDepartment);
        }
    }

    @Nested
    @DisplayName("update method tests")
    class UpdateTests {

        @Test
        @DisplayName("Cập nhật tên và mô tả của Phòng CNTT")
        void update_ShouldUpdateITDepartment_Successfully() {
            // Arrange
            Department updatedITDepartment = new Department();
            updatedITDepartment.setId(1L);
            updatedITDepartment.setName("Phòng Công nghệ Thông tin");
            updatedITDepartment.setDescription("Quản lý và phát triển hệ thống CNTT của trường học");

            when(departmentRepository.findById(1L)).thenReturn(Optional.of(itDepartment));
            when(centralMapper.toDepartmentUpdate(any(Department.class), any(Department.class))).thenReturn(updatedITDepartment);
            when(departmentRepository.save(any(Department.class))).thenReturn(updatedITDepartment);

            // Act
            Try<Department> result = departmentManagement.update(updatedITDepartment);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals("Phòng Công nghệ Thông tin", result.get().getName());
            assertEquals("Quản lý và phát triển hệ thống CNTT của trường học", result.get().getDescription());
            verify(departmentRepository).findById(1L);
            verify(centralMapper).toDepartmentUpdate(itDepartment, updatedITDepartment);
            verify(departmentRepository).save(updatedITDepartment);
        }

        @Test
        @DisplayName("Cập nhật chỉ mô tả của Phòng Tài chính")
        void update_ShouldUpdateOnlyDescription_ForFinanceDepartment() {
            // Arrange
            Department updatedFinanceDepartment = new Department();
            updatedFinanceDepartment.setId(3L);
            updatedFinanceDepartment.setName("Phòng Tài chính"); // Giữ nguyên tên
            updatedFinanceDepartment.setDescription("Quản lý tài chính, ngân sách và thanh toán cho trường học");

            when(departmentRepository.findById(3L)).thenReturn(Optional.of(financeDepartment));
            when(centralMapper.toDepartmentUpdate(any(Department.class), any(Department.class))).thenReturn(updatedFinanceDepartment);
            when(departmentRepository.save(any(Department.class))).thenReturn(updatedFinanceDepartment);

            // Act
            Try<Department> result = departmentManagement.update(updatedFinanceDepartment);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals("Phòng Tài chính", result.get().getName());
            assertEquals("Quản lý tài chính, ngân sách và thanh toán cho trường học", result.get().getDescription());
            verify(departmentRepository).findById(3L);
            verify(centralMapper).toDepartmentUpdate(financeDepartment, updatedFinanceDepartment);
            verify(departmentRepository).save(updatedFinanceDepartment);
        }

        @Test
        @DisplayName("Cập nhật với tên phòng ban đã tồn tại")
        void update_ShouldReturnFailure_WhenUpdatingToExistingName() {
            // Arrange
            Department duplicateNameDepartment = new Department();
            duplicateNameDepartment.setId(2L);
            duplicateNameDepartment.setName("Phòng CNTT"); // Tên đã tồn tại ở phòng khác
            duplicateNameDepartment.setDescription("Mô tả mới");

            when(departmentRepository.findById(2L)).thenReturn(Optional.of(hrDepartment));
            when(centralMapper.toDepartmentUpdate(any(Department.class), any(Department.class))).thenReturn(duplicateNameDepartment);
            when(departmentRepository.save(any(Department.class))).thenThrow(
                    new DataIntegrityViolationException("Unique constraint violation"));

            // Act
            Try<Department> result = departmentManagement.update(duplicateNameDepartment);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when update department", result.getCause().getMessage());
            verify(departmentRepository).findById(2L);
            verify(centralMapper).toDepartmentUpdate(hrDepartment, duplicateNameDepartment);
            verify(departmentRepository).save(duplicateNameDepartment);
        }

        @Test
        @DisplayName("Cập nhật phòng ban không tồn tại")
        void update_ShouldReturnFailure_WhenDepartmentNotExists() {
            // Arrange
            Department nonExistingDepartment = new Department();
            nonExistingDepartment.setId(999L);
            nonExistingDepartment.setName("Phòng Không tồn tại");
            nonExistingDepartment.setDescription("Mô tả phòng không tồn tại");

            when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Try<Department> result = departmentManagement.update(nonExistingDepartment);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when find department to update with id: 999", result.getCause().getMessage());
            verify(departmentRepository).findById(999L);
            verify(centralMapper, never()).toDepartmentUpdate(any(), any());
            verify(departmentRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete method tests")
    class DeleteTests {

        @Test
        @DisplayName("Xóa Phòng Nhân sự thành công")
        void delete_ShouldDeleteHRDepartment_Successfully() {
            // Arrange
            doNothing().when(departmentRepository).deleteById(2L);

            // Act
            Try<Boolean> result = departmentManagement.delete(2L);

            // Assert
            assertTrue(result.isSuccess());
            assertTrue(result.get());
            verify(departmentRepository).deleteById(2L);
        }

        @Test
        @DisplayName("Xóa Phòng Cơ sở vật chất thành công")
        void delete_ShouldDeleteFacilitiesDepartment_Successfully() {
            // Arrange
            doNothing().when(departmentRepository).deleteById(5L);

            // Act
            Try<Boolean> result = departmentManagement.delete(5L);

            // Assert
            assertTrue(result.isSuccess());
            assertTrue(result.get());
            verify(departmentRepository).deleteById(5L);
        }

        @Test
        @DisplayName("Xóa phòng ban không tồn tại")
        void delete_ShouldReturnFailure_WhenDepartmentNotExists() {
            // Arrange
            doThrow(new EmptyResultDataAccessException("No department found with id 999", 1))
                    .when(departmentRepository).deleteById(999L);

            // Act
            Try<Boolean> result = departmentManagement.delete(999L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when delete department with id: 999", result.getCause().getMessage());
            verify(departmentRepository).deleteById(999L);
        }

        @Test
        @DisplayName("Xóa phòng ban đang được sử dụng bởi các tài sản")
        void delete_ShouldReturnFailure_WhenDepartmentInUseByAssets() {
            // Arrange
            doThrow(new DataIntegrityViolationException("Cannot delete department with associated assets"))
                    .when(departmentRepository).deleteById(1L);

            // Act
            Try<Boolean> result = departmentManagement.delete(1L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when delete department with id: 1", result.getCause().getMessage());
            verify(departmentRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Xóa phòng ban đang được sử dụng bởi người dùng")
        void delete_ShouldReturnFailure_WhenDepartmentInUseByUsers() {
            // Arrange
            doThrow(new DataIntegrityViolationException("Cannot delete department with associated users"))
                    .when(departmentRepository).deleteById(4L);

            // Act
            Try<Boolean> result = departmentManagement.delete(4L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when delete department with id: 4", result.getCause().getMessage());
            verify(departmentRepository).deleteById(4L);
        }
    }

    @Nested
    @DisplayName("fetchAll method tests")
    class FetchAllTests {

        @Test
        @DisplayName("Trả về danh sách tất cả các phòng ban của trường")
        void fetchAll_ShouldReturnAllDepartments() {
            // Arrange
            List<Department> departments = Arrays.asList(
                    itDepartment, hrDepartment, financeDepartment,
                    educationDepartment, facilitiesDepartment
            );

            when(departmentRepository.findAll(any(Sort.class))).thenReturn(departments);

            // Act
            List<Department> result = departmentManagement.fetchAll();

            // Assert
            assertEquals(5, result.size());
            assertEquals(departments, result);
            verify(departmentRepository).findAll(Sort.by("name").ascending());
        }

        @Test
        @DisplayName("Trả về danh sách rỗng khi chưa có phòng ban nào")
        void fetchAll_ShouldReturnEmptyList_WhenNoDepartments() {
            // Arrange
            when(departmentRepository.findAll(any(Sort.class))).thenReturn(Collections.emptyList());

            // Act
            List<Department> result = departmentManagement.fetchAll();

            // Assert
            assertTrue(result.isEmpty());
            verify(departmentRepository).findAll(Sort.by("name").ascending());
        }

        @Test
        @DisplayName("Trả về danh sách đã sắp xếp theo tên phòng ban")
        void fetchAll_ShouldReturnSortedList_ByDepartmentName() {
            // Arrange - Tạo danh sách không theo thứ tự
            List<Department> unsortedDepartments = Arrays.asList(
                    hrDepartment, // Phòng Nhân sự
                    itDepartment, // Phòng CNTT
                    financeDepartment, // Phòng Tài chính
                    facilitiesDepartment, // Phòng Cơ sở vật chất
                    educationDepartment // Phòng Đào tạo
            );

            // Danh sách đã sắp xếp theo tên
            List<Department> sortedDepartments = Arrays.asList(
                    facilitiesDepartment, // Phòng Cơ sở vật chất
                    itDepartment, // Phòng CNTT
                    educationDepartment, // Phòng Đào tạo
                    hrDepartment, // Phòng Nhân sự
                    financeDepartment // Phòng Tài chính
            );

            when(departmentRepository.findAll(any(Sort.class))).thenReturn(sortedDepartments);

            // Act
            List<Department> result = departmentManagement.fetchAll();

            // Assert
            assertEquals(5, result.size());
            assertEquals("Phòng Cơ sở vật chất", result.get(0).getName());
            assertEquals("Phòng CNTT", result.get(1).getName());
            assertEquals("Phòng Đào tạo", result.get(2).getName());
            assertEquals("Phòng Nhân sự", result.get(3).getName());
            assertEquals("Phòng Tài chính", result.get(4).getName());
            verify(departmentRepository).findAll(Sort.by("name").ascending());
        }

        @Test
        @DisplayName("Trả về danh sách phòng ban với thông tin đầy đủ")
        void fetchAll_ShouldReturnCompleteInformation() {
            // Arrange
            List<Department> departments = Arrays.asList(itDepartment, hrDepartment);

            when(departmentRepository.findAll(any(Sort.class))).thenReturn(departments);

            // Act
            List<Department> result = departmentManagement.fetchAll();

            // Assert
            assertEquals(2, result.size());

            // Kiểm tra thông tin đầy đủ của phòng ban đầu tiên
            assertEquals(1L, result.get(0).getId());
            assertEquals("Phòng CNTT", result.get(0).getName());
            assertEquals("Phòng Công nghệ thông tin quản lý hệ thống IT của trường", result.get(0).getDescription());

            // Kiểm tra thông tin đầy đủ của phòng ban thứ hai
            assertEquals(2L, result.get(1).getId());
            assertEquals("Phòng Nhân sự", result.get(1).getName());
            assertEquals("Quản lý nhân sự và tuyển dụng", result.get(1).getDescription());

            verify(departmentRepository).findAll(Sort.by("name").ascending());
        }
    }

    @Nested
    @DisplayName("fetchPage method tests")
    class FetchPageTests {

        @Test
        @DisplayName("Trả về trang đầu tiên với 2 phòng ban")
        void fetchPage_ShouldReturnFirstPageWith2Departments() {
            // Arrange
            List<Department> departmentsPage1 = Arrays.asList(facilitiesDepartment, itDepartment);

            Page<Department> page = new PageImpl<>(departmentsPage1, PageRequest.of(0, 2), 5);
            FetchPageDepartmentRequestDTO dto = new FetchPageDepartmentRequestDTO(0, 2);

            when(departmentRepository.findAll(any(Pageable.class))).thenReturn(page);

            // Act
            Page<Department> result = departmentManagement.fetchPage(dto);

            // Assert
            assertEquals(2, result.getContent().size());
            assertEquals(5, result.getTotalElements()); // Tổng số phòng ban
            assertEquals(3, result.getTotalPages()); // Tổng số trang (5/2 = 3 trang)
            assertEquals("Phòng Cơ sở vật chất", result.getContent().get(0).getName());
            assertEquals("Phòng CNTT", result.getContent().get(1).getName());
            verify(departmentRepository).findAll(PageRequest.of(0, 2));
        }

        @Test
        @DisplayName("Trả về trang thứ hai với 2 phòng ban")
        void fetchPage_ShouldReturnSecondPageWith2Departments() {
            // Arrange
            List<Department> departmentsPage2 = Arrays.asList(educationDepartment, hrDepartment);

            Page<Department> page = new PageImpl<>(departmentsPage2, PageRequest.of(1, 2), 5);
            FetchPageDepartmentRequestDTO dto = new FetchPageDepartmentRequestDTO(1, 2);

            when(departmentRepository.findAll(any(Pageable.class))).thenReturn(page);

            // Act
            Page<Department> result = departmentManagement.fetchPage(dto);

            // Assert
            assertEquals(2, result.getContent().size());
            assertEquals(5, result.getTotalElements());
            assertEquals(3, result.getTotalPages());
            assertEquals("Phòng Đào tạo", result.getContent().get(0).getName());
            assertEquals("Phòng Nhân sự", result.getContent().get(1).getName());
            verify(departmentRepository).findAll(PageRequest.of(1, 2));
        }

        @Test
        @DisplayName("Trả về trang cuối cùng với 1 phòng ban")
        void fetchPage_ShouldReturnLastPageWithOneDepartment() {
            // Arrange
            List<Department> departmentsPage3 = Collections.singletonList(financeDepartment);

            Page<Department> page = new PageImpl<>(departmentsPage3, PageRequest.of(2, 2), 5);
            FetchPageDepartmentRequestDTO dto = new FetchPageDepartmentRequestDTO(2, 2);

            when(departmentRepository.findAll(any(Pageable.class))).thenReturn(page);

            // Act
            Page<Department> result = departmentManagement.fetchPage(dto);

            // Assert
            assertEquals(1, result.getContent().size());
            assertEquals(5, result.getTotalElements());
            assertEquals(3, result.getTotalPages());
            assertEquals("Phòng Tài chính", result.getContent().get(0).getName());
            assertTrue(result.isLast());
            verify(departmentRepository).findAll(PageRequest.of(2, 2));
        }

        @Test
        @DisplayName("Trả về trang rỗng khi chỉ số trang vượt quá số trang hiện có")
        void fetchPage_ShouldReturnEmptyPage_WhenPageNumberExceedsTotalPages() {
            // Arrange
            Page<Department> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(10, 2), 5);
            FetchPageDepartmentRequestDTO dto = new FetchPageDepartmentRequestDTO(10, 2);

            when(departmentRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

            // Act
            Page<Department> result = departmentManagement.fetchPage(dto);

            // Assert
            assertTrue(result.getContent().isEmpty());
            assertEquals(5, result.getTotalElements());
            assertEquals(3, result.getTotalPages());
            assertTrue(result.isLast());
            verify(departmentRepository).findAll(PageRequest.of(10, 2));
        }

        @Test
        @DisplayName("Trả về tất cả các phòng ban trong một trang khi kích thước trang lớn")
        void fetchPage_ShouldReturnAllDepartmentsInOnePage_WhenPageSizeIsLarge() {
            // Arrange
            List<Department> allDepartments = Arrays.asList(
                    facilitiesDepartment, itDepartment, educationDepartment,
                    hrDepartment, financeDepartment
            );

            Page<Department> singlePage = new PageImpl<>(allDepartments, PageRequest.of(0, 10), 5);
            FetchPageDepartmentRequestDTO dto = new FetchPageDepartmentRequestDTO(0, 10);

            when(departmentRepository.findAll(any(Pageable.class))).thenReturn(singlePage);

            // Act
            Page<Department> result = departmentManagement.fetchPage(dto);

            // Assert
// Assert
            assertEquals(5, result.getContent().size());
            assertEquals(5, result.getTotalElements());
            assertEquals(1, result.getTotalPages());
            assertTrue(result.isFirst());
            assertTrue(result.isLast());
            verify(departmentRepository).findAll(PageRequest.of(0, 10));
        }

        @Test
        @DisplayName("Kiểm tra phân trang với kích thước trang bằng 1")
        void fetchPage_ShouldPaginateCorrectly_WhenPageSizeIsOne() {
            // Arrange
            List<Department> firstDepartment = Collections.singletonList(facilitiesDepartment);

            Page<Department> page = new PageImpl<>(firstDepartment, PageRequest.of(0, 1), 5);
            FetchPageDepartmentRequestDTO dto = new FetchPageDepartmentRequestDTO(0, 1);

            when(departmentRepository.findAll(any(Pageable.class))).thenReturn(page);

            // Act
            Page<Department> result = departmentManagement.fetchPage(dto);

            // Assert
            assertEquals(1, result.getContent().size());
            assertEquals(5, result.getTotalElements());
            assertEquals(5, result.getTotalPages());
            assertTrue(result.isFirst());
            assertFalse(result.isLast());
            verify(departmentRepository).findAll(PageRequest.of(0, 1));
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Xử lý trường hợp kết nối cơ sở dữ liệu bị gián đoạn")
        void shouldHandleDatabaseConnectionLoss() {
            // Arrange
            when(departmentRepository.findById(anyLong())).thenThrow(
                    new org.springframework.dao.DataAccessResourceFailureException("Could not connect to database"));

            // Act
            Try<Department> result = departmentManagement.getOne(1L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when get department by id: 1", result.getCause().getMessage());
            verify(departmentRepository).findById(1L);
        }

        @Test
        @DisplayName("Xử lý trường hợp trùng lặp khóa chính")
        void shouldHandlePrimaryKeyViolation() {
            // Arrange
            Department existingDepartment = new Department("Phòng mới", "Mô tả phòng mới");
            existingDepartment.setId(1L); // ID đã tồn tại

            when(departmentRepository.save(any(Department.class))).thenThrow(
                    new org.springframework.dao.DuplicateKeyException("Duplicate primary key"));

            // Act
            Try<Department> result = departmentManagement.update(existingDepartment);

            // Assert
            assertTrue(result.isFailure());
            verify(departmentRepository).findById(1L);
        }

        @Test
        @DisplayName("Kiểm tra xử lý ngoại lệ khi thêm quá nhiều phòng ban")
        void shouldHandleLimitExceeded() {
            // Arrange - Giả định rằng đã đạt giới hạn số lượng phòng ban
            when(centralMapper.toDepartment(any())).thenReturn(new Department("Phòng mới", "Mô tả"));
            when(departmentRepository.save(any())).thenThrow(
                    new org.springframework.dao.DataIntegrityViolationException("Maximum department limit reached"));

            // Act
            Try<Department> result = departmentManagement.create(createDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when save department", result.getCause().getMessage());
        }

        @Test
        @DisplayName("Kiểm tra xử lý sự cố giao dịch")
        void shouldHandleTransactionFailure() {
            // Arrange
            when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(itDepartment));
            when(centralMapper.toDepartmentUpdate(any(), any())).thenReturn(itDepartment);
            when(departmentRepository.save(any())).thenThrow(
                    new org.springframework.transaction.TransactionSystemException("Transaction silently rolled back"));

            // Act
            Try<Department> result = departmentManagement.update(itDepartment);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when update department", result.getCause().getMessage());
        }

        @Test
        @DisplayName("Kiểm tra xử lý trận đồng thời")
        void shouldHandleConcurrencyIssues() {
            // Arrange
            when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(itDepartment));
            when(centralMapper.toDepartmentUpdate(any(), any())).thenReturn(itDepartment);
            when(departmentRepository.save(any())).thenThrow(
                    new org.springframework.dao.OptimisticLockingFailureException("Row was updated or deleted by another transaction"));

            // Act
            Try<Department> result = departmentManagement.update(itDepartment);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when update department", result.getCause().getMessage());
        }
    }

    @Nested
    @DisplayName("Performance and security tests")
    class PerformanceAndSecurityTests {

        @Test
        @DisplayName("Kiểm tra xử lý tên phòng ban có ký tự đặc biệt")
        void shouldHandleSpecialCharactersInName() {
            // Arrange
            DepartmentCreateRequestDTO specialCharsDTO = new DepartmentCreateRequestDTO();
            specialCharsDTO.setName("Phòng SQL 'Injection'; DROP TABLE;");
            specialCharsDTO.setDescription("Mô tả bình thường");

            Department mappedDepartment = new Department(specialCharsDTO.getName(), specialCharsDTO.getDescription());

            when(centralMapper.toDepartment(any())).thenReturn(mappedDepartment);
            when(departmentRepository.save(any())).thenThrow(
                    new DataIntegrityViolationException("Invalid characters in input"));

            // Act
            Try<Department> result = departmentManagement.create(specialCharsDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when save department", result.getCause().getMessage());
        }

        @Test
        @DisplayName("Xử lý tên phòng ban quá dài")
        void shouldHandleExtremelyLongDepartmentName() {
            // Arrange
            StringBuilder longName = new StringBuilder("Phòng ");
            for (int i = 0; i < 500; i++) {
                longName.append("x");
            }

            DepartmentCreateRequestDTO longNameDTO = new DepartmentCreateRequestDTO();
            longNameDTO.setName(longName.toString());
            longNameDTO.setDescription("Mô tả bình thường");

            Department mappedDepartment = new Department(longNameDTO.getName(), longNameDTO.getDescription());

            when(centralMapper.toDepartment(any())).thenReturn(mappedDepartment);
            when(departmentRepository.save(any())).thenThrow(
                    new DataIntegrityViolationException("String or binary data would be truncated"));

            // Act
            Try<Department> result = departmentManagement.create(longNameDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when save department", result.getCause().getMessage());
        }

        @Test
        @DisplayName("Kiểm tra xử lý nhiều yêu cầu cập nhật đồng thời")
        void shouldHandleMultipleUpdateRequests() {
            // Arrange
            Department updatedDepartment1 = new Department();
            updatedDepartment1.setId(1L);
            updatedDepartment1.setName("Phòng CNTT - Cập nhật 1");

            Department updatedDepartment2 = new Department();
            updatedDepartment2.setId(1L);
            updatedDepartment2.setName("Phòng CNTT - Cập nhật 2");

            when(departmentRepository.findById(1L)).thenReturn(Optional.of(itDepartment));
            when(centralMapper.toDepartmentUpdate(any(), eq(updatedDepartment1))).thenReturn(updatedDepartment1);
            when(centralMapper.toDepartmentUpdate(any(), eq(updatedDepartment2))).thenReturn(updatedDepartment2);

            // Giả lập cập nhật đầu tiên thành công
            when(departmentRepository.save(updatedDepartment1)).thenReturn(updatedDepartment1);

            // Giả lập cập nhật thứ hai thất bại do xung đột
            when(departmentRepository.save(updatedDepartment2)).thenThrow(
                    new org.springframework.dao.OptimisticLockingFailureException("Row was updated by another transaction"));

            // Act
            Try<Department> result1 = departmentManagement.update(updatedDepartment1);
            Try<Department> result2 = departmentManagement.update(updatedDepartment2);

            // Assert
            assertTrue(result1.isSuccess());
            assertTrue(result2.isFailure());
            assertEquals("Phòng CNTT - Cập nhật 1", result1.get().getName());
            assertEquals("Failure when update department", result2.getCause().getMessage());
        }
    }

    @Nested
    @DisplayName("Business logic tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Xử lý các mối quan hệ giữa phòng ban và các đối tượng khác")
        void shouldHandleDepartmentRelationships() {
            // Arrange - Giả định khi xóa phòng ban có liên kết với phòng học
            doThrow(new DataIntegrityViolationException(
                    "Cannot delete or update a parent row: a foreign key constraint fails (`asset_db`.`place`, CONSTRAINT `FK_place_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`))"
            )).when(departmentRepository).deleteById(4L);

            // Act
            Try<Boolean> result = departmentManagement.delete(4L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when delete department with id: 4", result.getCause().getMessage());
        }

        @Test
        @DisplayName("Xử lý khi phòng ban có liên kết với người dùng hệ thống")
        void shouldHandleDepartmentWithUsers() {
            // Arrange - Giả định khi xóa phòng ban có liên kết với người dùng
            doThrow(new DataIntegrityViolationException(
                    "Cannot delete or update a parent row: a foreign key constraint fails (`asset_db`.`users`, CONSTRAINT `FK_user_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`))"
            )).when(departmentRepository).deleteById(1L);

            // Act
            Try<Boolean> result = departmentManagement.delete(1L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when delete department with id: 1", result.getCause().getMessage());
        }

        @Test
        @DisplayName("Xử lý khi phòng ban có liên kết với tài sản")
        void shouldHandleDepartmentWithAssets() {
            // Arrange - Giả định khi xóa phòng ban có liên kết với tài sản
            doThrow(new DataIntegrityViolationException(
                    "Cannot delete or update a parent row: a foreign key constraint fails (`asset_db`.`material`, CONSTRAINT `FK_material_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`))"
            )).when(departmentRepository).deleteById(5L);

            // Act
            Try<Boolean> result = departmentManagement.delete(5L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when delete department with id: 5", result.getCause().getMessage());
        }
    }
}