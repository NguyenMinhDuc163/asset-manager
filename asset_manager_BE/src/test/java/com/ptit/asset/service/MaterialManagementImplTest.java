package com.ptit.asset.service.manager.impl;

import com.google.zxing.WriterException;
import com.ptit.asset.domain.*;
import com.ptit.asset.domain.enumeration.TimeAllocationType;
import com.ptit.asset.dto.request.FetchPageMaterialRequestDTO;
import com.ptit.asset.dto.request.MaterialCreateRequestDTO;
import com.ptit.asset.dto.request.MaterialFilterRequestDTO;
import com.ptit.asset.dto.request.MaterialUpdateRequestDTO;
import com.ptit.asset.dto.response.MaterialResponseDTO;
import com.ptit.asset.repository.*;
import com.ptit.asset.repository.data.Statistical;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import com.ptit.asset.util.QRCodeUtil;
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

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Class test cho MaterialManagementImpl, kiểm tra các chức năng quản lý tài sản (Material).
 */
@ExtendWith(MockitoExtension.class)
class MaterialManagementImplTest {

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AdditionalRepository additionalRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CentralMapper centralMapper;

    @Mock
    private QRCodeUtil qrCodeUtil;

    @InjectMocks
    private MaterialManagementImpl materialManagement;

    private Material testMaterial;
    private Product testProductYear;
    private Product testProductMonth;
    private Additional testAdditional;
    private Place testPlace;
    private User testUser;
    private MaterialCreateRequestDTO createDto;
    private MaterialUpdateRequestDTO updateDto;

    @BeforeEach
    void setUp() {
        // Khởi tạo dữ liệu cơ bản
        testMaterial = new Material();
        testMaterial.setId(1L);
        testMaterial.setCredentialCode("CRED001");
        testMaterial.setTimeStartDepreciation(Instant.now());

        testProductYear = new Product();
        testProductYear.setId(1L);
        testProductYear.setDepreciationRate(10.0);
        testProductYear.setTimeAllocationType(TimeAllocationType.YEAR);
        testMaterial.setProduct(testProductYear);

        testProductMonth = new Product();
        testProductMonth.setId(2L);
        testProductMonth.setDepreciationRate(5.0);
        testProductMonth.setTimeAllocationType(TimeAllocationType.MONTH);

        testAdditional = new Additional();
        testAdditional.setId(1L);

        testPlace = new Place();
        testPlace.setId(1L);

        testUser = new User();
        testUser.setId(1L);

        testMaterial.setCurrentPlace(testPlace);
        testMaterial.setUser(testUser);
        testMaterial.setAdditional(testAdditional);

        // DTO cho tạo mới
        createDto = new MaterialCreateRequestDTO();
        createDto.setCredentialCode("CRED001");
        MaterialCreateRequestDTO.Embedded embeddedCreate = new MaterialCreateRequestDTO.Embedded();
        embeddedCreate.setProductId(1L);
        embeddedCreate.setAdditionalId(1L);
        embeddedCreate.setPlaceId(1L);
        embeddedCreate.setUserId(1L);
        createDto.setEmbedded(embeddedCreate);

        // DTO cho cập nhật
        updateDto = new MaterialUpdateRequestDTO();
        updateDto.setId(1L);
        updateDto.setCredentialCode("CRED001");
        MaterialUpdateRequestDTO.Embedded embeddedUpdate = new MaterialUpdateRequestDTO.Embedded();
        embeddedUpdate.setProductId(1L);
        embeddedUpdate.setAdditionalId(1L);
        embeddedUpdate.setPlaceId(1L);
        embeddedUpdate.setUserId(1L);
        updateDto.setEmbedded(embeddedUpdate);
    }

