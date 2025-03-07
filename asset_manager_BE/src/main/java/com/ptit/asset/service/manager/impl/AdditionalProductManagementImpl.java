package com.ptit.asset.service.manager.impl;

import com.google.zxing.WriterException;
import com.ptit.asset.domain.AdditionalProduct;
import com.ptit.asset.domain.enumeration.MaterialStatus;
import com.ptit.asset.dto.request.AdditionalProductCreateDTO;
import com.ptit.asset.dto.request.AdditionalProductCreateInBoxNewVersionRequestDTO;
import com.ptit.asset.dto.request.MaterialCreateRequestDTO;
import com.ptit.asset.exception.InvalidProductResourcesException;
import com.ptit.asset.repository.AdditionalProductRepository;
import com.ptit.asset.repository.AdditionalRepository;
import com.ptit.asset.repository.MaterialRepository;
import com.ptit.asset.repository.ProductRepository;
import com.ptit.asset.service.manager.AdditionalProductManagement;
import com.ptit.asset.service.manager.MaterialManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
@Transactional
public class AdditionalProductManagementImpl implements AdditionalProductManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    AdditionalProductRepository additionalProductRepository;
    @Autowired
    private AdditionalRepository additionalRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private MaterialManagement materialManagement;


    @Override
    public Try<AdditionalProduct> getOne(Long id) {
        return Try.of(() -> additionalProductRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get additional product by id: "+id)));
    }

    @Override
    public Try<AdditionalProduct> create(AdditionalProductCreateDTO dto) {
        val additional = additionalRepository.findById(dto.getEmbedded().getAdditionalId());
        if (!additional.isPresent()){
            return Try.failure(new Exception("Failure when find additional by id: "+dto.getEmbedded().getAdditionalId()));
        }
        val product = productRepository.findById(dto.getEmbedded().getProductId());
        if (!product.isPresent()){
            return Try.failure(new Exception("Failure when find product by id: "+dto.getEmbedded().getProductId()));
        }

        val additionalProduct = centralMapper.toAdditionalProduct(dto,additional.get(),product.get());
//        System.out.println("Object : " + additionalProduct);

        return Try.of(() -> additionalProductRepository.save(additionalProduct))
            .orElse(() -> Try.failure(new Exception("Failure when save additional product")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            additionalProductRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete additional product with id: "+id)));
    }

    @Override
    public List<AdditionalProduct> fetchAll() {
        return additionalProductRepository.findAll();
    }


    // -----------------------------------------------------------------------------------------------------------------
//    @Override
//    @Transactional
//    public Try<Boolean> createInBox(AdditionalProductCreateInBoxRequestDTO dto) throws WriterException, IOException {
//        List<AdditionalProductCreateInBoxRequestDTO.Embedded.Record> recordListFiltered
//                = dto.getEmbedded().getRecordList().stream()
//                .collect(collectingAndThen(
//                    toCollection(() -> new TreeSet<>(
//                    comparingLong(AdditionalProductCreateInBoxRequestDTO.Embedded.Record::getProductId))),
//                    ArrayList::new
//                    )
//                );
//        System.out.println(recordListFiltered);
//        val additional = additionalRepository.findById(dto.getEmbedded().getAdditionalId());
//        if (!additional.isPresent()){
//            return Try.failure(new Exception("Failure when find additional with id:"+dto.getEmbedded().getAdditionalId()));
//        }
//        for (AdditionalProductCreateInBoxRequestDTO.Embedded.Record record: recordListFiltered){
//            // logic check to save additional product
//            val product = productRepository.findById(record.getProductId());
//
//            if (product.isPresent()){
//                additionalProductRepository.save(new AdditionalProduct(
//                        additional.get(), product.get(), record.getPrice()));
//
//                // save material also
//                for (String credentialCode : record.getListMaterialCode()){
//                    MaterialCreateRequestDTO.Embedded embedded = new MaterialCreateRequestDTO.Embedded();
//                    embedded.setAdditionalId(additional.get().getId());
//                    embedded.setProductId(product.get().getId());
//                    materialManagement.create(new MaterialCreateRequestDTO(
//                            credentialCode,
//                            MaterialStatus.IN_USED,
//                            additional.get().getTime(),
//                            false,
//                            null,
//                            embedded
//                    ));
//                }
//            }
//        }
//        return Try.of(() -> true);
//    }


    @Override
    @Transactional(rollbackFor = InvalidProductResourcesException.class)
    public Try<Boolean> createInBox(AdditionalProductCreateInBoxNewVersionRequestDTO dto)
            throws WriterException, IOException, InvalidProductResourcesException {

        List<AdditionalProductCreateInBoxNewVersionRequestDTO.Embedded.Record> recordListFiltered
            = dto.getEmbedded()
            .getRecordList().stream()
            .collect(collectingAndThen(
                toCollection(() -> new TreeSet<>(
                    comparingLong(AdditionalProductCreateInBoxNewVersionRequestDTO.Embedded.Record::getProductId)
                    )),
                    ArrayList::new
                )
            );

        System.out.println(recordListFiltered);

        val additional = additionalRepository.findById(dto.getEmbedded().getAdditionalId());
        if (!additional.isPresent()){
            return Try.failure(new Exception("Failure when find additional with id:"+dto.getEmbedded().getAdditionalId()));
        }
        if (!additional.get().getInProcess()){
            return Try.failure(new Exception("Failure process because additional not in process now"));
        }


        for (AdditionalProductCreateInBoxNewVersionRequestDTO.Embedded.Record record: recordListFiltered){
            // logic check to save additional product
            val product = productRepository.findById(record.getProductId());

            if (!product.isPresent()){
                throw new InvalidProductResourcesException("Contain product not exist in system");
            }

            // logic space
            additionalProductRepository.save(new AdditionalProduct(
                additional.get(), product.get(), record.getPrice()));

            // save material also
//            for (String credentialCode : record.getListMaterialCode()){
//                MaterialCreateRequestDTO.Embedded embedded = new MaterialCreateRequestDTO.Embedded();
//                embedded.setAdditionalId(additional.get().getId());
//                embedded.setProductId(product.get().getId());
//                materialManagement.create(new MaterialCreateRequestDTO(
//                        credentialCode,
//                        MaterialStatus.IN_USED,
//                        additional.get().getTime(),
//                        false,
//                        null,
//                        embedded
//                ));
//            }

            for (AdditionalProductCreateInBoxNewVersionRequestDTO.Embedded.Record.MaterialItem item :
            record.getListMaterial()){

                MaterialCreateRequestDTO.Embedded embedded = new MaterialCreateRequestDTO.Embedded();
                embedded.setAdditionalId(additional.get().getId());
                embedded.setProductId(product.get().getId());
                embedded.setPlaceId(item.getPlaceId());

                materialManagement.create(new MaterialCreateRequestDTO(
                    item.getCredential(),
                    MaterialStatus.IN_USED,
                    item.getTimeStartDepreciation(),
                    false,
                    null,
                    embedded
                ));
            }
            // logic space
        }
        return Try.of(() -> true);
    }
}
