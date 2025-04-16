package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Role;
import com.ptit.asset.domain.User;
import com.ptit.asset.domain.enumeration.RoleName;
import com.ptit.asset.dto.request.LoginRequestDTO;
import com.ptit.asset.dto.response.AuthResponseDTO;
import com.ptit.asset.repository.UserRepository;
import com.ptit.asset.security.UserPrinciple;
import com.ptit.asset.security.jwt.JwtProvider;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagementImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private UserManagementImpl userManagement;

    private User validUser;
    private LoginRequestDTO validLoginDTO;
    private UserPrinciple userPrinciple;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        validUser = new User("John Doe", "0123456789", "john@example.com", "johndoe", "encodedPassword", true, null, new HashSet<>());
        validUser.setId(1L);
        Role role = new Role();
        role.setRoleName(RoleName.ROLE_EMPLOYEE);
        validUser.getRoles().add(role);

        validLoginDTO = new LoginRequestDTO("johndoe", "password");

        userPrinciple = new UserPrinciple(
                1L,
                "John Doe",
                "0123456789",
                "john@example.com",
                "johndoe",
                "encodedPassword",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
        );

        authentication = new UsernamePasswordAuthenticationToken(userPrinciple, null, userPrinciple.getAuthorities());
    }

    @Test
    void login_success_validCredentials_returnsAuthResponse() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Option.of(validUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtProvider.generateJwtToken(authentication)).thenReturn("jwt-token");

        Try<AuthResponseDTO> result = userManagement.login(validLoginDTO);

        assertThat(result.isSuccess()).isTrue();
        AuthResponseDTO response = result.get();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("johndoe");
        assertThat(response.getFullName()).isEqualTo("John Doe");
        assertThat(response.getEmail()).isEqualTo("john@example.com");
        assertThat(response.getPhone()).isEqualTo("0123456789");
        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getRoles()).containsExactly("ROLE_EMPLOYEE");
        verify(userRepository).findByUsername("johndoe");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider).generateJwtToken(authentication);
    }

    @Test
    void login_failure_usernameNotFound_returnsFailure() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Option.none());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Bad credentials") {});

        Try<AuthResponseDTO> result = userManagement.login(validLoginDTO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when authenticate login");
        verify(userRepository).findByUsername("johndoe");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtProvider);
    }

    @Test
    void login_failure_wrongPassword_returnsFailure() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Option.of(validUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Bad credentials") {});

        Try<AuthResponseDTO> result = userManagement.login(new LoginRequestDTO("johndoe", "wrongpassword"));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when authenticate login");
        verify(userRepository).findByUsername("johndoe");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtProvider);
    }

    @Test
    void login_failure_deactiveUser_returnsFailure() {
        validUser.setActive(false);
        when(userRepository.findByUsername("johndoe")).thenReturn(Option.of(validUser));

        Try<AuthResponseDTO> result = userManagement.login(validLoginDTO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("User has been DE-ACTIVE account");
        verify(userRepository).findByUsername("johndoe");
        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtProvider);
    }

    @Test
    void login_edgeCase_nullUsername_returnsFailure() {
        LoginRequestDTO nullUsernameDTO = new LoginRequestDTO(null, "password");
        when(userRepository.findByUsername(null)).thenReturn(Option.none());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Bad credentials") {});

        Try<AuthResponseDTO> result = userManagement.login(nullUsernameDTO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when authenticate login");
        verify(userRepository).findByUsername(null);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtProvider);
    }

    @Test
    void login_edgeCase_nullPassword_returnsFailure() {
        LoginRequestDTO nullPasswordDTO = new LoginRequestDTO("johndoe", null);
        when(userRepository.findByUsername("johndoe")).thenReturn(Option.of(validUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Bad credentials") {});

        Try<AuthResponseDTO> result = userManagement.login(nullPasswordDTO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when authenticate login");
        verify(userRepository).findByUsername("johndoe");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtProvider);
    }

    @Test
    void login_edgeCase_nullDTO_throwsNullPointerException() {
        assertThatThrownBy(() -> userManagement.login(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(null);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtProvider);
    }
}