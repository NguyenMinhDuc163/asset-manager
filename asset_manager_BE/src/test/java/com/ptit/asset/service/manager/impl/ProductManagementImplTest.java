package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.CalculationUnit;
import com.ptit.asset.domain.Category;
import com.ptit.asset.domain.Group;
import com.ptit.asset.domain.Product;
import com.ptit.asset.domain.enumeration.ProductType;
import com.ptit.asset.domain.enumeration.TimeAllocationType;
import com.ptit.asset.dto.request.FetchPageProductRequestDTO;
import com.ptit.asset.dto.request.ProductCreateRequestDTO;
import com.ptit.asset.dto.request.ProductUpdateRequestDTO;
import com.ptit.asset.repository.CalculationUnitRepository;
import com.ptit.asset.repository.CategoryRepository;
import com.ptit.asset.repository.ProductRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductManagementImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CalculationUnitRepository calculationUnitRepository;

    @Mock
    private CentralMapper centralMapper;

    @InjectMocks
    private ProductManagementImpl productManagement;

    private Product product;
    private Category category;
    private CalculationUnit calculationUnit;
    private ProductCreateRequestDTO createRequestDTO;
    private ProductUpdateRequestDTO updateRequestDTO;
    private FetchPageProductRequestDTO fetchPageRequestDTO;

    @BeforeEach
    void setUp() {
        category = new Category("Electronics", "Electronic devices", new Group());
        category.setId(1L);
        calculationUnit = new CalculationUnit("Unit", "Standard unit");
        calculationUnit.setId(1L);
        product = new Product("Laptop", "High-end laptop", "China", ProductType.ASSET,
                TimeAllocationType.MONTH, 24, 4.1667, category, calculationUnit);
        product.setId(1L);

        createRequestDTO = new ProductCreateRequestDTO();
        createRequestDTO.setName("Laptop");
        createRequestDTO.setAllocationDuration(24);
        createRequestDTO.setEmbedded(new ProductCreateRequestDTO.Embedded(1L, 1L));

        updateRequestDTO = new ProductUpdateRequestDTO();
        updateRequestDTO.setId(1L);
        updateRequestDTO.setName("Updated Laptop");
        updateRequestDTO.setAllocationDuration(12);
        updateRequestDTO.setEmbedded(new ProductUpdateRequestDTO.Embedded(1L, 1L));

        fetchPageRequestDTO = new FetchPageProductRequestDTO();
        fetchPageRequestDTO.setPage(0);
        fetchPageRequestDTO.setSize(5);
    }

    // Test cases for getOne
    @Test
    void getOne_WhenProductExists_ReturnsSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Try<Product> result = productManagement.getOne(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(product);
        verify(productRepository).findById(1L);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void getOne_WhenProductNotFound_ReturnsFailure() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Product> result = productManagement.getOne(1L);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when get product by id: 1");
        verify(productRepository).findById(1L);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void getOne_WhenIdIsNull_ReturnsFailure() {
        Try<Product> result = productManagement.getOne(null);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when get product by id: null");
        verify(productRepository).findById(null);
        verifyNoMoreInteractions(productRepository);
    }

    // Test cases for create
    @Test
    void create_WhenValidInput_ReturnsSuccess() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(calculationUnitRepository.findById(1L)).thenReturn(Optional.of(calculationUnit));
        when(centralMapper.toProduct(any(), any(), any())).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Try<Product> result = productManagement.create(createRequestDTO);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(product);
        assertThat(result.get().getDepreciationRate()).isEqualTo(100.0 / 24.0);
        verify(categoryRepository).findById(1L);
        verify(calculationUnitRepository).findById(1L);
        verify(centralMapper).toProduct(any(), any(), any());
        verify(productRepository).save(product);
    }

    @Test
    void create_WhenProductNameExists_ReturnsFailure() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(calculationUnitRepository.findById(1L)).thenReturn(Optional.of(calculationUnit));
        when(centralMapper.toProduct(any(), any(), any())).thenReturn(product);
        when(productRepository.save(product)).thenThrow(new RuntimeException("Duplicate product name"));

        Try<Product> result = productManagement.create(createRequestDTO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when save product");
        verify(categoryRepository).findById(1L);
        verify(calculationUnitRepository).findById(1L);
        verify(centralMapper).toProduct(any(), any(), any());
        verify(productRepository).save(product);
    }

    @Test
    void create_WhenCategoryNotFound_ReturnsFailure() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Product> result = productManagement.create(createRequestDTO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when find category with id: 1");
        verify(categoryRepository).findById(1L);
        verifyNoInteractions(calculationUnitRepository, centralMapper, productRepository);
    }

    @Test
    void create_WhenCalculationUnitNotFound_ReturnsFailure() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(calculationUnitRepository.findById(1L)).thenReturn(Optional.empty());

        Try<Product> result = productManagement.create(createRequestDTO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when find calculation unit with id: 1");
        verify(categoryRepository).findById(1L);
        verify(calculationUnitRepository).findById(1L);
        verifyNoInteractions(centralMapper, productRepository);
    }

    // Test cases for update
    @Test
    void update_WhenValidInput_ReturnsSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(centralMapper.toProductUpdate(any(), any())).thenReturn(product);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(calculationUnitRepository.findById(1L)).thenReturn(Optional.of(calculationUnit));
        when(productRepository.save(product)).thenReturn(product);

        Try<Product> result = productManagement.update(updateRequestDTO);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(product);
        assertThat(result.get().getDepreciationRate()).isEqualTo(100.0 / 12.0);
        verify(productRepository).findById(1L);
        verify(centralMapper).toProductUpdate(any(), any());
        verify(categoryRepository).findById(1L);
        verify(calculationUnitRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void update_WhenProductNotFound_ReturnsFailure() {
        // Mock phương thức findById trả về Optional.empty()
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Thực hiện gọi phương thức update với updateRequestDTO
        Try<Product> result = productManagement.update(updateRequestDTO);

        // Kiểm tra rằng kết quả là failure
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when find product to update with id: 1");

        // Xác minh rằng phương thức findById đã được gọi
        verify(productRepository).findById(1L);

        // Kiểm tra rằng không có tương tác nào với các repository khác ngoài productRepository
        verifyNoMoreInteractions(centralMapper, categoryRepository, calculationUnitRepository);
    }


    @Test
    void update_WhenCategoryChanged_ReturnsSuccess() {
        Category newCategory = new Category("Furniture", "Furniture items", new Group());
        newCategory.setId(2L);
        updateRequestDTO.setEmbedded(new ProductUpdateRequestDTO.Embedded(2L, 1L));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(centralMapper.toProductUpdate(any(), any())).thenReturn(product);
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(newCategory));
        when(productRepository.save(product)).thenReturn(product);

        Try<Product> result = productManagement.update(updateRequestDTO);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getCategory()).isEqualTo(newCategory);
        verify(categoryRepository).findById(2L);
        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
        verify(centralMapper).toProductUpdate(any(), any());
        verifyNoInteractions(calculationUnitRepository);
    }

    @Test
    void update_WhenEmbeddedIsNull_ReturnsSuccess() {
        updateRequestDTO.setEmbedded(null);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(centralMapper.toProductUpdate(any(), any())).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Try<Product> result = productManagement.update(updateRequestDTO);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(product);
        assertThat(result.get().getDepreciationRate()).isEqualTo(100.0 / 12.0);
        verify(productRepository).findById(1L);
        verify(centralMapper).toProductUpdate(any(), any());
        verify(productRepository).save(product);
        verifyNoInteractions(categoryRepository, calculationUnitRepository);
    }

    @Test
    void update_WhenCategoryNotChanged_ReturnsSuccess() {
        updateRequestDTO.setEmbedded(new ProductUpdateRequestDTO.Embedded(1L, 1L));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(centralMapper.toProductUpdate(any(), any())).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Try<Product> result = productManagement.update(updateRequestDTO);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(product);
        verify(productRepository).findById(1L);
        verify(centralMapper).toProductUpdate(any(), any());
        verify(productRepository).save(product);
        verifyNoInteractions(categoryRepository, calculationUnitRepository);
    }

    @Test
    void update_WhenCategoryNotFound_ReturnsSuccess() {
        updateRequestDTO.setEmbedded(new ProductUpdateRequestDTO.Embedded(2L, 1L));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(centralMapper.toProductUpdate(any(), any())).thenReturn(product);
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());
        when(productRepository.save(product)).thenReturn(product);

        Try<Product> result = productManagement.update(updateRequestDTO);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getCategory()).isEqualTo(category); // Category không thay đổi
        verify(categoryRepository).findById(2L);
        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
        verify(centralMapper).toProductUpdate(any(), any());
        verifyNoInteractions(calculationUnitRepository);
    }

    @Test
    void update_WhenCalculationUnitChanged_ReturnsSuccess() {
        CalculationUnit newCalculationUnit = new CalculationUnit("NewUnit", "New unit");
        newCalculationUnit.setId(2L);
        updateRequestDTO.setEmbedded(new ProductUpdateRequestDTO.Embedded(1L, 2L));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(centralMapper.toProductUpdate(any(), any())).thenReturn(product);
        when(calculationUnitRepository.findById(2L)).thenReturn(Optional.of(newCalculationUnit));
        when(productRepository.save(product)).thenReturn(product);

        Try<Product> result = productManagement.update(updateRequestDTO);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getCalculationUnit()).isEqualTo(newCalculationUnit);
        verify(calculationUnitRepository).findById(2L);
        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
        verify(centralMapper).toProductUpdate(any(), any());
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void update_WhenCalculationUnitNotFound_ReturnsSuccess() {
        updateRequestDTO.setEmbedded(new ProductUpdateRequestDTO.Embedded(1L, 2L));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(centralMapper.toProductUpdate(any(), any())).thenReturn(product);
        when(calculationUnitRepository.findById(2L)).thenReturn(Optional.empty());
        when(productRepository.save(product)).thenReturn(product);

        Try<Product> result = productManagement.update(updateRequestDTO);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get().getCalculationUnit()).isEqualTo(calculationUnit); // CalculationUnit không thay đổi
        verify(calculationUnitRepository).findById(2L);
        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
        verify(centralMapper).toProductUpdate(any(), any());
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void update_WhenSaveFails_ReturnsFailure() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(centralMapper.toProductUpdate(any(), any())).thenReturn(product);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(calculationUnitRepository.findById(1L)).thenReturn(Optional.of(calculationUnit));
        when(productRepository.save(product)).thenThrow(new RuntimeException("Save failed"));

        Try<Product> result = productManagement.update(updateRequestDTO);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when update product");
        verify(productRepository).findById(1L);
        verify(centralMapper).toProductUpdate(any(), any());
        verify(categoryRepository).findById(1L);
        verify(calculationUnitRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void update_WhenCategoryIdIsNull_ReturnsSuccess() {
        updateRequestDTO.setEmbedded(new ProductUpdateRequestDTO.Embedded(null, 1L));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(centralMapper.toProductUpdate(any(), any())).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Try<Product> result = productManagement.update(updateRequestDTO);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(product);
        assertThat(result.get().getCategory()).isEqualTo(category); // Category không thay đổi

        verify(productRepository).findById(1L);
        verify(centralMapper).toProductUpdate(any(), any());
        verify(productRepository).save(product);

        verifyNoInteractions(categoryRepository, calculationUnitRepository);
    }


    @Test
    void update_WhenCalculationUnitIdIsNull_ReturnsSuccess() {
        updateRequestDTO.setEmbedded(new ProductUpdateRequestDTO.Embedded(1L, null));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(centralMapper.toProductUpdate(any(), any())).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Try<Product> result = productManagement.update(updateRequestDTO);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(product);
        assertThat(result.get().getCalculationUnit()).isEqualTo(calculationUnit);

        verify(productRepository).findById(1L);
        verify(centralMapper).toProductUpdate(any(), any());
        verify(productRepository).save(product);
        verifyNoInteractions(calculationUnitRepository);

        // ❌ Không cần verify(categoryRepository.findById) nếu bạn biết nó không được gọi
    }


    // Test cases for delete
    @Test
    void delete_WhenProductExists_ReturnsSuccess() {
        doNothing().when(productRepository).deleteById(1L);

        Try<Boolean> result = productManagement.delete(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(productRepository).deleteById(1L);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void delete_WhenDeletionFails_ReturnsFailure() {
        doThrow(new RuntimeException("Deletion error")).when(productRepository).deleteById(1L);

        Try<Boolean> result = productManagement.delete(1L);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when delete product with id: 1");
        verify(productRepository).deleteById(1L);
    }

    @Test
    void delete_WhenIdIsNull_ReturnsFailure() {
        doThrow(new IllegalArgumentException("ID cannot be null")).when(productRepository).deleteById(null);

        Try<Boolean> result = productManagement.delete(null);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).hasMessage("Failure when delete product with id: null");
        verify(productRepository).deleteById(null);
        verifyNoMoreInteractions(productRepository);
    }

    // Test cases for fetchAll
    @Test
    void fetchAll_WhenProductsExist_ReturnsList() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<Product> result = productManagement.fetchAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(product);
        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void fetchAll_WhenNoProducts_ReturnsEmptyList() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<Product> result = productManagement.fetchAll();

        assertThat(result).isEmpty();
        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    // Test cases for fetchPage
    @Test
    void fetchPage_WhenPageSize5_ReturnsPage() {
        fetchPageRequestDTO.setSize(5);
        Page<Product> page = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Product> result = productManagement.fetchPage(fetchPageRequestDTO);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(product);
        verify(productRepository).findAll(any(Pageable.class));
    }

    @Test
    void fetchPage_WhenPageSize10_ReturnsPage() {
        fetchPageRequestDTO.setSize(10);
        Page<Product> page = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Product> result = productManagement.fetchPage(fetchPageRequestDTO);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(product);
        verify(productRepository).findAll(any(Pageable.class));
    }

    @Test
    void fetchPage_WhenPageSize25_ReturnsPage() {
        fetchPageRequestDTO.setSize(25);
        Page<Product> page = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Product> result = productManagement.fetchPage(fetchPageRequestDTO);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(product);
        verify(productRepository).findAll(any(Pageable.class));
    }

    @Test
    void fetchPage_WhenNoProducts_ReturnsEmptyPage() {
        fetchPageRequestDTO.setSize(5);
        Page<Product> page = new PageImpl<>(Collections.emptyList());
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Product> result = productManagement.fetchPage(fetchPageRequestDTO);

        assertThat(result.getContent()).isEmpty();
        verify(productRepository).findAll(any(Pageable.class));
    }
}