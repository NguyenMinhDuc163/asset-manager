package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.*;
import com.ptit.asset.domain.enumeration.RoleName;
import com.ptit.asset.dto.request.UserUpdateRequestDTO;
import com.ptit.asset.dto.request.UserUpdateRequestDTO.Embedded;
import com.ptit.asset.dto.request.FetchPageUserRequestDTO;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ptit.asset.repository.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagementImplTest {

    @Mock private UserRepository userRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private CentralMapper centralMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserManagementImpl userManagement;

    private User user;
    private Department department;
    private Role roleAdmin;
    private Role roleEmployee;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("IT");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setDepartment(department);
        user.setRoles(new HashSet<>());

        roleAdmin = new Role();
        roleAdmin.setRoleName(RoleName.ROLE_ADMIN);

        roleEmployee = new Role();
        roleEmployee.setRoleName(RoleName.ROLE_EMPLOYEE);
    }

    // STT 1: Tests for getOne
    @Test
    void getOne_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Try<User> result = userManagement.getOne(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(user);
        verify(userRepository).findById(1L);
    }

    @Test
    void getOne_failure_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Try<User> result = userManagement.getOne(1L);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when find user by id: 1");
        verify(userRepository).findById(1L);
    }

    // STT 2: Tests for fetchAll
    @Test
    void fetchAll_success() {
        List<User> users = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userManagement.fetchAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(user);
        verify(userRepository).findAll();
    }

    @Test
    void fetchAll_success_empty() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userManagement.fetchAll();

        assertThat(result).isEmpty();
        verify(userRepository).findAll();
    }

    // STT 3: Tests for fetchPage
    @Test
    void fetchPage_success() {
        FetchPageUserRequestDTO dto = new FetchPageUserRequestDTO();
        dto.setPage(0);
        dto.setSize(10);
        Page<User> page = new PageImpl<>(Collections.singletonList(user));
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<User> result = userManagement.fetchPage(dto);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(user);
        verify(userRepository).findAll(pageable);
    }

    @Test
    void fetchPage_success_empty() {
        FetchPageUserRequestDTO dto = new FetchPageUserRequestDTO();
        dto.setPage(0);
        dto.setSize(10);
        Page<User> page = new PageImpl<>(Collections.emptyList());
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<User> result = userManagement.fetchPage(dto);

        assertThat(result.getContent()).isEmpty();
        verify(userRepository).findAll(pageable);
    }

    // STT 4: Tests for update
    @Test
    void update_success_withDepartmentAndRoles() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO();
        dto.setId(1L);
        dto.setPassword("newpassword");
        dto.setRoles(new HashSet<>(Arrays.asList("ROLE_ADMIN", "ROLE_EMPLOYEE")));
        Embedded embedded = new Embedded();
        embedded.setDepartmentId(2L);
        dto.setEmbedded(embedded);

        Department newDepartment = new Department();
        newDepartment.setId(2L);
        newDepartment.setName("HR");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(centralMapper.toUserUpdate(user, dto)).thenReturn(user);
        when(departmentRepository.findById(2L)).thenReturn(Optional.of(newDepartment));
        // Dòng dưới đây bị lỗi: Optional.of(roleAdmin)
        when(roleRepository.findByRoleName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(roleAdmin));
        // Dòng dưới đây bị lỗi: Optional.of(roleEmployee)
        when(roleRepository.findByRoleName(RoleName.ROLE_EMPLOYEE)).thenReturn(Optional.of(roleEmployee));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedpassword");
        when(userRepository.save(user)).thenReturn(user);

        Try<User> result = userManagement.update(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(user);
        assertThat(user.getDepartment()).isEqualTo(newDepartment);
        assertThat(user.getRoles()).containsExactlyInAnyOrder(roleAdmin, roleEmployee);
        assertThat(user.getPassword()).isEqualTo("encodedpassword");
        verify(userRepository).findById(1L);
        verify(departmentRepository).findById(2L);
        verify(roleRepository).findByRoleName(RoleName.ROLE_ADMIN);
        verify(roleRepository).findByRoleName(RoleName.ROLE_EMPLOYEE);
        verify(passwordEncoder).encode("newpassword");
        verify(userRepository).save(user);
    }

    @Test
    void update_success_noDepartmentChange() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO();
        dto.setId(1L);
        dto.setPassword("newpassword");
        dto.setRoles(new HashSet<>(Collections.singletonList("ROLE_ADMIN")));
        Embedded embedded = new Embedded();
        embedded.setDepartmentId(1L);
        dto.setEmbedded(embedded);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(centralMapper.toUserUpdate(user, dto)).thenReturn(user);
        // Dòng dưới đây bị lỗi: Optional.of(roleAdmin)
        when(roleRepository.findByRoleName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(roleAdmin));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedpassword");
        when(userRepository.save(user)).thenReturn(user);

        Try<User> result = userManagement.update(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(user);
        assertThat(user.getDepartment()).isEqualTo(department);
        assertThat(user.getRoles()).containsExactly(roleAdmin);
        assertThat(user.getPassword()).isEqualTo("encodedpassword");
        verify(userRepository).findById(1L);
        verify(departmentRepository, never()).findById(any());
        verify(roleRepository).findByRoleName(RoleName.ROLE_ADMIN);
        verify(passwordEncoder).encode("newpassword");
        verify(userRepository).save(user);
    }

    @Test
    void update_failure_userNotFound() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO();
        dto.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Try<User> result = userManagement.update(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when find user to update by id: 1");
        verify(userRepository).findById(1L);
        verifyNoInteractions(centralMapper, departmentRepository, roleRepository, passwordEncoder);
    }

    @Test
    void update_failure_noRolesAssigned() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO();
        dto.setId(1L);
        dto.setRoles(new HashSet<>(Collections.singletonList("ROLE_UNKNOWN")));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(centralMapper.toUserUpdate(user, dto)).thenReturn(user);
        // Dòng dưới đây bị lỗi: Optional.empty()
        when(roleRepository.findByRoleName(any())).thenReturn(Optional.empty());

        Try<User> result = userManagement.update(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("User register need assign role");
        verify(userRepository).findById(1L);
        verify(roleRepository).findByRoleName(any());
        verifyNoInteractions(departmentRepository, passwordEncoder);
    }

    @Test
    void update_failure_saveException() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO();
        dto.setId(1L);
        dto.setPassword("newpassword");
        dto.setRoles(new HashSet<>(Collections.singletonList("ROLE_ADMIN")));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(centralMapper.toUserUpdate(user, dto)).thenReturn(user);
        // Dòng dưới đây bị lỗi: Optional.of(roleAdmin)
        when(roleRepository.findByRoleName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(roleAdmin));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedpassword");
        when(userRepository.save(user)).thenThrow(new RuntimeException("DB error"));

        Try<User> result = userManagement.update(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when update user");
        verify(userRepository).findById(1L);
        verify(roleRepository).findByRoleName(RoleName.ROLE_ADMIN);
        verify(passwordEncoder).encode("newpassword");
        verify(userRepository).save(user);
    }

}