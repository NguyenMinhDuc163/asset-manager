package com.ptit.asset.service;

import com.ptit.asset.domain.Category;
import com.ptit.asset.domain.Group;
import com.ptit.asset.dto.request.CategoryCreateRequestDTO;
import com.ptit.asset.dto.request.CategoryUpdateRequestDTO;
import com.ptit.asset.dto.request.FetchPageCategoryRequestDTO;
import com.ptit.asset.repository.CategoryRepository;
import com.ptit.asset.repository.GroupRepository;
import com.ptit.asset.service.manager.impl.CategoryManagementImpl;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryManagementImplTest {

    @Mock
    private CentralMapper centralMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private CategoryManagementImpl categoryManagement;

    private Group itGroup;
    private Group officeGroup;
    private Category computerCategory;
    private Category peripheralCategory;
    private Category furnitureCategory;
    private CategoryCreateRequestDTO createDTO;
    private CategoryUpdateRequestDTO updateDTO;

    @BeforeEach
    void setUp() {
        // Khởi tạo các nhóm
        itGroup = new Group();
        itGroup.setId(1L);
        itGroup.setCode("IT");
        itGroup.setDescription("Nhóm tài sản CNTT");

        officeGroup = new Group();
        officeGroup.setId(2L);
        officeGroup.setCode("OFFICE");
        officeGroup.setDescription("Nhóm trang thiết bị văn phòng");

        // Khởi tạo các danh mục
        computerCategory = new Category();
        computerCategory.setId(1L);
        computerCategory.setName("Máy tính");
        computerCategory.setDescription("Máy tính để bàn, laptop");
        computerCategory.setGroup(itGroup);

        peripheralCategory = new Category();
        peripheralCategory.setId(2L);
        peripheralCategory.setName("Thiết bị ngoại vi");
        peripheralCategory.setDescription("Chuột, bàn phím, màn hình");
        peripheralCategory.setGroup(itGroup);

        furnitureCategory = new Category();
        furnitureCategory.setId(3L);
        furnitureCategory.setName("Bàn ghế");
        furnitureCategory.setDescription("Bàn làm việc, ghế văn phòng");
        furnitureCategory.setGroup(officeGroup);

        // Khởi tạo DTO cho tạo mới
        createDTO = new CategoryCreateRequestDTO();
        createDTO.setName("Máy in");
        createDTO.setDescription("Máy in, máy scan, máy photocopy");

        CategoryCreateRequestDTO.Embedded embedded = new CategoryCreateRequestDTO.Embedded();
        embedded.setGroupId(1L);
        createDTO.setEmbedded(embedded);

        // Khởi tạo DTO cho cập nhật
        updateDTO = new CategoryUpdateRequestDTO();
        updateDTO.setId(1L);
        updateDTO.setName("Máy tính cá nhân");
        updateDTO.setDescription("PC, laptop cá nhân");

        CategoryUpdateRequestDTO.Embedded updateEmbedded = new CategoryUpdateRequestDTO.Embedded();
        updateEmbedded.setGroupId(1L);
        updateDTO.setEmbedded(updateEmbedded);
    }

    @Nested
    @DisplayName("Chức năng lấy thông tin danh mục")
    class GetCategoryTests {

        @Test
        @DisplayName("Lấy danh mục tồn tại")
        void getExistingCategory() {
            // Arrange
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(computerCategory));

            // Act
            Try<Category> result = categoryManagement.getOne(1L);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals(computerCategory, result.get());
            assertEquals("Máy tính", result.get().getName());
            verify(categoryRepository).findById(1L);
        }

        @Test
        @DisplayName("Lấy danh mục không tồn tại")
        void getNonExistingCategory() {
            // Arrange
            when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Try<Category> result = categoryManagement.getOne(999L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when get category by id: 999", result.getCause().getMessage());
            verify(categoryRepository).findById(999L);
        }

        @Test
        @DisplayName("Lấy danh mục với ID âm")
        void getCategoryWithNegativeId() {
            // Arrange
            when(categoryRepository.findById(-1L)).thenThrow(new IllegalArgumentException("ID không hợp lệ"));

            // Act
            Try<Category> result = categoryManagement.getOne(-1L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when get category by id: -1", result.getCause().getMessage());
            verify(categoryRepository).findById(-1L);
        }
    }

    @Nested
    @DisplayName("Chức năng tạo danh mục")
    class CreateCategoryTests {

        @Test
        @DisplayName("Tạo danh mục thành công")
        void createValidCategory() {
            // Arrange
            Category newCategory = new Category();
            newCategory.setName("Máy in");
            newCategory.setDescription("Máy in, máy scan, máy photocopy");
            newCategory.setGroup(itGroup);

            when(groupRepository.findById(1L)).thenReturn(Optional.of(itGroup));
            when(centralMapper.toCategory(any(CategoryCreateRequestDTO.class), any(Group.class))).thenReturn(newCategory);
            when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

            // Act
            Try<Category> result = categoryManagement.create(createDTO);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals(newCategory, result.get());
            assertEquals("Máy in", result.get().getName());
            verify(groupRepository).findById(1L);
            verify(centralMapper).toCategory(createDTO, itGroup);
            verify(categoryRepository).save(newCategory);
        }

        @Test
        @DisplayName("Tạo danh mục với nhóm không tồn tại")
        void createWithNonExistingGroup() {
            // Arrange
            when(groupRepository.findById(999L)).thenReturn(Optional.empty());

            CategoryCreateRequestDTO invalidDTO = new CategoryCreateRequestDTO();
            invalidDTO.setName("Danh mục mới");

            CategoryCreateRequestDTO.Embedded embedded = new CategoryCreateRequestDTO.Embedded();
            embedded.setGroupId(999L);
            invalidDTO.setEmbedded(embedded);

            // Act
            Try<Category> result = categoryManagement.create(invalidDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when find group with id:999", result.getCause().getMessage());
            verify(groupRepository).findById(999L);
            verify(centralMapper, never()).toCategory(any(), any());
            verify(categoryRepository, never()).save(any());
        }

        @Test
        @DisplayName("Tạo danh mục với tên trùng")
        void createDuplicateName() {
            // Arrange
            Category duplicateCategory = new Category();
            duplicateCategory.setName("Máy tính"); // Tên đã tồn tại
            duplicateCategory.setDescription("Mô tả mới");
            duplicateCategory.setGroup(itGroup);

            when(groupRepository.findById(1L)).thenReturn(Optional.of(itGroup));
            when(centralMapper.toCategory(any(CategoryCreateRequestDTO.class), any(Group.class))).thenReturn(duplicateCategory);
            when(categoryRepository.save(any(Category.class))).thenThrow(
                    new DataIntegrityViolationException("Unique name violation"));

            // Act
            Try<Category> result = categoryManagement.create(createDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when save product", result.getCause().getMessage());
            verify(groupRepository).findById(1L);
            verify(centralMapper).toCategory(createDTO, itGroup);
            verify(categoryRepository).save(duplicateCategory);
        }
    }

    @Nested
    @DisplayName("Chức năng cập nhật danh mục")
    class UpdateCategoryTests {

        @Test
        @DisplayName("Cập nhật danh mục thành công")
        void updateValidCategory() {
            // Arrange
            Category updatedCategory = new Category();
            updatedCategory.setId(1L);
            updatedCategory.setName("Máy tính cá nhân");
            updatedCategory.setDescription("PC, laptop cá nhân");
            updatedCategory.setGroup(itGroup);

            when(categoryRepository.findById(1L)).thenReturn(Optional.of(computerCategory));
            when(centralMapper.toCategoryUpdate(any(Category.class), any(CategoryUpdateRequestDTO.class))).thenReturn(updatedCategory);
            when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

            // Act
            Try<Category> result = categoryManagement.update(updateDTO);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals(updatedCategory, result.get());
            assertEquals("Máy tính cá nhân", result.get().getName());
            verify(categoryRepository).findById(1L);
            verify(centralMapper).toCategoryUpdate(computerCategory, updateDTO);
            verify(categoryRepository).save(updatedCategory);
        }

        @Test
        @DisplayName("Cập nhật danh mục không tồn tại")
        void updateNonExistingCategory() {
            // Arrange
            updateDTO.setId(999L);
            when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Try<Category> result = categoryManagement.update(updateDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when find category to update with id: 999", result.getCause().getMessage());
            verify(categoryRepository).findById(999L);
            verify(centralMapper, never()).toCategoryUpdate(any(), any());
            verify(categoryRepository, never()).save(any());
        }

        @Test
        @DisplayName("Cập nhật danh mục với nhóm khác")
        void updateCategoryWithDifferentGroup() {
            // Arrange
            Category updatedCategory = new Category();
            updatedCategory.setId(1L);
            updatedCategory.setName("Máy tính cá nhân");
            updatedCategory.setGroup(itGroup);

            updateDTO.getEmbedded().setGroupId(2L);

            when(categoryRepository.findById(1L)).thenReturn(Optional.of(computerCategory));
            when(centralMapper.toCategoryUpdate(any(Category.class), any(CategoryUpdateRequestDTO.class))).thenReturn(updatedCategory);
            when(groupRepository.findById(2L)).thenReturn(Optional.of(officeGroup));
            when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

            // Act
            Try<Category> result = categoryManagement.update(updateDTO);

            // Assert
            assertTrue(result.isSuccess());
            verify(categoryRepository).findById(1L);
            verify(centralMapper).toCategoryUpdate(computerCategory, updateDTO);
            verify(groupRepository).findById(2L);
            verify(categoryRepository).save(updatedCategory);
        }
    }

    @Nested
    @DisplayName("Chức năng xóa danh mục")
    class DeleteCategoryTests {

        @Test
        @DisplayName("Xóa danh mục thành công")
        void deleteValidCategory() {
            // Arrange
            doNothing().when(categoryRepository).deleteById(3L);

            // Act
            Try<Boolean> result = categoryManagement.delete(3L);

            // Assert
            assertTrue(result.isSuccess());
            assertTrue(result.get());
            verify(categoryRepository).deleteById(3L);
        }

        @Test
        @DisplayName("Xóa danh mục không tồn tại")
        void deleteNonExistingCategory() {
            // Arrange
            doThrow(new EmptyResultDataAccessException("Không tìm thấy danh mục", 1))
                    .when(categoryRepository).deleteById(999L);

            // Act
            Try<Boolean> result = categoryManagement.delete(999L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when delete category with id: 999", result.getCause().getMessage());
            verify(categoryRepository).deleteById(999L);
        }

        @Test
        @DisplayName("Xóa danh mục đang sử dụng bởi sản phẩm")
        void deleteCategoryInUse() {
            // Arrange
            doThrow(new DataIntegrityViolationException("FK_product_category constraint"))
                    .when(categoryRepository).deleteById(1L);

            // Act
            Try<Boolean> result = categoryManagement.delete(1L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when delete category with id: 1", result.getCause().getMessage());
            verify(categoryRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Chức năng lấy danh sách danh mục")
    class FetchCategoryTests {

        @Test
        @DisplayName("Lấy tất cả danh mục")
        void fetchAllCategories() {
            // Arrange
            List<Category> categories = Arrays.asList(computerCategory, peripheralCategory, furnitureCategory);
            when(categoryRepository.findAll()).thenReturn(categories);

            // Act
            List<Category> result = categoryManagement.fetchAll();

            // Assert
            assertEquals(3, result.size());
            assertEquals(categories, result);
            verify(categoryRepository).findAll();
        }

        @Test
        @DisplayName("Lấy danh sách danh mục rỗng")
        void fetchEmptyCategories() {
            // Arrange
            when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<Category> result = categoryManagement.fetchAll();

            // Assert
            assertTrue(result.isEmpty());
            verify(categoryRepository).findAll();
        }

        @Test
        @DisplayName("Lấy danh mục phân trang")
        void fetchPagedCategories() {
            // Arrange
            List<Category> categories = Arrays.asList(computerCategory, peripheralCategory);
            Page<Category> page = new PageImpl<>(categories);

            FetchPageCategoryRequestDTO dto = new FetchPageCategoryRequestDTO(0, 2);
            Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());

            when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);

            // Act
            Page<Category> result = categoryManagement.fetchPage(dto);

            // Assert
            assertEquals(2, result.getContent().size());
            assertEquals(page, result);
            verify(categoryRepository).findAll(pageable);
        }
    }

    @Nested
    @DisplayName("Các trường hợp đặc biệt")
    class EdgeCases {

        @Test
        @DisplayName("Xử lý lỗi kết nối cơ sở dữ liệu")
        void handleDBConnectionError() {
            // Arrange
            when(categoryRepository.findById(anyLong())).thenThrow(
                    new org.springframework.dao.DataAccessResourceFailureException("DB connection error"));

            // Act
            Try<Category> result = categoryManagement.getOne(1L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when get category by id: 1", result.getCause().getMessage());
            verify(categoryRepository).findById(1L);
        }

        @Test
        @DisplayName("Xử lý cập nhật đồng thời")
        void handleConcurrentUpdates() {
            // Arrange
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(computerCategory));
            when(centralMapper.toCategoryUpdate(any(Category.class), any(CategoryUpdateRequestDTO.class))).thenReturn(computerCategory);
            when(categoryRepository.save(any(Category.class))).thenThrow(
                    new org.springframework.dao.OptimisticLockingFailureException("Concurrent update"));

            // Act
            Try<Category> result = categoryManagement.update(updateDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when update category", result.getCause().getMessage());
            verify(categoryRepository).findById(1L);
            verify(centralMapper).toCategoryUpdate(computerCategory, updateDTO);
            verify(categoryRepository).save(computerCategory);
        }

        @Test
        @DisplayName("Xử lý tên danh mục quá dài")
        void handleLongCategoryName() {
            // Arrange
            StringBuilder longName = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                longName.append("a");
            }

            createDTO.setName(longName.toString());

            Category invalidCategory = new Category();
            invalidCategory.setName(longName.toString());
            invalidCategory.setGroup(itGroup);

            when(groupRepository.findById(1L)).thenReturn(Optional.of(itGroup));
            when(centralMapper.toCategory(any(CategoryCreateRequestDTO.class), any(Group.class))).thenReturn(invalidCategory);
            when(categoryRepository.save(any(Category.class))).thenThrow(
                    new DataIntegrityViolationException("String too long"));

            // Act
            Try<Category> result = categoryManagement.create(createDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when save product", result.getCause().getMessage());
            verify(groupRepository).findById(1L);
            verify(centralMapper).toCategory(createDTO, itGroup);
            verify(categoryRepository).save(invalidCategory);
        }
    }
}