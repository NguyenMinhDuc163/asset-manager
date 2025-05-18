package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.*;
import com.ptit.asset.domain.enumeration.RoleName;
import com.ptit.asset.dto.request.*;
import com.ptit.asset.dto.response.AuthResponseDTO;
import com.ptit.asset.security.UserPrinciple;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Option;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
    @Mock private AuthenticationManager authenticationManager;
    @InjectMocks private UserManagementImpl userManagement;

    private User user;
    private Department department;
    private Role roleAdmin;
    private Role roleEmployee;
    private Role roleAccountant;
    private Role roleInspector;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("IT");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setFullName("Test User");
        user.setPhone("0123456789");
        user.setEmail("test@example.com");
        user.setPassword("encodedpassword");
        user.setActive(true);
        user.setDepartment(department);
        user.setRoles(new HashSet<>());

        roleAdmin = new Role();
        roleAdmin.setRoleName(RoleName.ROLE_ADMIN);

        roleEmployee = new Role();
        roleEmployee.setRoleName(RoleName.ROLE_EMPLOYEE);

        roleAccountant = new Role();
        roleAccountant.setRoleName(RoleName.ROLE_ACCOUNTANT);

        roleInspector = new Role();
        roleInspector.setRoleName(RoleName.ROLE_INSPECTOR);
    }

    // Tests for getOne
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

    // Tests for fetchAll
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

    // Tests for fetchPage
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

    // Tests for update
    @Test
    void update_success_withDepartmentAndRoles() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO();
        dto.setId(1L);
        dto.setPassword("newpassword");
        dto.setRoles(new HashSet<>(Arrays.asList("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_ACCOUNTANT", "ROLE_INSPECTOR")));
        UserUpdateRequestDTO.Embedded embedded = new UserUpdateRequestDTO.Embedded();
        embedded.setDepartmentId(2L);
        dto.setEmbedded(embedded);

        Department newDepartment = new Department();
        newDepartment.setId(2L);
        newDepartment.setName("HR");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(centralMapper.toUserUpdate(user, dto)).thenReturn(user);
        when(departmentRepository.findById(2L)).thenReturn(Optional.of(newDepartment));
        doReturn(Option.of(roleAdmin)).when(roleRepository).findByRoleName(RoleName.ROLE_ADMIN);
        doReturn(Option.of(roleEmployee)).when(roleRepository).findByRoleName(RoleName.ROLE_EMPLOYEE);
        doReturn(Option.of(roleAccountant)).when(roleRepository).findByRoleName(RoleName.ROLE_ACCOUNTANT);
        doReturn(Option.of(roleInspector)).when(roleRepository).findByRoleName(RoleName.ROLE_INSPECTOR);
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedpassword");
        when(userRepository.save(user)).thenReturn(user);

        Try<User> result = userManagement.update(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(user);
        assertThat(user.getDepartment()).isEqualTo(newDepartment);
        assertThat(user.getRoles()).containsExactlyInAnyOrder(roleAdmin, roleEmployee, roleAccountant, roleInspector);
        assertThat(user.getPassword()).isEqualTo("encodedpassword");
        verify(userRepository).findById(1L);
        verify(departmentRepository).findById(2L);
        verify(roleRepository).findByRoleName(RoleName.ROLE_ADMIN);
        verify(roleRepository).findByRoleName(RoleName.ROLE_EMPLOYEE);
        verify(roleRepository).findByRoleName(RoleName.ROLE_ACCOUNTANT);
        verify(roleRepository).findByRoleName(RoleName.ROLE_INSPECTOR);
        verify(passwordEncoder).encode("newpassword");
        verify(userRepository).save(user);
    }

    @Test
    void update_success_noDepartmentChange() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO();
        dto.setId(1L);
        dto.setPassword("newpassword");
        dto.setRoles(new HashSet<>(Arrays.asList("ROLE_ADMIN", "ROLE_ACCOUNTANT")));
        UserUpdateRequestDTO.Embedded embedded = new UserUpdateRequestDTO.Embedded();
        embedded.setDepartmentId(1L);
        dto.setEmbedded(embedded);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(centralMapper.toUserUpdate(user, dto)).thenReturn(user);
        doReturn(Option.of(roleAdmin)).when(roleRepository).findByRoleName(RoleName.ROLE_ADMIN);
        doReturn(Option.of(roleAccountant)).when(roleRepository).findByRoleName(RoleName.ROLE_ACCOUNTANT);
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedpassword");
        when(userRepository.save(user)).thenReturn(user);

        Try<User> result = userManagement.update(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(user);
        assertThat(user.getDepartment()).isEqualTo(department);
        assertThat(user.getRoles()).containsExactlyInAnyOrder(roleAdmin, roleAccountant);
        assertThat(user.getPassword()).isEqualTo("encodedpassword");
        verify(userRepository).findById(1L);
        verify(departmentRepository, never()).findById(any());
        verify(roleRepository).findByRoleName(RoleName.ROLE_ADMIN);
        verify(roleRepository).findByRoleName(RoleName.ROLE_ACCOUNTANT);
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

        Try<User> result = userManagement.update(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("User register need assign role");
        verify(userRepository).findById(1L);
        verify(centralMapper).toUserUpdate(user, dto);
        verifyNoInteractions(departmentRepository, roleRepository, passwordEncoder, userRepository);
    }
    // Tests for register
    @Test
    void register_success() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setFullName("New User");
        dto.setPhone("0987654321");
        dto.setEmail("newuser@example.com");
        dto.setUsername("newuser");
        dto.setPassword("password123");
        dto.setActive(true);
        RegisterRequestDTO.Embedded embedded = new RegisterRequestDTO.Embedded();
        embedded.setDepartmentId(1L);
        dto.setEmbedded(embedded);
        dto.setRoles(new HashSet<>(Arrays.asList("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_ACCOUNTANT", "ROLE_INSPECTOR")));

        User newUser = new User();
        newUser.setFullName("New User");
        newUser.setPhone("0987654321");
        newUser.setEmail("newuser@example.com");
        newUser.setUsername("newuser");
        newUser.setPassword("encodedpassword");
        newUser.setActive(true);
        newUser.setDepartment(department);
        newUser.setRoles(new HashSet<>(Arrays.asList(roleAdmin, roleEmployee, roleAccountant, roleInspector)));

        doReturn(Option.none()).when(userRepository).findByUsername("newuser");
        doReturn(Option.none()).when(userRepository).findByEmail("newuser@example.com");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        doReturn(Option.of(roleAdmin)).when(roleRepository).findByRoleName(RoleName.ROLE_ADMIN);
        doReturn(Option.of(roleEmployee)).when(roleRepository).findByRoleName(RoleName.ROLE_EMPLOYEE);
        doReturn(Option.of(roleAccountant)).when(roleRepository).findByRoleName(RoleName.ROLE_ACCOUNTANT);
        doReturn(Option.of(roleInspector)).when(roleRepository).findByRoleName(RoleName.ROLE_INSPECTOR);
        when(passwordEncoder.encode("password123")).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        Try<AuthResponseDTO> result = userManagement.register(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getUsername()).isEqualTo("newuser");
        assertThat(result.get().getRoles()).containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_ACCOUNTANT", "ROLE_INSPECTOR");
        verify(userRepository).findByUsername("newuser");
        verify(userRepository).findByEmail("newuser@example.com");
        verify(departmentRepository).findById(1L);
        verify(roleRepository).findByRoleName(RoleName.ROLE_ADMIN);
        verify(roleRepository).findByRoleName(RoleName.ROLE_EMPLOYEE);
        verify(roleRepository).findByRoleName(RoleName.ROLE_ACCOUNTANT);
        verify(roleRepository).findByRoleName(RoleName.ROLE_INSPECTOR);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_success_noDepartment() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setFullName("New User");
        dto.setPhone("0987654321");
        dto.setEmail("newuser@example.com");
        dto.setUsername("newuser");
        dto.setPassword("password123");
        dto.setActive(true);
        dto.setEmbedded(new RegisterRequestDTO.Embedded()); // No departmentId
        dto.setRoles(new HashSet<>(Arrays.asList("ROLE_ADMIN", "ROLE_INSPECTOR")));

        User newUser = new User();
        newUser.setFullName("New User");
        newUser.setPhone("0987654321");
        newUser.setEmail("newuser@example.com");
        newUser.setUsername("newuser");
        newUser.setPassword("encodedpassword");
        newUser.setActive(true);
        newUser.setDepartment(null);
        newUser.setRoles(new HashSet<>(Arrays.asList(roleAdmin, roleInspector)));

        doReturn(Option.none()).when(userRepository).findByUsername("newuser");
        doReturn(Option.none()).when(userRepository).findByEmail("newuser@example.com");
        doReturn(Option.of(roleAdmin)).when(roleRepository).findByRoleName(RoleName.ROLE_ADMIN);
        doReturn(Option.of(roleInspector)).when(roleRepository).findByRoleName(RoleName.ROLE_INSPECTOR);
        when(passwordEncoder.encode("password123")).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        Try<AuthResponseDTO> result = userManagement.register(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getUsername()).isEqualTo("newuser");
        assertThat(result.get().getRoles()).containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_INSPECTOR");
        verify(userRepository).findByUsername("newuser");
        verify(userRepository).findByEmail("newuser@example.com");
        verify(roleRepository).findByRoleName(RoleName.ROLE_ADMIN);
        verify(roleRepository).findByRoleName(RoleName.ROLE_INSPECTOR);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verifyNoInteractions(departmentRepository);
    }

    @Test
    void register_failure_duplicateUsername() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("testuser");
        dto.setEmail("newuser@example.com");
        dto.setPassword("password123");
        dto.setFullName("New User");
        dto.setPhone("0987654321");
        dto.setActive(true);
        dto.setRoles(new HashSet<>(Collections.singletonList("ROLE_ADMIN")));

        doReturn(Option.of(user)).when(userRepository).findByUsername("testuser");

        Try<AuthResponseDTO> result = userManagement.register(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Username already used!");
        verify(userRepository).findByUsername("testuser");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(departmentRepository, roleRepository, passwordEncoder);
    }

    @Test
    void register_failure_duplicateEmail() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("newuser");
        dto.setEmail("test@example.com");
        dto.setPassword("password123");
        dto.setFullName("New User");
        dto.setPhone("0987654321");
        dto.setActive(true);
        dto.setRoles(new HashSet<>(Collections.singletonList("ROLE_ADMIN")));

        doReturn(Option.none()).when(userRepository).findByUsername("newuser");
        doReturn(Option.of(user)).when(userRepository).findByEmail("test@example.com");

        Try<AuthResponseDTO> result = userManagement.register(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Email already used!");
        verify(userRepository).findByUsername("newuser");
        verify(userRepository).findByEmail("test@example.com");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(departmentRepository, roleRepository, passwordEncoder);
    }

    @Test
    void register_failure_noRolesAssigned() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setFullName("New User");
        dto.setPhone("0987654321");
        dto.setEmail("newuser@example.com");
        dto.setUsername("newuser");
        dto.setPassword("password123");
        dto.setActive(true);
        RegisterRequestDTO.Embedded embedded = new RegisterRequestDTO.Embedded();
        embedded.setDepartmentId(1L);
        dto.setEmbedded(embedded);
        dto.setRoles(new HashSet<>(Collections.singletonList("ROLE_UNKNOWN")));

        doReturn(Option.none()).when(userRepository).findByUsername("newuser");
        doReturn(Option.none()).when(userRepository).findByEmail("newuser@example.com");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Try<AuthResponseDTO> result = userManagement.register(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("User register need assign role");
        verify(userRepository).findByUsername("newuser");
        verify(userRepository).findByEmail("newuser@example.com");
        verify(departmentRepository).findById(1L);
        verifyNoInteractions(roleRepository, passwordEncoder, userRepository);
    }
