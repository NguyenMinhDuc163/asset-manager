package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Liquidate;
import com.ptit.asset.domain.User;
import com.ptit.asset.dto.request.LiquidateCreateRequestDTO;
import com.ptit.asset.repository.LiquidateRepository;
import com.ptit.asset.repository.UserRepository;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LiquidateManagementImplTest {

    @Mock
    private LiquidateRepository liquidateRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CentralMapper centralMapper;

    @InjectMocks
    private LiquidateManagementImpl liquidateManagement;

    private User testUser;
    private Liquidate testLiquidate;
    private LiquidateCreateRequestDTO dto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testLiquidate = new Liquidate();
        testLiquidate.setId(1L);
        testLiquidate.setUser(testUser);
        testLiquidate.setTime(Instant.now());
        testLiquidate.setDone(false);

        dto = new LiquidateCreateRequestDTO();
        LiquidateCreateRequestDTO.Embedded embedded = new LiquidateCreateRequestDTO.Embedded();
        embedded.setUserId(1L);
        dto.setEmbedded(embedded);
        dto.setTime(Instant.now());
    }

    @Test
    void create_success() {
        // Mục tiêu: Kiểm tra tạo Liquidate thành công khi user tồn tại và không có Liquidate chưa hoàn tất
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(liquidateRepository.findAll()).thenReturn(Collections.emptyList());
        when(centralMapper.toLiquidate(dto, testUser)).thenReturn(testLiquidate);
        when(liquidateRepository.save(any(Liquidate.class))).thenReturn(testLiquidate);

        Try<Liquidate> result = liquidateManagement.create(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getDone()).isFalse();
        verify(userRepository, times(1)).findById(1L);
        verify(liquidateRepository, times(1)).findAll();
        verify(centralMapper, times(1)).toLiquidate(dto, testUser);
        verify(liquidateRepository, times(1)).save(any(Liquidate.class));
    }

    @Test
    void create_fail_userNotFound() {
        // Mục tiêu: Kiểm tra tạo Liquidate thất bại khi user không tồn tại
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Liquidate> result = liquidateManagement.create(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find user by id: 1");
        verify(userRepository, times(1)).findById(1L);
        verifyNoInteractions(liquidateRepository, centralMapper);
    }

    @Test
    void create_fail_existingInProcess() {
        // Mục tiêu: Kiểm tra tạo Liquidate thất bại khi có Liquidate chưa hoàn tất
        Liquidate existing = new Liquidate();
        existing.setId(2L);
        existing.setDone(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(liquidateRepository.findAll()).thenReturn(Collections.singletonList(existing));

        Try<Liquidate> result = liquidateManagement.create(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Already existing liquidate not done");
        verify(userRepository, times(1)).findById(1L);
        verify(liquidateRepository, times(1)).findAll();
        verifyNoInteractions(centralMapper);
        verify(liquidateRepository, never()).save(any(Liquidate.class));
    }

    @Test
    void create_fail_saveError() {
        // Mục tiêu: Kiểm tra tạo Liquidate thất bại khi lưu vào repository gặp lỗi
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(liquidateRepository.findAll()).thenReturn(Collections.emptyList());
        when(centralMapper.toLiquidate(dto, testUser)).thenReturn(testLiquidate);
        when(liquidateRepository.save(any(Liquidate.class))).thenThrow(new RuntimeException("DB error"));

        Try<Liquidate> result = liquidateManagement.create(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when save liquidate");
        verify(userRepository, times(1)).findById(1L);
        verify(liquidateRepository, times(1)).findAll();
        verify(centralMapper, times(1)).toLiquidate(dto, testUser);
        verify(liquidateRepository, times(1)).save(any(Liquidate.class));
    }
}