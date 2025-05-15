package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Campus;
import com.ptit.asset.domain.enumeration.CampusType;
import com.ptit.asset.dto.request.CampusCreateRequestDTO;
import com.ptit.asset.repository.CampusRepository;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampusManagementImplTest {

    @Mock
    private CampusRepository campusRepository;

    @Mock
    private CentralMapper centralMapper;

    @InjectMocks
    private CampusManagementImpl campusManagement;

    private Campus validCampus;
    private CampusCreateRequestDTO createRequestDTO;
    private Long validId;

    @BeforeEach
    void setUp() {
        validId = 1L;
        validCampus = new Campus("CampusA", "Description", "0123456789", "campus@example.com", CampusType.HEADQUARTERS, "123 Street", "http://map.url");
        validCampus.setId(validId);
        createRequestDTO = new CampusCreateRequestDTO();
    }

    // Tests for getOne
    @Test
    void getOne_success_validId_returnsCampus() {
        when(campusRepository.findById(validId)).thenReturn(Optional.of(validCampus));

        Try<Campus> result = campusManagement.getOne(validId);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(validCampus);
        verify(campusRepository).findById(validId);
    }

    @Test
    void getOne_failure_idNotFound_returnsFailure() {
        when(campusRepository.findById(validId)).thenReturn(Optional.empty());

        Try<Campus> result = campusManagement.getOne(validId);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when get campus by id: " + validId);
        verify(campusRepository).findById(validId);
    }

    @Test
    void getOne_edgeCase_nullId_returnsFailure() {
        Try<Campus> result = campusManagement.getOne(null);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).isInstanceOf(Exception.class);
        assertThat(result.getCause()).hasMessage("Failure when get campus by id: null");
        verify(campusRepository).findById(null);
    }

    // Tests for create
    @Test
    void create_success_validDTO_returnsCampus() {
        when(centralMapper.toCampus(createRequestDTO)).thenReturn(validCampus);
        when(campusRepository.save(validCampus)).thenReturn(validCampus);

        Try<Campus> result = campusManagement.create(createRequestDTO);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(validCampus);
        verify(centralMapper).toCampus(createRequestDTO);
        verify(campusRepository).save(validCampus);
    }

    @Test
    void create_failure_mappingError_returnsFailure() {
        when(centralMapper.toCampus(createRequestDTO)).thenThrow(new RuntimeException("Mapping error"));

        Try<Campus> result = campusManagement.create(createRequestDTO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when save campus");
        verify(centralMapper).toCampus(createRequestDTO);
        verifyNoInteractions(campusRepository);
    }

    @Test
    void create_edgeCase_nullDTO_returnsFailure() {
        when(centralMapper.toCampus(null)).thenThrow(new NullPointerException("DTO is null"));

        Try<Campus> result = campusManagement.create(null);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when save campus");
        verify(centralMapper).toCampus(null);
        verifyNoInteractions(campusRepository);
    }

    @Test
    void create_failure_duplicateName_returnsFailure() {
        Campus newCampus = new Campus("CampusA", "New Description", "0987654321", "newcampus@example.com", CampusType.FACILITY, "456 Street", null);
        when(centralMapper.toCampus(createRequestDTO)).thenReturn(newCampus);
        when(campusRepository.save(newCampus)).thenThrow(new DataIntegrityViolationException("Duplicate entry 'CampusA' for key 'name'"));

        Try<Campus> result = campusManagement.create(createRequestDTO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when save campus");
        verify(centralMapper).toCampus(createRequestDTO);
        verify(campusRepository).save(newCampus);
    }

    @Test
    void create_failure_duplicatePhone_returnsFailure() {
        Campus newCampus = new Campus("CampusB", "New Description", "0123456789", "newcampus@example.com", CampusType.FACILITY, "456 Street", null);
        when(centralMapper.toCampus(createRequestDTO)).thenReturn(newCampus);
        when(campusRepository.save(newCampus)).thenThrow(new DataIntegrityViolationException("Duplicate entry '0123456789' for key 'contact_phone'"));

        Try<Campus> result = campusManagement.create(createRequestDTO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when save campus");
        verify(centralMapper).toCampus(createRequestDTO);
        verify(campusRepository).save(newCampus);
    }

    @Test
    void create_failure_duplicateEmail_returnsFailure() {
        Campus newCampus = new Campus("CampusB", "New Description", "0987654321", "campus@example.com", CampusType.FACILITY, "456 Street", null);
        when(centralMapper.toCampus(createRequestDTO)).thenReturn(newCampus);
        when(campusRepository.save(newCampus)).thenThrow(new DataIntegrityViolationException("Duplicate entry 'campus@example.com' for key 'contact_email'"));

        Try<Campus> result = campusManagement.create(createRequestDTO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when save campus");
        verify(centralMapper).toCampus(createRequestDTO);
        verify(campusRepository).save(newCampus);
    }

    // Tests for update
    @Test
    void update_success_validCampus_returnsUpdatedCampus() {
        Campus updatedInput = new Campus("CampusB", "Updated Description", "0987654321", "newcampus@example.com", CampusType.FACILITY, "456 Street", null);
        updatedInput.setId(validId);
        Campus updatedCampus = new Campus("CampusB", "Updated Description", "0987654321", "newcampus@example.com", CampusType.FACILITY, "456 Street", null);
        updatedCampus.setId(validId);

        when(campusRepository.findById(validId)).thenReturn(Optional.of(validCampus));
        when(centralMapper.toCampusUpdate(validCampus, updatedInput)).thenReturn(updatedCampus);
        when(campusRepository.save(updatedCampus)).thenReturn(updatedCampus);

        Try<Campus> result = campusManagement.update(updatedInput);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(updatedCampus);
        verify(campusRepository).findById(validId);
        verify(centralMapper).toCampusUpdate(validCampus, updatedInput);
        verify(campusRepository).save(updatedCampus);
    }

    @Test
    void update_failure_campusNotFound_returnsFailure() {
        Campus updatedInput = new Campus("CampusB", "Updated Description", "0987654321", "newcampus@example.com", CampusType.FACILITY, "456 Street", null);
        updatedInput.setId(validId);

        when(campusRepository.findById(validId)).thenReturn(Optional.empty());

        Try<Campus> result = campusManagement.update(updatedInput);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when find campus to update with id: " + validId);
        verify(campusRepository).findById(validId);
        verifyNoInteractions(centralMapper);
        verifyNoMoreInteractions(campusRepository);
    }

    @Test
    void update_edgeCase_nullCampus_throwsNullPointerException() {
        assertThatThrownBy(() -> campusManagement.update(null))
                .isInstanceOf(NullPointerException.class)
                .hasNoCause();
        verifyNoInteractions(campusRepository);
        verifyNoInteractions(centralMapper);
    }

    @Test
    void update_failure_duplicateName_returnsFailure() {
        Campus inputCampus = new Campus("CampusX", "New Description", "0987654321", "newcampus@example.com", CampusType.FACILITY, "456 Street", null);
        inputCampus.setId(1L);
        Campus currentCampus = new Campus("CampusA", "Description", "0123456789", "campus@example.com", CampusType.HEADQUARTERS, "123 Street", null);
        currentCampus.setId(1L);
        Campus updatedCampus = new Campus("CampusX", "New Description", "0987654321", "newcampus@example.com", CampusType.FACILITY, "456 Street", null);
        updatedCampus.setId(1L);

        when(campusRepository.findById(1L)).thenReturn(Optional.of(currentCampus));
        when(centralMapper.toCampusUpdate(currentCampus, inputCampus)).thenReturn(updatedCampus);
        when(campusRepository.save(updatedCampus)).thenThrow(new DataIntegrityViolationException("Duplicate entry 'CampusX' for key 'name'"));

        Try<Campus> result = campusManagement.update(inputCampus);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when update campus");
        verify(campusRepository).findById(1L);
        verify(centralMapper).toCampusUpdate(currentCampus, inputCampus);
        verify(campusRepository).save(updatedCampus);
    }

    @Test
    void update_failure_duplicatePhone_returnsFailure() {
        Campus inputCampus = new Campus("CampusB", "New Description", "0123456789", "newcampus@example.com", CampusType.FACILITY, "456 Street", null);
        inputCampus.setId(1L);
        Campus currentCampus = new Campus("CampusA", "Description", "0987654321", "campus@example.com", CampusType.HEADQUARTERS, "123 Street", null);
        currentCampus.setId(1L);
        Campus updatedCampus = new Campus("CampusB", "New Description", "0123456789", "newcampus@example.com", CampusType.FACILITY, "456 Street", null);
        updatedCampus.setId(1L);

        when(campusRepository.findById(1L)).thenReturn(Optional.of(currentCampus));
        when(centralMapper.toCampusUpdate(currentCampus, inputCampus)).thenReturn(updatedCampus);
        when(campusRepository.save(updatedCampus)).thenThrow(new DataIntegrityViolationException("Duplicate entry '0123456789' for key 'contact_phone'"));

        Try<Campus> result = campusManagement.update(inputCampus);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when update campus");
        verify(campusRepository).findById(1L);
        verify(centralMapper).toCampusUpdate(currentCampus, inputCampus);
        verify(campusRepository).save(updatedCampus);
    }

    @Test
    void update_failure_duplicateEmail_returnsFailure() {
        Campus inputCampus = new Campus("CampusB", "New Description", "0987654321", "campus@example.com", CampusType.FACILITY, "456 Street", null);
        inputCampus.setId(1L);
        Campus currentCampus = new Campus("CampusA", "Description", "0123456789", "other@example.com", CampusType.HEADQUARTERS, "123 Street", null);
        currentCampus.setId(1L);
        Campus updatedCampus = new Campus("CampusB", "New Description", "0987654321", "campus@example.com", CampusType.FACILITY, "456 Street", null);
        updatedCampus.setId(1L);

        when(campusRepository.findById(1L)).thenReturn(Optional.of(currentCampus));
        when(centralMapper.toCampusUpdate(currentCampus, inputCampus)).thenReturn(updatedCampus);
        when(campusRepository.save(updatedCampus)).thenThrow(new DataIntegrityViolationException("Duplicate entry 'campus@example.com' for key 'contact_email'"));

        Try<Campus> result = campusManagement.update(inputCampus);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when update campus");
        verify(campusRepository).findById(1L);
        verify(centralMapper).toCampusUpdate(currentCampus, inputCampus);
        verify(campusRepository).save(updatedCampus);
    }

    // Tests for delete
    @Test
    void delete_success_validId_returnsTrue() {
        Try<Boolean> result = campusManagement.delete(validId);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(campusRepository).deleteById(validId);
    }

    @Test
    void delete_failure_deleteError_returnsFailure() {
        doThrow(new RuntimeException("Delete error")).when(campusRepository).deleteById(validId);

        Try<Boolean> result = campusManagement.delete(validId);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when delete campus with id: " + validId);
        verify(campusRepository).deleteById(validId);
    }

    @Test
    void delete_edgeCase_nullId_returnsFailure() {
        Try<Boolean> result = campusManagement.delete(null);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).isInstanceOf(Exception.class);
        assertThat(result.getCause()).hasMessage("Failure when delete campus with id: null");
        verify(campusRepository).deleteById(null);
    }

    // Tests for fetchAll
    @Test
    void fetchAll_success_returnsCampuses() {
        List<Campus> campuses = Collections.singletonList(validCampus);
        when(campusRepository.findAll()).thenReturn(campuses);

        List<Campus> result = campusManagement.fetchAll();

        assertThat(result).hasSize(1).containsExactly(validCampus);
        verify(campusRepository).findAll();
    }

    @Test
    void fetchAll_success_emptyList_returnsEmpty() {
        when(campusRepository.findAll()).thenReturn(Collections.emptyList());

        List<Campus> result = campusManagement.fetchAll();

        assertThat(result).isEmpty();
        verify(campusRepository).findAll();
    }
}