//
//    @Test
//    void register_failure_departmentNotFound() {
//        RegisterRequestDTO dto = new RegisterRequestDTO();
//        dto.setFullName("New User");
//        dto.setPhone("0987654321");
//        dto.setEmail("newuser@example.com");
//        dto.setUsername("newuser");
//        dto.setPassword("password123");
//        dto.setActive(true);
//        RegisterRequestDTO.Embedded embedded = new RegisterRequestDTO.Embedded();
//        embedded.setDepartmentId(999L);
//        dto.setEmbedded(embedded);
//        dto.setRoles(new HashSet<>(Collections.singletonList("ROLE_ADMIN")));
//
//        doReturn(Option.none()).when(userRepository).findByUsername("newuser");
//        doReturn(Option.none()).when(userRepository).findByEmail("newuser@example.com");
//        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());
//
//        Try<AuthResponseDTO> result = userManagement.register(dto);
//
//        assertThat(result.isFailure()).isTrue();
//        assertThat(result.getCause()).isInstanceOf(NoSuchElementException.class);
//        verify(userRepository).findByUsername("newuser");
//        verify(userRepository).findByEmail("newuser@example.com");
//        verify(departmentRepository).findById(999L);
//        verifyNoInteractions(roleRepository, passwordEncoder, userRepository);
//    }
//
//    @Test
//    void register_failure_roleNotFound() {
//        RegisterRequestDTO dto = new RegisterRequestDTO();
//        dto.setFullName("New User");
//        dto.setPhone("0987654321");
//        dto.setEmail("newuser@example.com");
//        dto.setUsername("newuser");
//        dto.setPassword("password123");
//        dto.setActive(true);
//        RegisterRequestDTO.Embedded embedded = new RegisterRequestDTO.Embedded();
//        embedded.setDepartmentId(1L);
//        dto.setEmbedded(embedded);
//        dto.setRoles(new HashSet<>(Collections.singletonList("ROLE_ACCOUNTANT")));
//
//        doReturn(Option.none()).when(userRepository).findByUsername("newuser");
//        doReturn(Option.none()).when(userRepository).findByEmail("newuser@example.com");
//        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
//        doReturn(Option.none()).when(roleRepository).findByRoleName(RoleName.ROLE_ACCOUNTANT);
//
//        Try<AuthResponseDTO> result = userManagement.register(dto);
//
//        assertThat(result.isFailure()).isTrue();
//        assertThat(result.getCause().getMessage()).isEqualTo("User register need assign role");
//        verify(userRepository).findByUsername("newuser");
//        verify(userRepository).findByEmail("newuser@example.com");
//        verify(departmentRepository).findById(1L);
//        verify(roleRepository).findByRoleName(RoleName.ROLE_ACCOUNTANT);
//        verifyNoInteractions(passwordEncoder, userRepository);
//    }