    // Test cho phương thức getOne (Tìm kiếm một tài sản)
    @Test
    void testGetOne_Success() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));

        Try<Material> result = materialManagement.getOne(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(materialRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOne_NotFound() {
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Material> result = materialManagement.getOne(1L);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when get material by id: 1");
        verify(materialRepository, times(1)).findById(1L);
    }

    // Test cho phương thức create
    @Test
    void testCreate_Success_WithPlaceAndUser() throws WriterException, IOException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProductYear));
        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(placeRepository.findById(1L)).thenReturn(Optional.of(testPlace));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(centralMapper.toMaterial(any(), any(), any(), any(), any())).thenReturn(testMaterial);
        when(materialRepository.save(any(Material.class))).thenReturn(testMaterial);
        when(qrCodeUtil.generateQRCode("CRED001", "1", 100, 100)).thenReturn(new byte[]{1, 2, 3});

        Try<Material> result = materialManagement.create(createDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(additionalRepository, times(1)).findById(1L);
        verify(placeRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void testCreate_ProductNotFound() throws WriterException, IOException {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Material> result = materialManagement.create(createDto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when find product by id: 1");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testCreate_AdditionalNotFound() throws WriterException, IOException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProductYear));
        when(additionalRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Material> result = materialManagement.create(createDto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when find additional by id: 1");
        verify(productRepository, times(1)).findById(1L);
        verify(additionalRepository, times(1)).findById(1L);
    }

    @Test
    void testCreate_PlaceNotFound() throws WriterException, IOException {
        createDto.getEmbedded().setPlaceId(2L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProductYear));
        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(placeRepository.findById(2L)).thenReturn(Optional.empty());

        Try<Material> result = materialManagement.create(createDto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when find place by id: 2");
        verify(placeRepository, times(1)).findById(2L);
    }

    @Test
    void testCreate_UserNotFound() throws WriterException, IOException {
        createDto.getEmbedded().setUserId(2L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProductYear));
        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(placeRepository.findById(1L)).thenReturn(Optional.of(testPlace));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Try<Material> result = materialManagement.create(createDto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when find user by id: 2");
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testCreate_NullPlaceAndUser() throws WriterException, IOException {
        createDto.getEmbedded().setPlaceId(null);
        createDto.getEmbedded().setUserId(null);
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProductYear));
        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(centralMapper.toMaterial(any(), any(), any(), eq(null), eq(null))).thenReturn(testMaterial);
        when(materialRepository.save(any(Material.class))).thenReturn(testMaterial);
        when(qrCodeUtil.generateQRCode("CRED001", "1", 100, 100)).thenReturn(new byte[]{1, 2, 3});

        Try<Material> result = materialManagement.create(createDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void testCreate_QRCodeGenerationFails() throws WriterException, IOException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProductYear));
        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(placeRepository.findById(1L)).thenReturn(Optional.of(testPlace));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(centralMapper.toMaterial(any(), any(), any(), any(), any())).thenReturn(testMaterial);
        when(materialRepository.save(any(Material.class))).thenReturn(testMaterial);
        when(qrCodeUtil.generateQRCode("CRED001", "1", 100, 100)).thenReturn(null);

        Try<Material> result = materialManagement.create(createDto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when save QR code");
        verify(qrCodeUtil, times(1)).generateQRCode("CRED001", "1", 100, 100);
    }

    @Test
    void testCreate_QRCodeThrowsException() throws WriterException, IOException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProductYear));
        when(additionalRepository.findById(1L)).thenReturn(Optional.of(testAdditional));
        when(placeRepository.findById(1L)).thenReturn(Optional.of(testPlace));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(centralMapper.toMaterial(any(), any(), any(), any(), any())).thenReturn(testMaterial);
        when(materialRepository.save(any(Material.class))).thenReturn(testMaterial);
        when(qrCodeUtil.generateQRCode("CRED001", "1", 100, 100)).thenThrow(new IOException("QR Code error"));

        Try<Material> result = materialManagement.create(createDto);

        assertThat(result.isSuccess()).isTrue(); // Exception được catch và print, không ảnh hưởng kết quả
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(qrCodeUtil, times(1)).generateQRCode("CRED001", "1", 100, 100);
    }

    // Test cho phương thức update
    @Test
    void testUpdate_Success_NoChangeCredentialCode() throws WriterException, IOException {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(centralMapper.toMaterialUpdate(any(Material.class), any(MaterialUpdateRequestDTO.class))).thenReturn(testMaterial);
        when(materialRepository.save(any(Material.class))).thenReturn(testMaterial);

        Try<Material> result = materialManagement.update(updateDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(materialRepository, times(1)).findById(1L);
        verify(centralMapper, times(1)).toMaterialUpdate(any(), any());
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void testUpdate_NullFieldsInEmbedded() throws WriterException, IOException {
        // Thiết lập embedded với các trường null
        MaterialUpdateRequestDTO.Embedded embedded = new MaterialUpdateRequestDTO.Embedded();
        embedded.setProductId(null);
        embedded.setAdditionalId(null);
        embedded.setPlaceId(null);
        embedded.setUserId(null);
        updateDto.setEmbedded(embedded);
        updateDto.setCredentialCode("CRED001"); // Không thay đổi credentialCode

        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(centralMapper.toMaterialUpdate(any(Material.class), any(MaterialUpdateRequestDTO.class))).thenReturn(testMaterial);
        when(materialRepository.save(any(Material.class))).thenReturn(testMaterial);

        Try<Material> result = materialManagement.update(updateDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void testUpdate_NotFound() throws WriterException, IOException {
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Material> result = materialManagement.update(updateDto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when find material to update with id: 1");
        verify(materialRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdate_ChangeCredentialCode_Success() throws WriterException, IOException {
        updateDto.setCredentialCode("CRED002");
        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(centralMapper.toMaterialUpdate(any(Material.class), any(MaterialUpdateRequestDTO.class))).thenReturn(testMaterial);
        when(qrCodeUtil.deleteFile("CRED001")).thenReturn(true);
        when(qrCodeUtil.generateQRCode("CRED002", "1", 100, 100)).thenReturn(new byte[]{1, 2, 3});
        when(materialRepository.save(any(Material.class))).thenReturn(testMaterial);

        Try<Material> result = materialManagement.update(updateDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(qrCodeUtil, times(1)).deleteFile("CRED001");
        verify(qrCodeUtil, times(1)).generateQRCode("CRED002", "1", 100, 100);
    }

    @Test
    void testUpdate_ChangeCredentialCode_DeleteFails() throws WriterException, IOException {
        updateDto.setCredentialCode("CRED002");
        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(centralMapper.toMaterialUpdate(any(Material.class), any(MaterialUpdateRequestDTO.class))).thenReturn(testMaterial);
        when(qrCodeUtil.deleteFile("CRED001")).thenReturn(false);

        Try<Material> result = materialManagement.update(updateDto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when update new QR code image");
        verify(qrCodeUtil, times(1)).deleteFile("CRED001");
    }

    @Test
    void testUpdate_ChangeCredentialCode_QRCodeFails() throws WriterException, IOException {
        updateDto.setCredentialCode("CRED002");
        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(centralMapper.toMaterialUpdate(any(Material.class), any(MaterialUpdateRequestDTO.class))).thenReturn(testMaterial);
        when(qrCodeUtil.deleteFile("CRED001")).thenReturn(true);
        when(qrCodeUtil.generateQRCode("CRED002", "1", 100, 100)).thenReturn(null);

        Try<Material> result = materialManagement.update(updateDto);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("Failure when update QR code");
        verify(qrCodeUtil, times(1)).generateQRCode("CRED002", "1", 100, 100);
    }

    @Test
    void testUpdate_ChangeRelationships() throws WriterException, IOException {
        updateDto.getEmbedded().setProductId(2L);
        updateDto.getEmbedded().setAdditionalId(2L);
        updateDto.getEmbedded().setPlaceId(2L);
        updateDto.getEmbedded().setUserId(2L);

        Product newProduct = new Product();
        newProduct.setId(2L);
        Additional newAdditional = new Additional();
        newAdditional.setId(2L);
        Place newPlace = new Place();
        newPlace.setId(2L);
        User newUser = new User();
        newUser.setId(2L);

        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(centralMapper.toMaterialUpdate(any(Material.class), any(MaterialUpdateRequestDTO.class))).thenReturn(testMaterial);
        when(productRepository.findById(2L)).thenReturn(Optional.of(newProduct));
        when(additionalRepository.findById(2L)).thenReturn(Optional.of(newAdditional));
        when(placeRepository.findById(2L)).thenReturn(Optional.of(newPlace));
        when(userRepository.findById(2L)).thenReturn(Optional.of(newUser));
        when(materialRepository.save(any(Material.class))).thenReturn(testMaterial);

        Try<Material> result = materialManagement.update(updateDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(productRepository, times(1)).findById(2L);
        verify(additionalRepository, times(1)).findById(2L);
        verify(placeRepository, times(1)).findById(2L);
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testUpdate_NullEmbedded() throws WriterException, IOException {
        updateDto.setEmbedded(null);
        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(centralMapper.toMaterialUpdate(any(Material.class), any(MaterialUpdateRequestDTO.class))).thenReturn(testMaterial);
        when(materialRepository.save(any(Material.class))).thenReturn(testMaterial);

        Try<Material> result = materialManagement.update(updateDto);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    // Test cho phương thức delete
    @Test
    void testDelete_Success_MaterialExists() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(qrCodeUtil.deleteFile("CRED001")).thenReturn(true);
        doNothing().when(materialRepository).deleteById(1L);

        Try<Boolean> result = materialManagement.delete(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(materialRepository, times(1)).findById(1L);
        verify(qrCodeUtil, times(1)).deleteFile("CRED001");
        verify(materialRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_Success_MaterialNotExists() {
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());
        doNothing().when(materialRepository).deleteById(1L);

        Try<Boolean> result = materialManagement.delete(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(materialRepository, times(1)).findById(1L);
        verify(materialRepository, times(1)).deleteById(1L);
    }

    // Test cho phương thức fetchAll
    @Test
    void testFetchAll_WithYearDepreciation() {
        // Chuyển Instant.now() sang LocalDateTime để thực hiện phép trừ
        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Ho_Chi_Minh"));

        Material materialYear = new Material();
        materialYear.setId(1L);
        materialYear.setCredentialCode("CRED001");
        // Trừ 2 năm từ LocalDateTime, rồi chuyển lại thành Instant
        Instant twoYearsAgo = now.minus(2, ChronoUnit.YEARS).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
        materialYear.setTimeStartDepreciation(twoYearsAgo);
        materialYear.setProduct(testProductYear);

        Material materialMonth = new Material();
        materialMonth.setId(2L);
        materialMonth.setCredentialCode("CRED002");
        // Trừ 12 tháng từ LocalDateTime, rồi chuyển lại thành Instant
        Instant twelveMonthsAgo = now.minus(12, ChronoUnit.MONTHS).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
        materialMonth.setTimeStartDepreciation(twelveMonthsAgo);
        materialMonth.setProduct(testProductMonth);

        List<Material> materials = Arrays.asList(materialYear, materialMonth);
        when(materialRepository.findAll()).thenReturn(materials);
        when(materialRepository.getPrice(1L)).thenReturn(1000.0f);
        when(materialRepository.getPrice(2L)).thenReturn(2000.0f);

        List<MaterialResponseDTO> result = materialManagement.fetchAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getExtendedInfo().getPriceOrigin().floatValue()).isEqualTo(1000.0f);
        assertThat(result.get(1).getExtendedInfo().getPriceOrigin().floatValue()).isEqualTo(2000.0f);
        verify(materialRepository, times(1)).findAll();
        verify(materialRepository, times(1)).getPrice(1L);
        verify(materialRepository, times(1)).getPrice(2L);
    }
    // Test cho phương thức fetchByCategory
    @Test
    void testFetchByCategory() {
        List<Material> materials = Arrays.asList(testMaterial);
        when(materialRepository.findAllByCategoryId(1L)).thenReturn(materials);

        List<Material> result = materialManagement.fetchByCategory(1L);

        assertThat(result).hasSize(1);
        verify(materialRepository, times(1)).findAllByCategoryId(1L);
    }

    // Test cho phương thức filter
    @Test
    void testFilter_WithYearAndMonthDepreciation() {
        // Chuyển Instant.now() sang LocalDateTime để thực hiện phép trừ
        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Ho_Chi_Minh"));

        Material materialYear = new Material();
        materialYear.setId(1L);
        materialYear.setCredentialCode("CRED001");
        // Trừ 2 năm từ LocalDateTime, rồi chuyển lại thành Instant
        Instant twoYearsAgo = now.minus(2, ChronoUnit.YEARS).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
        materialYear.setTimeStartDepreciation(twoYearsAgo);
        materialYear.setProduct(testProductYear);

        Material materialMonth = new Material();
        materialMonth.setId(2L);
        materialMonth.setCredentialCode("CRED002");
        // Trừ 12 tháng từ LocalDateTime, rồi chuyển lại thành Instant
        Instant twelveMonthsAgo = now.minus(12, ChronoUnit.MONTHS).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
        materialMonth.setTimeStartDepreciation(twelveMonthsAgo);
        materialMonth.setProduct(testProductMonth);

        List<Material> materials = Arrays.asList(materialYear, materialMonth);
        MaterialFilterRequestDTO filterDto = new MaterialFilterRequestDTO();
        // Sửa stubbing để khớp với tham số thực tế (tất cả là null)
        when(materialRepository.findByFilter(null, null, null, null, null)).thenReturn(materials);
        when(materialRepository.getPrice(1L)).thenReturn(1000.0f);
        when(materialRepository.getPrice(2L)).thenReturn(2000.0f);

        List<MaterialResponseDTO> result = materialManagement.filter(filterDto);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getExtendedInfo().getPriceOrigin().floatValue()).isEqualTo(1000.0f);
        assertThat(result.get(1).getExtendedInfo().getPriceOrigin().floatValue()).isEqualTo(2000.0f);
        verify(materialRepository, times(1)).findByFilter(null, null, null, null, null);
        verify(materialRepository, times(1)).getPrice(1L);
        verify(materialRepository, times(1)).getPrice(2L);
    }
    // Test cho phương thức fetchPage
    @Test
    void testFetchPage() {
        FetchPageMaterialRequestDTO fetchDto = new FetchPageMaterialRequestDTO();
        fetchDto.setPage(0);
        fetchDto.setSize(10);

        Pageable pageable = PageRequest.of(0, 10);
        List<Material> materials = Arrays.asList(testMaterial);
        Page<Material> materialPage = new PageImpl<>(materials, pageable, 1);
        when(materialRepository.findAll(pageable)).thenReturn(materialPage);

        Page<Material> result = materialManagement.fetchPage(fetchDto);

        assertThat(result).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(materialRepository, times(1)).findAll(pageable);
    }

    // Test cho phương thức fetchHistoryTransfer
    @Test
    void testFetchHistoryTransfer() {
        List<Statistical.HistoryTransfer> historyList = Arrays.asList(mock(Statistical.HistoryTransfer.class));
        when(materialRepository.fetchHistoryTransfer(1L)).thenReturn(historyList);

        List<Statistical.HistoryTransfer> result = materialManagement.fetchHistoryTransfer(1L);

        assertThat(result).hasSize(1);
        verify(materialRepository, times(1)).fetchHistoryTransfer(1L);
    }
}