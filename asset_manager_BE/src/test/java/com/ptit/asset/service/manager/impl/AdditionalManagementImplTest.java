package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Additional;
import com.ptit.asset.domain.Material;
import com.ptit.asset.domain.Organization;
import com.ptit.asset.domain.User;
import com.ptit.asset.domain.enumeration.MaterialStatus;
import com.ptit.asset.dto.request.AdditionalChangeStatusRequestDTO;
import com.ptit.asset.dto.request.AdditionalCreateRequestDTO;
import com.ptit.asset.dto.request.AdditionalUpdateRequestDTO;
import com.ptit.asset.repository.AdditionalRepository;
import com.ptit.asset.repository.MaterialRepository;
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
    private MaterialRepository materialRepository;

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
        testAdditional.setTime(Instant.now());
    }

    // STT 1: Lấy thông tin của 1 đợt bổ sung
    @Test
    void getOne_success() {
        // STT 1: Kiểm tra trường hợp thành công của phương thức getOne theo ID
        // Test case sẽ: Giả lập tìm thấy Additional với ID 1 và kiểm tra kết quả trả về đúng
        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        Try<Additional> result = additionalManagement.getOne(1L);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(additionalRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(additionalRepository);
    }

    @Test
    void getOne_fail_notFound() {
        // STT 1: Kiểm tra trường hợp thất bại của phương thức getOne theo ID
        // Test case sẽ: Giả lập không tìm thấy Additional với ID 1 và kiểm tra lỗi trả về
        when(additionalRepository.findById(1L)).thenReturn(Optional.empty());
        Try<Additional> result = additionalManagement.getOne(1L);
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when get additional by id: 1");
        verify(additionalRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(additionalRepository);
    }

    // STT 2: Tạo mới 1 đợt bổ sung
    @Test
    void create_success() {
        // STT 2: Tạo mới một Additional thành công
        // Test case sẽ: Giả lập user, org tồn tại, không có đợt bổ sung đang diễn ra, và kiểm tra tạo thành công
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
        verifyNoMoreInteractions(userRepository, organizationRepository, additionalRepository);
    }

    @Test
    void create_success_existingNotInProcess() {
        // STT 2: Tạo mới một Additional thành công khi có đợt bổ sung đã hoàn tất
        // Test case sẽ: Giả lập user, org tồn tại, có một Additional với inProcess = false, và kiểm tra tạo thành công
        AdditionalCreateRequestDTO dto = new AdditionalCreateRequestDTO();
        AdditionalCreateRequestDTO.Embedded embedded = new AdditionalCreateRequestDTO.Embedded();
        embedded.setUserId(1L);
        embedded.setOrganizationId(1L);
        dto.setEmbedded(embedded);
        dto.setTime(Instant.now());

        Additional completedAdditional = new Additional();
        completedAdditional.setId(2L);
        completedAdditional.setInProcess(false);
        completedAdditional.setUser(testUser);
        completedAdditional.setOrganization(testOrg);
        completedAdditional.setTime(Instant.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(testOrg));
        when(additionalRepository.findAll()).thenReturn(Collections.singletonList(completedAdditional));
        when(centralMapper.toAdditional(dto, testUser, testOrg)).thenReturn(testAdditional);
        when(additionalRepository.save(any(Additional.class))).thenReturn(testAdditional);

        Try<Additional> result = additionalManagement.create(dto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getInProcess()).isTrue();
        verify(userRepository, times(1)).findById(1L);
        verify(organizationRepository, times(1)).findById(1L);
        verify(additionalRepository, times(1)).findAll();
        verify(additionalRepository, times(1)).save(any(Additional.class));
        verifyNoMoreInteractions(userRepository, organizationRepository, additionalRepository);
    }

    @Test
    void create_fail_userNotFound() {
        // STT 2: Tạo mới thất bại vì không tồn tại user thực hiện
        // Test case sẽ: Giả lập user không tồn tại và kiểm tra lỗi trả về
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
    void create_fail_orgNotFound() {
        // STT 2: Tạo mới thất bại vì không tồn tại tổ chức
        // Test case sẽ: Giả lập org không tồn tại và kiểm tra lỗi trả về
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
    void create_fail_existingInProcess() {
        // STT 2: Tạo mới thất bại vì tồn tại một đợt bổ sung đang diễn ra
        // Test case sẽ: Giả lập có đợt bổ sung đang diễn ra và kiểm tra lỗi trả về
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
        verifyNoMoreInteractions(userRepository, organizationRepository, additionalRepository);
    }

    // STT 3: Cập nhật 1 đợt bổ sung
    @Test
    void update_success_noEmbedded() {
        // STT 3: Kiểm tra phương thức update thành công khi không có thay đổi user hoặc org
        // Test case sẽ: Giả lập không gửi embedded, cập nhật các trường khác và kiểm tra thành công
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
        verifyNoMoreInteractions(additionalRepository, centralMapper);
    }


    @Test
    void update_fail_changeUser() {
        // STT 3: Kiểm tra phương thức update thất bại khi cập nhật user mới
        // Test case sẽ: Giả lập gửi userId khác hiện tại và kiểm tra lỗi trả về
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
        verifyNoMoreInteractions(additionalRepository, centralMapper);
    }

    @Test
    void update_fail_changeOrg() {
        // STT 3: Kiểm tra phương thức update thất bại khi cập nhật org mới
        // Test case sẽ: Giả lập gửi organizationId khác hiện tại và kiểm tra lỗi trả về
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
        verifyNoMoreInteractions(additionalRepository, centralMapper);
    }

    @Test
    void update_fail_userIdNull() {
        // STT 3: Kiểm tra phương thức update thất bại khi user null
        // Test case sẽ: Giả lập gửi userId là null và kiểm tra lỗi trả về
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
        verifyNoMoreInteractions(additionalRepository, centralMapper);
    }

    @Test
    void update_fail_organizationIdNull() {
        // STT 3: Kiểm tra phương thức update thất bại khi org null
        // Test case sẽ: Giả lập gửi organizationId là null và kiểm tra lỗi trả về
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
        verifyNoMoreInteractions(additionalRepository, centralMapper);
    }

    @Test
    void update_fail_bothIdsNull() {
        // STT 3: Kiểm tra phương thức update thất bại khi cả user và org null
        // Test case sẽ: Giả lập gửi cả userId và organizationId là null và kiểm tra lỗi trả về
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
        verifyNoMoreInteractions(additionalRepository, centralMapper);
    }

    @Test
    void update_fail_additionalNotFound() {
        // STT 3: Kiểm tra phương thức update thất bại khi không tìm thấy Additional
        // Test case sẽ: Giả lập không tìm thấy Additional với ID 1 và kiểm tra lỗi trả về
        AdditionalUpdateRequestDTO dto = new AdditionalUpdateRequestDTO();
        dto.setId(1L);

        when(additionalRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Additional> result = additionalManagement.update(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find additional to update with id: 1");
        verify(additionalRepository, times(1)).findById(1L);
        verifyNoInteractions(userRepository, organizationRepository, centralMapper);
        verifyNoMoreInteractions(additionalRepository);
    }

    // STT 4: Xóa 1 đợt bổ sung
    @Test
    void delete_success() {
        // STT 4: Kiểm tra phương thức delete thành công khi xóa một Additional theo ID
        // Test case sẽ: Giả lập gọi deleteById thành công và kiểm tra kết quả trả về
        doNothing().when(additionalRepository).deleteById(1L);

        Try<Boolean> result = additionalManagement.delete(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(additionalRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(additionalRepository);
    }

    @Test
    void delete_fail_has_materials() {
        // STT 4: Kiểm tra phương thức delete thất bại khi Additional có cơ sở vật chất (Material)
        // Test case sẽ: Giả lập một Additional đã có nhiều Material bổ sung để kiểm tra xóa
        // Dữ liệu mẫu: Tạo một Additional và hai Material liên kết để minh họa tình huống
        Additional additional = new Additional();
        additional.setId(1L);

        Material material1 = new Material();
        material1.setId(1L);
        material1.setCredentialCode("MAT001");
        material1.setStatus(MaterialStatus.IN_USED);
        material1.setTimeStartDepreciation(Instant.now());
        material1.setAdditional(additional);

        Material material2 = new Material();
        material2.setId(2L);
        material2.setCredentialCode("MAT002");
        material2.setStatus(MaterialStatus.IN_USED);
        material2.setTimeStartDepreciation(Instant.now());
        material2.setAdditional(additional);

        doNothing().when(additionalRepository).deleteById(1L);
        Try<Boolean> result = additionalManagement.delete(1L);

        assertThat(result.isFailure()).isTrue(); // Lỗi: Thành công nhưng không đúng nghiệp vụ
        assertThat(result.get()).isTrue();
        verify(additionalRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(additionalRepository);
    }

    // STT 5: Lấy tất cả đợt bổ sung
    @Test
    void fetchAll_success() {
        // STT 5: Kiểm tra phương thức fetchAll thành công khi trả về danh sách các đợt bổ sung
        // Test case sẽ: Giả lập danh sách có 1 Additional và kiểm tra kết quả trả về
        when(additionalRepository.findAll()).thenReturn(Collections.singletonList(testAdditional));

        List<Additional> result = additionalManagement.fetchAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(additionalRepository, times(1)).findAll();
        verifyNoMoreInteractions(additionalRepository);
    }

    @Test
    void fetchAll_empty() {
        // STT 5: Kiểm tra phương thức fetchAll thành công khi trả về danh sách rỗng
        // Test case sẽ: Giả lập danh sách rỗng và kiểm tra kết quả trả về
        when(additionalRepository.findAll()).thenReturn(Collections.emptyList());

        List<Additional> result = additionalManagement.fetchAll();

        assertThat(result).isEmpty();
        verify(additionalRepository, times(1)).findAll();
        verifyNoMoreInteractions(additionalRepository);
    }

    // STT 6: Thay đổi trạng thái đợt bổ sung
    @Test
    void changeStatus_success() {
        // STT 6: Kiểm tra phương thức changeStatus thành công khi thay đổi trạng thái của Additional từ true sang false
        // Test case sẽ: Giả lập đổi trạng thái từ true sang false và kiểm tra thành công
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
        verifyNoMoreInteractions(additionalRepository);
    }

    @Test
    void changeStatus_fail_notFound() {
        // STT 6: Kiểm tra phương thức changeStatus thất bại khi không tìm thấy Additional theo ID
        // Test case sẽ: Giả lập không tìm thấy Additional và kiểm tra lỗi trả về
        AdditionalChangeStatusRequestDTO dto = new AdditionalChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(false);

        when(additionalRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Boolean> result = additionalManagement.changeStatus(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Failure when find additional with id:1");
        verify(additionalRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(additionalRepository);
    }

    @Test
    void changeStatus_fail_sameStatus() {
        // STT 6: Kiểm tra phương thức changeStatus thất bại khi trạng thái mới trùng với trạng thái hiện tại (true)
        // Test case sẽ: Giả lập đổi trạng thái từ true sang true và kiểm tra lỗi trả về
        AdditionalChangeStatusRequestDTO dto = new AdditionalChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(true);

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));

        Try<Boolean> result = additionalManagement.changeStatus(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("Nothing change with current status");
        verify(additionalRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(additionalRepository);
    }

    @Test
    void changeStatus_fail_turnBackToTrue() {
        // STT 6: Kiểm tra phương thức changeStatus thất bại khi cố gắng đổi trạng thái từ false về true
        // Test case sẽ: Giả lập đổi trạng thái từ false sang true và kiểm tra lỗi trả về
        AdditionalChangeStatusRequestDTO dto = new AdditionalChangeStatusRequestDTO();
        dto.setId(1L);
        dto.setStatus(true);

        Additional inactive = new Additional();
        inactive.setId(1L);
        inactive.setInProcess(false);
        inactive.setUser(testUser);
        inactive.setOrganization(testOrg);
        inactive.setTime(Instant.now());

        when(additionalRepository.findById(1L)).thenReturn(Optional.of(inactive));

        Try<Boolean> result = additionalManagement.changeStatus(dto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).contains("not allow turn back");
        verify(additionalRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(additionalRepository);
    }
}