//
//    // Tests for resetPassword
//    @Test
//    void resetPassword_success() {
//        ResetPasswordRequestDTO dto = new ResetPasswordRequestDTO();
//        dto.setUsername("testuser");
//        dto.setCurrentPassword("currentpassword");
//        dto.setNewPassword("newpassword");
//        dto.setNewPasswordConfirm("newpassword");
//
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getPrincipal()).thenReturn(new UserPrinciple(1L, "Test User", "0123456789", "test@example.com", "testuser", "123456", Collections.emptyList()));
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenReturn(authentication);
//
//        doReturn(Option.of(user)).when(userRepository).findByUsername("testuser");
//        when(passwordEncoder.encode("newpassword")).thenReturn("encodednewpassword");
//        when(userRepository.save(user)).thenReturn(user);
//
//        Try<Boolean> result = userManagement.resetPassword(dto);
//
//        assertThat(result.isSuccess()).isTrue();
//        assertThat(result.get()).isTrue();
//        assertThat(user.getPassword()).isEqualTo("encodednewpassword");
//        verify(userRepository).findByUsername("testuser");
//        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verify(passwordEncoder).encode("newpassword");
//        verify(userRepository).save(user);
//    }
//
//    @Test
//    void resetPassword_failure_usernameNotExist() {
//        ResetPasswordRequestDTO dto = new ResetPasswordRequestDTO();
//        dto.setUsername("nonexistent");
//        dto.setCurrentPassword("currentpassword");
//        dto.setNewPassword("newpassword");
//        dto.setNewPasswordConfirm("newpassword");
//
//        doReturn(Option.none()).when(userRepository).findByUsername("nonexistent");
//
//        Try<Boolean> result = userManagement.resetPassword(dto);
//
//        assertThat(result.isFailure()).isTrue();
//        assertThat(result.getCause().getMessage()).isEqualTo("Username not exist");
//        verify(userRepository).findByUsername("nonexistent");
//        verifyNoInteractions(authenticationManager, passwordEncoder, userRepository);
//    }
//
//    @Test
//    void resetPassword_failure_inactiveUser() {
//        ResetPasswordRequestDTO dto = new ResetPasswordRequestDTO();
//        dto.setUsername("testuser");
//        dto.setCurrentPassword("currentpassword");
//        dto.setNewPassword("newpassword");
//        dto.setNewPasswordConfirm("newpassword");
//
//        User inactiveUser = new User();
//        inactiveUser.setId(1L);
//        inactiveUser.setUsername("testuser");
//        inactiveUser.setFullName("Test User");
//        inactiveUser.setPhone("0123456789");
//        inactiveUser.setEmail("test@example.com");
//        inactiveUser.setPassword("encodedpassword");
//        inactiveUser.setActive(false);
//        inactiveUser.setDepartment(department);
//        inactiveUser.setRoles(new HashSet<>());
//
//        doReturn(Option.of(inactiveUser)).when(userRepository).findByUsername("testuser");
//
//        Try<Boolean> result = userManagement.resetPassword(dto);
//
//        assertThat(result.isFailure()).isTrue();
//        assertThat(result.getCause().getMessage()).isEqualTo("User has been DE-ACTIVE account");
//        verify(userRepository).findByUsername("testuser");
//        verifyNoInteractions(authenticationManager, passwordEncoder, userRepository);
//    }
//
//    @Test
//    void resetPassword_failure_invalidCurrentPassword() {
//        ResetPasswordRequestDTO dto = new ResetPasswordRequestDTO();
//        dto.setUsername("testuser");
//        dto.setCurrentPassword("wrongpassword");
//        dto.setNewPassword("newpassword");
//        dto.setNewPasswordConfirm("newpassword");
//
//        doReturn(Option.of(user)).when(userRepository).findByUsername("testuser");
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenThrow(new RuntimeException("Invalid credentials"));
//
//        Try<Boolean> result = userManagement.resetPassword(dto);
//
//        assertThat(result.isFailure()).isTrue();
//        assertThat(result.getCause().getMessage()).contains("Invalid credentials");
//        verify(userRepository).findByUsername("testuser");
//        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verifyNoInteractions(passwordEncoder, userRepository);
//    }
//
//    @Test
//    void resetPassword_failure_nullUserPrinciple() {
//        ResetPasswordRequestDTO dto = new ResetPasswordRequestDTO();
//        dto.setUsername("testuser");
//        dto.setCurrentPassword("currentpassword");
//        dto.setNewPassword("newpassword");
//        dto.setNewPasswordConfirm("newpassword");
//
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getPrincipal()).thenReturn(null);
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenReturn(authentication);
//
//        doReturn(Option.of(user)).when(userRepository).findByUsername("testuser");
//
//        Try<Boolean> result = userManagement.resetPassword(dto);
//
//        assertThat(result.isFailure()).isTrue();
//        assertThat(result.getCause().getMessage()).isEqualTo("User not authenticated!");
//        verify(userRepository).findByUsername("testuser");
//        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verifyNoInteractions(passwordEncoder, userRepository);
//    }
//
//    @Test
//    void resetPassword_failure_newPasswordSameAsCurrent() {
//        ResetPasswordRequestDTO dto = new ResetPasswordRequestDTO();
//        dto.setUsername("testuser");
//        dto.setCurrentPassword("password123");
//        dto.setNewPassword("password123");
//        dto.setNewPasswordConfirm("password123");
//
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getPrincipal()).thenReturn(new UserPrinciple(1L, "Test User", "0123456789", "test@example.com", "testuser", "123456", Collections.emptyList()));
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenReturn(authentication);
//
//        doReturn(Option.of(user)).when(userRepository).findByUsername("testuser");
//
//        Try<Boolean> result = userManagement.resetPassword(dto);
//
//        assertThat(result.isFailure()).isTrue();
//        assertThat(result.getCause().getMessage()).isEqualTo("New password is duplicated with current password");
//        verify(userRepository).findByUsername("testuser");
//        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verifyNoInteractions(passwordEncoder, userRepository);
//    }
//
//    @Test
//    void resetPassword_failure_passwordConfirmNotMatched() {
//        ResetPasswordRequestDTO dto = new ResetPasswordRequestDTO();
//        dto.setUsername("testuser");
//        dto.setCurrentPassword("currentpassword");
//        dto.setNewPassword("newpassword");
//        dto.setNewPasswordConfirm("differentpassword");
//
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getPrincipal()).thenReturn(new UserPrinciple(1L, "Test User", "0123456789", "test@example.com", "testuser", "123456", Collections.emptyList()));
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenReturn(authentication);
//
//        doReturn(Option.of(user)).when(userRepository).findByUsername("testuser");
//
//        Try<Boolean> result = userManagement.resetPassword(dto);
//
//        assertThat(result.isFailure()).isTrue();
//        assertThat(result.getCause().getMessage()).isEqualTo("Password confirm not matched");
//        verify(userRepository).findByUsername("testuser");
//        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verifyNoInteractions(passwordEncoder, userRepository);
//    }
//
//    @Test
//    void resetPassword_failure_saveException() {
//        ResetPasswordRequestDTO dto = new ResetPasswordRequestDTO();
//        dto.setUsername("testuser");
//        dto.setCurrentPassword("currentpassword");
//        dto.setNewPassword("newpassword");
//        dto.setNewPasswordConfirm("newpassword");
//
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getPrincipal()).thenReturn(new UserPrinciple(1L, "Test User", "0123456789", "test@example.com", "testuser", "123456", Collections.emptyList()));
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenReturn(authentication);
//
//        doReturn(Option.of(user)).when(userRepository).findByUsername("testuser");
//        when(passwordEncoder.encode("newpassword")).thenReturn("encodednewpassword");
//        when(userRepository.save(user)).thenThrow(new RuntimeException("Database error"));
//
//        Try<Boolean> result = userManagement.resetPassword(dto);
//
//        assertThat(result.isFailure()).isTrue();
//        assertThat(result.getCause().getMessage()).isEqualTo("Failure when reset password");
//        verify(userRepository).findByUsername("testuser");
//        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verify(passwordEncoder).encode("newpassword");
//        verify(userRepository).save(user);
//    }
}