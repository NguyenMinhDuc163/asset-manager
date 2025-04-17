package com.ptit.asset.service;

import com.ptit.asset.domain.Group;
import com.ptit.asset.dto.request.GroupCreateRequestDTO;
import com.ptit.asset.repository.GroupRepository;
import com.ptit.asset.service.manager.impl.GroupManagementImpl;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupManagementImplTest {

    @Mock
    private CentralMapper centralMapper;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupManagementImpl groupManagement;

    private Group itGroup;
    private Group officeGroup;
    private Group labGroup;
    private GroupCreateRequestDTO createDTO;

    @BeforeEach
    void setUp() {
        // Khởi tạo các đối tượng test
        itGroup = new Group();
        itGroup.setId(1L);
        itGroup.setCode("IT");
        itGroup.setDescription("Nhóm tài sản CNTT");

        officeGroup = new Group();
        officeGroup.setId(2L);
        officeGroup.setCode("OFFICE");
        officeGroup.setDescription("Nhóm trang thiết bị văn phòng");

        labGroup = new Group();
        labGroup.setId(3L);
        labGroup.setCode("LAB");
        labGroup.setDescription("Nhóm thiết bị phòng thí nghiệm");

        createDTO = new GroupCreateRequestDTO();
        createDTO.setCode("FURNITURE");
        createDTO.setDescription("Nhóm nội thất");
    }

    @Nested
    @DisplayName("getOne tests")
    class GetOneTests {

        @Test
        @DisplayName("Lấy nhóm thành công")
        void getExistingGroup() {
            // Arrange
            when(groupRepository.findById(1L)).thenReturn(Optional.of(itGroup));

            // Act
            Try<Group> result = groupManagement.getOne(1L);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals(itGroup, result.get());
            verify(groupRepository).findById(1L);
        }

        @Test
        @DisplayName("Lấy nhóm không tồn tại")
        void getNonExistingGroup() {
            // Arrange
            when(groupRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Try<Group> result = groupManagement.getOne(999L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when get group by id: 999", result.getCause().getMessage());
            verify(groupRepository).findById(999L);
        }

        @Test
        @DisplayName("Lấy nhóm với ID âm")
        void getGroupWithNegativeId() {
            // Arrange
            when(groupRepository.findById(-1L)).thenThrow(new IllegalArgumentException("ID không hợp lệ"));

            // Act
            Try<Group> result = groupManagement.getOne(-1L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when get group by id: -1", result.getCause().getMessage());
            verify(groupRepository).findById(-1L);
        }
    }

    @Nested
    @DisplayName("create tests")
    class CreateTests {

        @Test
        @DisplayName("Tạo nhóm thành công")
        void createValidGroup() {
            // Arrange
            Group newGroup = new Group();
            newGroup.setCode("FURNITURE");
            newGroup.setDescription("Nhóm nội thất");

            when(centralMapper.toGroup(any(GroupCreateRequestDTO.class))).thenReturn(newGroup);
            when(groupRepository.save(any(Group.class))).thenReturn(newGroup);

            // Act
            Try<Group> result = groupManagement.create(createDTO);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals(newGroup, result.get());
            verify(centralMapper).toGroup(createDTO);
            verify(groupRepository).save(newGroup);
        }

        @Test
        @DisplayName("Tạo nhóm với mã trùng")
        void createDuplicateCode() {
            // Arrange
            Group duplicateGroup = new Group();
            duplicateGroup.setCode("IT"); // Mã đã tồn tại
            duplicateGroup.setDescription("Mô tả mới");

            when(centralMapper.toGroup(any(GroupCreateRequestDTO.class))).thenReturn(duplicateGroup);
            when(groupRepository.save(any(Group.class))).thenThrow(
                    new DataIntegrityViolationException("Unique code violation"));

            // Act
            Try<Group> result = groupManagement.create(createDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when save group", result.getCause().getMessage());
            verify(centralMapper).toGroup(createDTO);
            verify(groupRepository).save(duplicateGroup);
        }

        @Test
        @DisplayName("Tạo nhóm với mã quá dài")
        void createGroupWithLongCode() {
            // Arrange
            GroupCreateRequestDTO longCodeDTO = new GroupCreateRequestDTO();
            longCodeDTO.setCode("THIS_CODE_IS_TOO_LONG_FOR_DATABASE_FIELD"); // > 50 ký tự
            longCodeDTO.setDescription("Mô tả nhóm");

            Group invalidGroup = new Group();
            invalidGroup.setCode(longCodeDTO.getCode());
            invalidGroup.setDescription(longCodeDTO.getDescription());

            when(centralMapper.toGroup(any(GroupCreateRequestDTO.class))).thenReturn(invalidGroup);
            when(groupRepository.save(any(Group.class))).thenThrow(
                    new DataIntegrityViolationException("String too long"));

            // Act
            Try<Group> result = groupManagement.create(longCodeDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when save group", result.getCause().getMessage());
            verify(centralMapper).toGroup(longCodeDTO);
            verify(groupRepository).save(invalidGroup);
        }
    }

    @Nested
    @DisplayName("update tests")
    class UpdateTests {

        @Test
        @DisplayName("Cập nhật nhóm thành công")
        void updateValidGroup() {
            // Arrange
            Group updatedGroup = new Group();
            updatedGroup.setId(1L);
            updatedGroup.setCode("IT_UPDATED");
            updatedGroup.setDescription("Nhóm CNTT đã cập nhật");

            when(groupRepository.findById(1L)).thenReturn(Optional.of(itGroup));
            when(centralMapper.toGroupUpdate(any(Group.class), any(Group.class))).thenReturn(updatedGroup);
            when(groupRepository.save(any(Group.class))).thenReturn(updatedGroup);

            // Act
            Try<Group> result = groupManagement.update(updatedGroup);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals(updatedGroup, result.get());
            verify(groupRepository).findById(1L);
            verify(centralMapper).toGroupUpdate(itGroup, updatedGroup);
            verify(groupRepository).save(updatedGroup);
        }

        @Test
        @DisplayName("Cập nhật nhóm không tồn tại")
        void updateNonExistingGroup() {
            // Arrange
            Group nonExistingGroup = new Group();
            nonExistingGroup.setId(999L);
            nonExistingGroup.setCode("UNKNOWN");

            when(groupRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Try<Group> result = groupManagement.update(nonExistingGroup);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when find group to update with id: 999", result.getCause().getMessage());
            verify(groupRepository).findById(999L);
            verify(centralMapper, never()).toGroupUpdate(any(), any());
            verify(groupRepository, never()).save(any());
        }

        @Test
        @DisplayName("Cập nhật nhóm với mã trùng")
        void updateToDuplicateCode() {
            // Arrange
            Group duplicateGroup = new Group();
            duplicateGroup.setId(2L);
            duplicateGroup.setCode("IT"); // Mã đã tồn tại

            when(groupRepository.findById(2L)).thenReturn(Optional.of(officeGroup));
            when(centralMapper.toGroupUpdate(any(Group.class), any(Group.class))).thenReturn(duplicateGroup);
            when(groupRepository.save(any(Group.class))).thenThrow(
                    new DataIntegrityViolationException("Unique code violation"));

            // Act
            Try<Group> result = groupManagement.update(duplicateGroup);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when update group", result.getCause().getMessage());
            verify(groupRepository).findById(2L);
            verify(centralMapper).toGroupUpdate(officeGroup, duplicateGroup);
            verify(groupRepository).save(duplicateGroup);
        }
    }

    @Nested
    @DisplayName("delete tests")
    class DeleteTests {

        @Test
        @DisplayName("Xóa nhóm thành công")
        void deleteValidGroup() {
            // Arrange
            doNothing().when(groupRepository).deleteById(2L);

            // Act
            Try<Boolean> result = groupManagement.delete(2L);

            // Assert
            assertTrue(result.isSuccess());
            assertTrue(result.get());
            verify(groupRepository).deleteById(2L);
        }

        @Test
        @DisplayName("Xóa nhóm không tồn tại")
        void deleteNonExistingGroup() {
            // Arrange
            doThrow(new EmptyResultDataAccessException("Không tìm thấy nhóm", 1))
                    .when(groupRepository).deleteById(999L);

            // Act
            Try<Boolean> result = groupManagement.delete(999L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when delete group with id: 999", result.getCause().getMessage());
            verify(groupRepository).deleteById(999L);
        }

        @Test
        @DisplayName("Xóa nhóm đang được sử dụng")
        void deleteGroupInUse() {
            // Arrange
            doThrow(new DataIntegrityViolationException("FK_category_group constraint"))
                    .when(groupRepository).deleteById(1L);

            // Act
            Try<Boolean> result = groupManagement.delete(1L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when delete group with id: 1", result.getCause().getMessage());
            verify(groupRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("fetchAll tests")
    class FetchAllTests {

        @Test
        @DisplayName("Lấy danh sách nhóm")
        void fetchAllGroups() {
            // Arrange
            List<Group> groups = Arrays.asList(itGroup, officeGroup, labGroup);
            when(groupRepository.findAll()).thenReturn(groups);

            // Act
            List<Group> result = groupManagement.fetchAll();

            // Assert
            assertEquals(3, result.size());
            assertEquals(groups, result);
            verify(groupRepository).findAll();
        }

        @Test
        @DisplayName("Lấy danh sách nhóm rỗng")
        void fetchEmptyList() {
            // Arrange
            when(groupRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<Group> result = groupManagement.fetchAll();

            // Assert
            assertTrue(result.isEmpty());
            verify(groupRepository).findAll();
        }

        @Test
        @DisplayName("Xử lý lỗi khi lấy danh sách")
        void handleErrorOnFetch() {
            // Arrange
            when(groupRepository.findAll()).thenThrow(new RuntimeException("Database error"));

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                groupManagement.fetchAll();
            });

            assertEquals("Database error", exception.getMessage());
            verify(groupRepository).findAll();
        }
    }

    @Nested
    @DisplayName("edge cases")
    class EdgeCases {

        @Test
        @DisplayName("Xử lý nhóm với mã đặc biệt")
        void handleSpecialCharacters() {
            // Arrange
            Group specialGroup = new Group();
            specialGroup.setId(4L);
            specialGroup.setCode("SPECIAL!@#$%");
            specialGroup.setDescription("Nhóm với ký tự đặc biệt");

            when(centralMapper.toGroup(any())).thenReturn(specialGroup);
            when(groupRepository.save(any())).thenThrow(
                    new DataIntegrityViolationException("Invalid characters"));

            // Act
            Try<Group> result = groupManagement.create(createDTO);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when save group", result.getCause().getMessage());
            verify(centralMapper).toGroup(createDTO);
            verify(groupRepository).save(specialGroup);
        }

        @Test
        @DisplayName("Xử lý khi cập nhật nhiều đồng thời")
        void handleConcurrentUpdates() {
            // Arrange
            Group updateGroup1 = new Group();
            updateGroup1.setId(1L);
            updateGroup1.setCode("IT_UPDATE1");

            Group updateGroup2 = new Group();
            updateGroup2.setId(1L);
            updateGroup2.setCode("IT_UPDATE2");

            when(groupRepository.findById(1L)).thenReturn(Optional.of(itGroup));
            when(centralMapper.toGroupUpdate(any(), eq(updateGroup1))).thenReturn(updateGroup1);
            when(centralMapper.toGroupUpdate(any(), eq(updateGroup2))).thenReturn(updateGroup2);

            when(groupRepository.save(updateGroup1)).thenReturn(updateGroup1);
            when(groupRepository.save(updateGroup2)).thenThrow(
                    new org.springframework.dao.OptimisticLockingFailureException("Concurrent update"));

            // Act
            Try<Group> result1 = groupManagement.update(updateGroup1);
            Try<Group> result2 = groupManagement.update(updateGroup2);

            // Assert
            assertTrue(result1.isSuccess());
            assertTrue(result2.isFailure());
            assertEquals("Failure when update group", result2.getCause().getMessage());
        }

        @Test
        @DisplayName("Xử lý khi mất kết nối DB")
        void handleDBConnectionLoss() {
            // Arrange
            when(groupRepository.findById(anyLong())).thenThrow(
                    new org.springframework.dao.DataAccessResourceFailureException("DB connection lost"));

            // Act
            Try<Group> result = groupManagement.getOne(1L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals("Failure when get group by id: 1", result.getCause().getMessage());
            verify(groupRepository).findById(1L);
        }
    }
}