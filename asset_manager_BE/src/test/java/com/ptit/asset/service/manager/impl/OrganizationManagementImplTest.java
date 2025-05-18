package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Organization;
import com.ptit.asset.dto.request.FetchPageOrganizationRequestDTO;
import com.ptit.asset.dto.request.OrganizationCreateRequestDTO;
import com.ptit.asset.repository.OrganizationRepository;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationManagementImplTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private CentralMapper centralMapper;

    @InjectMocks
    private OrganizationManagementImpl organizationManagement;

    private Organization validOrganization;
    private OrganizationCreateRequestDTO createRequestDTO;
    private FetchPageOrganizationRequestDTO fetchPageRequestDTO;
    private Long validId;

    @BeforeEach
    void setUp() {
        validId = 1L;
        validOrganization = new Organization("OrgName", "Contact");
        validOrganization.setId(validId);
        createRequestDTO = new OrganizationCreateRequestDTO();
        fetchPageRequestDTO = new FetchPageOrganizationRequestDTO(0, 10);
    }

    // Tests for getOne
    @Test
    void getOne_success_validId_returnsOrganization() {
        when(organizationRepository.findById(validId)).thenReturn(Optional.of(validOrganization));

        Try<Organization> result = organizationManagement.getOne(validId);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(validOrganization);
        verify(organizationRepository).findById(validId);
    }

    @Test
    void getOne_failure_idNotFound_returnsFailure() {
        when(organizationRepository.findById(validId)).thenReturn(Optional.empty());

        Try<Organization> result = organizationManagement.getOne(validId);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when get organization by id: " + validId);
        verify(organizationRepository).findById(validId);
    }

    @Test
    void getOne_edgeCase_nullId_returnsFailure() {
        Try<Organization> result = organizationManagement.getOne(null);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).isInstanceOf(Exception.class);
        assertThat(result.getCause()).hasMessage("Failure when get organization by id: null");
        verify(organizationRepository).findById(null);
    }

    @Test
    void getOne_specificBranch_findByIdGetThrowsException_returnsFailure() {
        when(organizationRepository.findById(validId)).thenReturn(Optional.empty());

        Try<Organization> result = organizationManagement.getOne(validId);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when get organization by id: " + validId);
        verify(organizationRepository).findById(validId);
    }

    // Tests for create
    @Test
    void create_success_validDTO_returnsOrganization() {
        when(centralMapper.toOrganization(createRequestDTO)).thenReturn(validOrganization);
        when(organizationRepository.save(validOrganization)).thenReturn(validOrganization);

        Try<Organization> result = organizationManagement.create(createRequestDTO);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(validOrganization);
        verify(centralMapper).toOrganization(createRequestDTO);
        verify(organizationRepository).save(validOrganization);
    }

    @Test
    void create_failure_mappingError_returnsFailure() {
        when(centralMapper.toOrganization(createRequestDTO)).thenThrow(new RuntimeException("Mapping error"));

        Try<Organization> result = organizationManagement.create(createRequestDTO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when save organization");
        verify(centralMapper).toOrganization(createRequestDTO);
        verifyNoInteractions(organizationRepository);
    }

    @Test
    void create_edgeCase_nullDTO_returnsFailure() {
        Try<Organization> result = organizationManagement.create(null);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when save organization");
        verify(centralMapper).toOrganization(null);
        verifyNoInteractions(organizationRepository);
    }

    @Test
    void create_failure_duplicateName_returnsFailure() {
        OrganizationCreateRequestDTO dto = new OrganizationCreateRequestDTO();
        Organization newOrg = new Organization("ExistingOrg", "NewContact");

        when(centralMapper.toOrganization(dto)).thenReturn(newOrg);
        when(organizationRepository.save(newOrg)).thenThrow(new DataIntegrityViolationException("Duplicate entry 'ExistingOrg' for key 'name'"));

        Try<Organization> result = organizationManagement.create(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when save organization");
        verify(centralMapper).toOrganization(dto);
        verify(organizationRepository).save(newOrg);
    }

    // Tests for update
    @Test
    void update_success_validOrganization_returnsUpdatedOrganization() {
        Organization updatedInput = new Organization("NewName", "NewContact");
        updatedInput.setId(validId);
        Organization updatedOrganization = new Organization("NewName", "NewContact");
        updatedOrganization.setId(validId);

        when(organizationRepository.findById(validId)).thenReturn(Optional.of(validOrganization));
        when(centralMapper.toOrganizationUpdate(validOrganization, updatedInput)).thenReturn(updatedOrganization);
        when(organizationRepository.save(updatedOrganization)).thenReturn(updatedOrganization);

        Try<Organization> result = organizationManagement.update(updatedInput);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(updatedOrganization);
        verify(organizationRepository).findById(validId);
        verify(centralMapper).toOrganizationUpdate(validOrganization, updatedInput);
        verify(organizationRepository).save(updatedOrganization);
    }

    @Test
    void update_failure_organizationNotFound_returnsFailure() {
        Organization updatedInput = new Organization("NewName", "NewContact");
        updatedInput.setId(validId);

        when(organizationRepository.findById(validId)).thenReturn(Optional.empty());

        Try<Organization> result = organizationManagement.update(updatedInput);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when find organization to update with id: " + validId);
        verify(organizationRepository).findById(validId);
        verifyNoInteractions(centralMapper);
        verifyNoMoreInteractions(organizationRepository);
    }

    @Test
    void update_edgeCase_nullOrganization_throwsNullPointerException() {
        assertThatThrownBy(() -> organizationManagement.update(null))
                .isInstanceOf(NullPointerException.class)
                .hasNoCause();
        verifyNoInteractions(organizationRepository);
        verifyNoInteractions(centralMapper);
    }

    @Test
    void update_failure_duplicateName_returnsFailure() {
        Organization existingOrg = new Organization("ExistingOrg", "Contact");
        existingOrg.setId(2L);
        Organization inputOrg = new Organization("ExistingOrg", "NewContact");
        inputOrg.setId(1L);
        Organization currentOrg = new Organization("CurrentOrg", "OldContact");
        currentOrg.setId(1L);
        Organization updatedOrg = new Organization("ExistingOrg", "NewContact");
        updatedOrg.setId(1L);

        when(organizationRepository.findById(1L)).thenReturn(Optional.of(currentOrg));
        when(centralMapper.toOrganizationUpdate(currentOrg, inputOrg)).thenReturn(updatedOrg);
        when(organizationRepository.save(updatedOrg)).thenThrow(new DataIntegrityViolationException("Duplicate entry 'ExistingOrg' for key 'name'"));

        Try<Organization> result = organizationManagement.update(inputOrg);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when update organization");
        verify(organizationRepository).findById(1L);
        verify(centralMapper).toOrganizationUpdate(currentOrg, inputOrg);
        verify(organizationRepository).save(updatedOrg);
    }

    // Tests for delete
    @Test
    void delete_success_validId_returnsTrue() {
        Try<Boolean> result = organizationManagement.delete(validId);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(organizationRepository).deleteById(validId);
    }

    @Test
    void delete_failure_deleteError_returnsFailure() {
        doThrow(new RuntimeException("Delete error")).when(organizationRepository).deleteById(validId);

        Try<Boolean> result = organizationManagement.delete(validId);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when delete organization with id: " + validId);
        verify(organizationRepository).deleteById(validId);
    }

    @Test
    void delete_edgeCase_nullId_returnsFailure() {
        Try<Boolean> result = organizationManagement.delete(null);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when delete organization with id: null");
        verify(organizationRepository).deleteById(null);
    }

    // Tests for fetchAll
    @Test
    void fetchAll_success_returnsOrganizations() {
        List<Organization> organizations = Collections.singletonList(validOrganization);
        when(organizationRepository.findAll()).thenReturn(organizations);

        List<Organization> result = organizationManagement.fetchAll();

        assertThat(result).hasSize(1).containsExactly(validOrganization);
        verify(organizationRepository).findAll();
    }

    @Test
    void fetchAll_success_emptyList_returnsEmpty() {
        when(organizationRepository.findAll()).thenReturn(Collections.emptyList());

        List<Organization> result = organizationManagement.fetchAll();

        assertThat(result).isEmpty();
        verify(organizationRepository).findAll();
    }

    // Tests for fetchPage
    @Test
    void fetchPage_success_validDTO_returnsPage() {
        Page<Organization> page = new PageImpl<>(Collections.singletonList(validOrganization));
        when(organizationRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Organization> result = organizationManagement.fetchPage(fetchPageRequestDTO);

        assertThat(result.getContent()).hasSize(1).containsExactly(validOrganization);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(organizationRepository).findAll(any(Pageable.class));
    }

    @Test
    void fetchPage_success_emptyPage_returnsEmpty() {
        Page<Organization> emptyPage = new PageImpl<>(Collections.emptyList());
        when(organizationRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Page<Organization> result = organizationManagement.fetchPage(fetchPageRequestDTO);

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
        verify(organizationRepository).findAll(any(Pageable.class));
    }

    @Test
    void fetchPage_edgeCase_nullDTO_throwsNullPointerException() {
        assertThatThrownBy(() -> organizationManagement.fetchPage(null))
                .isInstanceOf(NullPointerException.class)
                .hasNoCause();
        verifyNoInteractions(organizationRepository);
    }

}