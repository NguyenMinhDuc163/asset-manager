package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Additional;
import com.ptit.asset.domain.Organization;
import com.ptit.asset.domain.User;
import com.ptit.asset.dto.request.AdditionalChangeStatusRequestDTO;
import com.ptit.asset.dto.request.AdditionalCreateRequestDTO;
import com.ptit.asset.dto.request.AdditionalUpdateRequestDTO;
import com.ptit.asset.repository.AdditionalRepository;
import com.ptit.asset.repository.OrganizationRepository;
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
    void getOne_success() { // mã test case 1: phương thức getOne thành công với id 1
        // Giả lập: Khi gọi findById(1L) trên additionalRepository, trả về một Optional chứa testAdditional
        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        // Gọi phương thức getOne với id = 1L và lưu kết quả (một Try<Additional>) vào biến result
        Try<Additional> result = additionalManagement.getOne(1L);
        // Kiểm tra: Xác nhận rằng result là thành công (không có lỗi xảy ra)
        assertThat(result.isSuccess()).isTrue();
        // Kiểm tra: Lấy đối tượng Additional từ result và xác nhận id của nó là 1L như kỳ vọng
        assertThat(result.get().getId()).isEqualTo(1L);
        // Xác minh: Đảm bảo rằng findById(1L) đã được gọi đúng 1 lần trên additionalRepository
        verify(additionalRepository, times(1)).findById(1L);
    }

    @Test
    void getOne_fail_notFound() { // mã test case 1: phương thức getOne thất bại với id 1
        when(additionalRepository.findById(1L)).thenReturn(Optional.empty());
        Try<Additional> result = additionalManagement.getOne(1L);
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when get additional by id: 1");
        verify(additionalRepository, times(1)).findById(1L);
    }

    // create
    @Test
    void create_success() { // mã test case 2: phương thức create thành công
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
    void create_fail_userNotFound() { // mã test case 2: phương thức create thất bại vì không tồn tại user thực hiện
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
    void create_fail_orgNotFound() { // mã test case 2: phương thức create thất bại vì không tồn tại tổ chức
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
    void create_fail_existingInProcess() {// mã test case 2: phương thức create thất bại vì tồn tại một đợt bổ sung khác đang diễn ra
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
    void update_fail_additionalNotFound() {
        // Nhánh: Additional không tồn tại
        AdditionalUpdateRequestDTO dto = new AdditionalUpdateRequestDTO();
        dto.setId(1L);

        when(additionalRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Additional> result = additionalManagement.update(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find additional to update with id: 1");
        verify(additionalRepository, times(1)).findById(1L);
        verifyNoInteractions(userRepository, organizationRepository, centralMapper);
    }

    @Test
    void update_success_noEmbedded() {
        // Nhánh: embedded = null, không kiểm tra user/org
        AdditionalUpdateRequestDTO dto = new AdditionalUpdateRequestDTO();
        dto.setId(1L);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(centralMapper.toAdditionalUpdate(testAdditional, dto)).thenReturn(testAdditional);
        when(additionalRepository.save(testAdditional)).thenReturn(testAdditional);

        Try<Additional> result = additionalManagement.update(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getUser().getId()).isEqualTo(1L);
        assertThat(result.get().getOrganization().getId()).isEqualTo(1L);
        verify(additionalRepository, times(1)).findById(1L);
        verify(centralMapper, times(1)).toAdditionalUpdate(testAdditional, dto);
        verify(additionalRepository, times(1)).save(testAdditional);
        verifyNoInteractions(userRepository, organizationRepository);
    }

    @Test
    void update_success_sameUserAndOrg() {
        // Nhánh: userId và organizationId trùng với hiện tại
        AdditionalUpdateRequestDTO dto = new AdditionalUpdateRequestDTO();
        dto.setId(1L);
        AdditionalUpdateRequestDTO.Embedded embedded = new AdditionalUpdateRequestDTO.Embedded();
        embedded.setUserId(1L);
        embedded.setOrganizationId(1L);
        dto.setEmbedded(embedded);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(centralMapper.toAdditionalUpdate(testAdditional, dto)).thenReturn(testAdditional);
        when(additionalRepository.save(testAdditional)).thenReturn(testAdditional);

        Try<Additional> result = additionalManagement.update(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getUser().getId()).isEqualTo(1L);
        assertThat(result.get().getOrganization().getId()).isEqualTo(1L);
        verify(additionalRepository, times(1)).findById(1L);
        verify(centralMapper, times(1)).toAdditionalUpdate(testAdditional, dto);
        verify(additionalRepository, times(1)).save(testAdditional);
        verifyNoInteractions(userRepository, organizationRepository);
    }

    @Test
    void update_fail_changeUser() {
        // Nhánh: Thử thay đổi user (userId khác hiện tại), phải thất bại
        AdditionalUpdateRequestDTO dto = new AdditionalUpdateRequestDTO();
        dto.setId(1L);
        AdditionalUpdateRequestDTO.Embedded embedded = new AdditionalUpdateRequestDTO.Embedded();
        embedded.setUserId(2L); // Khác hiện tại
        embedded.setOrganizationId(1L);
        dto.setEmbedded(embedded);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(centralMapper.toAdditionalUpdate(testAdditional, dto)).thenReturn(testAdditional);

        Try<Additional> result = additionalManagement.update(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Cannot change user for additional with id: 1");
        verify(additionalRepository, times(1)).findById(1L);
        verify(centralMapper, times(1)).toAdditionalUpdate(testAdditional, dto);
        verifyNoInteractions(userRepository, organizationRepository);
        verify(additionalRepository, never()).save(any(Additional.class));
    }

    @Test
    void update_fail_changeOrg() {
        // Nhánh: Thử thay đổi org (organizationId khác hiện tại), phải thất bại
        AdditionalUpdateRequestDTO dto = new AdditionalUpdateRequestDTO();
        dto.setId(1L);
        AdditionalUpdateRequestDTO.Embedded embedded = new AdditionalUpdateRequestDTO.Embedded();
        embedded.setUserId(1L);
        embedded.setOrganizationId(2L); // Khác hiện tại
        dto.setEmbedded(embedded);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(centralMapper.toAdditionalUpdate(testAdditional, dto)).thenReturn(testAdditional);

        Try<Additional> result = additionalManagement.update(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Cannot change organization for additional with id: 1");
        verify(additionalRepository, times(1)).findById(1L);
        verify(centralMapper, times(1)).toAdditionalUpdate(testAdditional, dto);
        verifyNoInteractions(userRepository, organizationRepository);
        verify(additionalRepository, never()).save(any(Additional.class));
    }

    @Test
    void update_fail_userIdNull() {
        // Nhánh: userId = null, phải thất bại
        AdditionalUpdateRequestDTO dto = new AdditionalUpdateRequestDTO();
        dto.setId(1L);
        AdditionalUpdateRequestDTO.Embedded embedded = new AdditionalUpdateRequestDTO.Embedded();
        embedded.setUserId(null);
        embedded.setOrganizationId(1L);
        dto.setEmbedded(embedded);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(centralMapper.toAdditionalUpdate(testAdditional, dto)).thenReturn(testAdditional);

        Try<Additional> result = additionalManagement.update(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("UserId cannot be null");
        verify(additionalRepository, times(1)).findById(1L);
        verify(centralMapper, times(1)).toAdditionalUpdate(testAdditional, dto);
        verifyNoInteractions(userRepository, organizationRepository);
        verify(additionalRepository, never()).save(any(Additional.class));
    }

    @Test
    void update_fail_organizationIdNull() {
        // Nhánh: organizationId = null, phải thất bại
        AdditionalUpdateRequestDTO dto = new AdditionalUpdateRequestDTO();
        dto.setId(1L);
        AdditionalUpdateRequestDTO.Embedded embedded = new AdditionalUpdateRequestDTO.Embedded();
        embedded.setUserId(1L);
        embedded.setOrganizationId(null);
        dto.setEmbedded(embedded);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(centralMapper.toAdditionalUpdate(testAdditional, dto)).thenReturn(testAdditional);

        Try<Additional> result = additionalManagement.update(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("OrganizationId cannot be null");
        verify(additionalRepository, times(1)).findById(1L);
        verify(centralMapper, times(1)).toAdditionalUpdate(testAdditional, dto);
        verifyNoInteractions(userRepository, organizationRepository);
        verify(additionalRepository, never()).save(any(Additional.class));
    }

    @Test
    void update_fail_bothIdsNull() {
        // Nhánh: Cả userId và organizationId null, phải thất bại
        AdditionalUpdateRequestDTO dto = new AdditionalUpdateRequestDTO();
        dto.setId(1L);
        AdditionalUpdateRequestDTO.Embedded embedded = new AdditionalUpdateRequestDTO.Embedded();
        embedded.setUserId(null);
        embedded.setOrganizationId(null);
        dto.setEmbedded(embedded);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(centralMapper.toAdditionalUpdate(testAdditional, dto)).thenReturn(testAdditional);

        Try<Additional> result = additionalManagement.update(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("UserId cannot be null");
        verify(additionalRepository, times(1)).findById(1L);
        verify(centralMapper, times(1)).toAdditionalUpdate(testAdditional, dto);
        verifyNoInteractions(userRepository, organizationRepository);
        verify(additionalRepository, never()).save(any(Additional.class));
    }

    @Test
    void update_success_nullIds_currentBehavior() {
        // Nhánh: userId = null, organizationId = null theo hành vi hiện tại (thành công)
        AdditionalUpdateRequestDTO dto = new AdditionalUpdateRequestDTO();
        dto.setId(1L);
        AdditionalUpdateRequestDTO.Embedded embedded = new AdditionalUpdateRequestDTO.Embedded();
        embedded.setUserId(null);
        embedded.setOrganizationId(null);
        dto.setEmbedded(embedded);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(centralMapper.toAdditionalUpdate(testAdditional, dto)).thenReturn(testAdditional);
        when(additionalRepository.save(testAdditional)).thenReturn(testAdditional);

        Try<Additional> result = additionalManagement.update(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getUser().getId()).isEqualTo(1L);
        assertThat(result.get().getOrganization().getId()).isEqualTo(1L);
        verify(additionalRepository, times(1)).findById(1L);
        verify(additionalRepository, times(1)).save(testAdditional);
        verifyNoInteractions(userRepository, organizationRepository);
    }
    // delete
    /*@Test
    void delete_success_inProcess() {
        // Nhánh: Xóa Additional chưa hoàn tất thành công
        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        doNothing().when(additionalRepository).deleteById(1L);

        Try<Boolean> result = additionalManagement.delete(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(additionalRepository, times(1)).findById(1L);
        verify(additionalRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(additionalRepository);
    }

    @Test
    void delete_fail_completed() {
        // Nhánh: Thất bại khi xóa Additional đã hoàn tất
        Additional completed = new Additional();
        completed.setId(1L);
        completed.setInProcess(false);
        completed.setUser(testUser);
        completed.setOrganization(testOrg);
        completed.setTime(Instant.now());

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(completed));

        Try<Boolean> result = additionalManagement.delete(1L);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Cannot delete completed additional with id: 1");
        verify(additionalRepository, times(1)).findById(1L);
        verify(additionalRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(additionalRepository);
    }

    @Test
    void delete_fail_notFound() {
        // Nhánh: Thất bại khi Additional không tồn tại
        when(additionalRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Boolean> result = additionalManagement.delete(1L);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find additional with id: 1");
        verify(additionalRepository, times(1)).findById(1L);
        verify(additionalRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(additionalRepository);
    }*/

    // fetchAll
    @Test
    void fetchAll_success() { // mã test case 5: lấy danh sách thành công
        when(additionalRepository.findAll()).thenReturn(Collections.singletonList(testAdditional));

        List<Additional> result = additionalManagement.fetchAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(additionalRepository, times(1)).findAll();
    }

    @Test
    void fetchAll_empty() { // mã test case 5: lấy ra danh sách rỗng
        when(additionalRepository.findAll()).thenReturn(Collections.emptyList());

        List<Additional> result = additionalManagement.fetchAll();

        assertThat(result).isEmpty();
        verify(additionalRepository, times(1)).findAll();
    }

    // changeStatus
    @Test
    void changeStatus_success() { // mã test case 6: đổi trạng thái từ true sang false thành công
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
    void changeStatus_fail_notFound() { // mã test case 6: đổi trạng thái thất bại không tìm thấy Additional theo ID
        AdditionalChangeStatusRequestDTO dto = new AdditionalChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(false);

        when(additionalRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Boolean> result = additionalManagement.changeStatus(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find additional with id:1");
        verify(additionalRepository, times(1)).findById(1L);
    }

    @Test
    void changeStatus_fail_sameStatus() { // mã test case 6: đổi trạng thái thất bại do vẫn giữ nguyên trạng thái True -> True
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
    void changeStatus_fail_turnBackToTrue() { // mã test case 6: đổi trạng thái thất bại do từ False -> True
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