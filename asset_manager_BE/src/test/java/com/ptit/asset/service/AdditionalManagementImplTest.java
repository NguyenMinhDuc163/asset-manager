package com.ptit.asset.service;

import com.ptit.asset.domain.Additional;
import com.ptit.asset.domain.Organization;
import com.ptit.asset.domain.User;
import com.ptit.asset.dto.request.AdditionalChangeStatusRequestDTO;
import com.ptit.asset.dto.request.AdditionalCreateRequestDTO;
import com.ptit.asset.dto.request.AdditionalUpdateRequestDTO;
import com.ptit.asset.repository.AdditionalRepository;
import com.ptit.asset.repository.OrganizationRepository;
import com.ptit.asset.repository.UserRepository;
import com.ptit.asset.service.manager.impl.AdditionalManagementImpl;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdditionalManagementImplTest {

    @Mock
    private AdditionalRepository additionalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private CentralMapper centralMapper;

    @InjectMocks
    private AdditionalManagementImpl additionalManagement;

    private User testUser;
    private Organization testOrg;
    private Additional testAdditional;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testOrg = new Organization();
        testOrg.setId(1L);
        testOrg.setName("Test Organization");

        testAdditional = new Additional();
        testAdditional.setId(1L);
        testAdditional.setUser(testUser);
        testAdditional.setOrganization(testOrg);
        testAdditional.setInProcess(true);
    }

    // getOne
    @Test
    void testGetOne_success() {
        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        Try<Additional> result = additionalManagement.getOne(1L);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(additionalRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOne_failure_notFound() {
        when(additionalRepository.findById(1L)).thenReturn(Optional.empty());
        Try<Additional> result = additionalManagement.getOne(1L);
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when get additional by id: 1");
        verify(additionalRepository, times(1)).findById(1L);
    }

    // create
    @Test
    void testCreate_success() {
        AdditionalCreateRequestDTO dto = new AdditionalCreateRequestDTO();
        AdditionalCreateRequestDTO.Embedded embedded = new AdditionalCreateRequestDTO.Embedded();
        embedded.setUserId(1L);
        embedded.setOrganizationId(1L);
        dto.setEmbedded(embedded);
        dto.setTime(Instant.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(testOrg));
        when(additionalRepository.findAll()).thenReturn(Collections.emptyList());
        when(centralMapper.toAdditional(dto, testUser, testOrg)).thenReturn(testAdditional);
        when(additionalRepository.save(any(Additional.class))).thenReturn(testAdditional);

        Try<Additional> result = additionalManagement.create(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getInProcess()).isTrue();
        verify(userRepository, times(1)).findById(1L);
        verify(organizationRepository, times(1)).findById(1L);
        verify(additionalRepository, times(1)).findAll();
        verify(additionalRepository, times(1)).save(any(Additional.class));
    }

    @Test
    void testCreate_failure_userNotFound() {
        AdditionalCreateRequestDTO dto = new AdditionalCreateRequestDTO();
        AdditionalCreateRequestDTO.Embedded embedded = new AdditionalCreateRequestDTO.Embedded();
        embedded.setUserId(1L);
        embedded.setOrganizationId(1L);
        dto.setEmbedded(embedded);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Additional> result = additionalManagement.create(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find user by id: 1");
        verify(userRepository, times(1)).findById(1L);
        verifyNoInteractions(organizationRepository, additionalRepository);
    }

    @Test
    void testCreate_failure_orgNotFound() {
        AdditionalCreateRequestDTO dto = new AdditionalCreateRequestDTO();
        AdditionalCreateRequestDTO.Embedded embedded = new AdditionalCreateRequestDTO.Embedded();
        embedded.setUserId(1L);
        embedded.setOrganizationId(1L);
        dto.setEmbedded(embedded);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(organizationRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Additional> result = additionalManagement.create(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find organization by id: 1");
        verify(userRepository, times(1)).findById(1L);
        verify(organizationRepository, times(1)).findById(1L);
        verifyNoInteractions(additionalRepository);
    }

    @Test
    void testCreate_failure_existingInProcess() {
        AdditionalCreateRequestDTO dto = new AdditionalCreateRequestDTO();
        AdditionalCreateRequestDTO.Embedded embedded = new AdditionalCreateRequestDTO.Embedded();
        embedded.setUserId(1L);
        embedded.setOrganizationId(1L);
        dto.setEmbedded(embedded);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(testOrg));
        when(additionalRepository.findAll()).thenReturn(Collections.singletonList(testAdditional));

        Try<Additional> result = additionalManagement.create(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Already existing additional in process");
        verify(userRepository, times(1)).findById(1L);
        verify(organizationRepository, times(1)).findById(1L);
        verify(additionalRepository, times(1)).findAll();
    }

    // update
    @Test
    void testUpdate_success_noChanges() {
        AdditionalUpdateRequestDTO dto = new AdditionalUpdateRequestDTO();
        dto.setId(1L);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(centralMapper.toAdditionalUpdate(testAdditional, dto)).thenReturn(testAdditional);
        when(additionalRepository.save(testAdditional)).thenReturn(testAdditional);

        Try<Additional> result = additionalManagement.update(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(additionalRepository, times(1)).findById(1L);
        verify(additionalRepository, times(1)).save(testAdditional);
    }

    @Test
    void testUpdate_success_withNewUserAndOrg() {
        AdditionalUpdateRequestDTO dto = new AdditionalUpdateRequestDTO();
        dto.setId(1L);
        AdditionalUpdateRequestDTO.Embedded embedded = new AdditionalUpdateRequestDTO.Embedded();
        embedded.setUserId(2L);
        embedded.setOrganizationId(2L);
        dto.setEmbedded(embedded);

        User newUser = new User();
        newUser.setId(2L);
        Organization newOrg = new Organization();
        newOrg.setId(2L);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(centralMapper.toAdditionalUpdate(testAdditional, dto)).thenReturn(testAdditional);
        when(userRepository.findById(2L)).thenReturn(Optional.of(newUser));
        when(organizationRepository.findById(2L)).thenReturn(Optional.of(newOrg));
        when(additionalRepository.save(testAdditional)).thenReturn(testAdditional);

        Try<Additional> result = additionalManagement.update(dto);

        assertThat(result.isSuccess()).isTrue();
        verify(userRepository, times(1)).findById(2L);
        verify(organizationRepository, times(1)).findById(2L);
        verify(additionalRepository, times(1)).save(testAdditional);
    }

    @Test
    void testUpdate_failure_notFound() {
        AdditionalUpdateRequestDTO dto = new AdditionalUpdateRequestDTO();
        dto.setId(1L);

        when(additionalRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Additional> result = additionalManagement.update(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find additional to update with id: 1");
        verify(additionalRepository, times(1)).findById(1L);
    }

    // delete
    @Test
    void testDelete_success() {
        doNothing().when(additionalRepository).deleteById(1L);

        Try<Boolean> result = additionalManagement.delete(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(additionalRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_failure() {
        doThrow(new RuntimeException("DB error")).when(additionalRepository).deleteById(1L);

        Try<Boolean> result = additionalManagement.delete(1L);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when delete additional with id: 1");
        verify(additionalRepository, times(1)).deleteById(1L);
    }

    // fetchAll
    @Test
    void testFetchAll_success() {
        when(additionalRepository.findAll()).thenReturn(Collections.singletonList(testAdditional));

        List<Additional> result = additionalManagement.fetchAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(additionalRepository, times(1)).findAll();
    }

    @Test
    void testFetchAll_empty() {
        when(additionalRepository.findAll()).thenReturn(Collections.emptyList());

        List<Additional> result = additionalManagement.fetchAll();

        assertThat(result).isEmpty();
        verify(additionalRepository, times(1)).findAll();
    }

    // changeStatus
    @Test
    void testChangeStatus_success() {
        AdditionalChangeStatusRequestDTO dto = new AdditionalChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(false);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(additionalRepository.save(testAdditional)).thenReturn(testAdditional);

        Try<Boolean> result = additionalManagement.changeStatus(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        assertThat(testAdditional.getInProcess()).isFalse();
        verify(additionalRepository, times(1)).findById(1L);
        verify(additionalRepository, times(1)).save(testAdditional);
    }

    @Test
    void testChangeStatus_failure_notFound() {
        AdditionalChangeStatusRequestDTO dto = new AdditionalChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(false);

        when(additionalRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Boolean> result = additionalManagement.changeStatus(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find additional with id: 1");
        verify(additionalRepository, times(1)).findById(1L);
    }

    @Test
    void testChangeStatus_failure_sameStatus() {
        AdditionalChangeStatusRequestDTO dto = new AdditionalChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(true);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));

        Try<Boolean> result = additionalManagement.changeStatus(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Nothing change with current status");
        verify(additionalRepository, times(1)).findById(1L);
    }

    @Test
    void testChangeStatus_failure_turnBackToTrue() {
        AdditionalChangeStatusRequestDTO dto = new AdditionalChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(true);

        Additional inactive = new Additional();
        inactive.setId(1L);
        inactive.setInProcess(false);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(inactive));

        Try<Boolean> result = additionalManagement.changeStatus(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("not allow turn back");
        verify(additionalRepository, times(1)).findById(1L);
    }

    // validationData (protected method, indirectly tested via create)
}