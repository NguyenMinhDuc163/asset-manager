package com.ptit.asset.service.manager.impl;

import com.google.zxing.WriterException;
import com.ptit.asset.domain.Material;
import com.ptit.asset.domain.Place;
import com.ptit.asset.domain.User;
import com.ptit.asset.domain.enumeration.TimeAllocationType;
import com.ptit.asset.dto.request.FetchPageMaterialRequestDTO;
import com.ptit.asset.dto.request.MaterialCreateRequestDTO;
import com.ptit.asset.dto.request.MaterialFilterRequestDTO;
import com.ptit.asset.dto.request.MaterialUpdateRequestDTO;
import com.ptit.asset.dto.response.MaterialResponseDTO;
import com.ptit.asset.repository.*;
import com.ptit.asset.repository.data.Statistical;
import com.ptit.asset.service.manager.MaterialManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import com.ptit.asset.util.QRCodeUtil;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialManagementImpl implements MaterialManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AdditionalRepository additionalRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QRCodeUtil qrCodeUtil;



    @Override
    public Try<Material> getOne(Long id) {
        return Try.of(() -> materialRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get material by id: "+id)));
    }

    @Override
    public Try<Material> create(MaterialCreateRequestDTO dto) throws WriterException, IOException {
        val product = productRepository.findById(dto.getEmbedded().getProductId());
        if (!product.isPresent()){
            return Try.failure(new Exception("Failure when find product by id: "+dto.getEmbedded().getProductId()));
        }
        val additional = additionalRepository.findById(dto.getEmbedded().getAdditionalId());
        if (!additional.isPresent()){
            return Try.failure(new Exception("Failure when find additional by id: "+dto.getEmbedded().getAdditionalId()));
        }
        Place placeAssign = null;
        if (dto.getEmbedded().getPlaceId() != null){
            val place = placeRepository.findById(dto.getEmbedded().getPlaceId());
            if (!place.isPresent()){
                return Try.failure(new Exception("Failure when find place by id: "+dto.getEmbedded().getPlaceId()));
            }
            placeAssign = place.get();
        }
        User userAssign = null;
        if (dto.getEmbedded().getUserId() != null){
            val user = userRepository.findById(dto.getEmbedded().getUserId());
            if (!user.isPresent()){
                return Try.failure(new Exception("Failure when find user by id: "+dto.getEmbedded().getUserId()));
            }
            userAssign = user.get();
        }

        // setup base entity to save
        val material = centralMapper.toMaterial(dto,additional.get(), product.get(), placeAssign, userAssign);

        return Try.of(() -> materialRepository.save(material))
        .flatMap(materialSaved -> {
            String infoCompressToQRCode = String.valueOf(materialSaved.getId());
            byte [] qrCode = new byte[0];
            try {
                qrCode = qrCodeUtil.generateQRCode(materialSaved.getCredentialCode(),infoCompressToQRCode, 100, 100);
            } catch (WriterException | IOException e) { e.printStackTrace(); }
            if (qrCode == null){
                return Try.failure(new Exception("Failure when save QR code"));
            }
            return Try.of(() -> materialSaved);
        })
        .orElse(() -> Try.failure(new Exception("Failure when save material")));

//        String infoCompressToQRCode = String.valueOf(material.getId());
//        byte [] qrCode = qrCodeUtil.generateQRCode(material.getCredentialCode(),infoCompressToQRCode, 100, 100);
//        if (qrCode == null){
//            return Try.failure(new Exception("Failure when save QR code"));
//        }

//        Blob blob = BlobProxy.generateProxy(qrCode);
//        material.setQrCode(blob);

//        return Try.of(() -> materialRepository.save(material))
//            .orElse(() -> Try.failure(new Exception("Failure when save material")));
    }

    @Override
    public Try<Material> update(MaterialUpdateRequestDTO dto) throws WriterException, IOException {
        val materialResult = materialRepository.findById(dto.getId());
        String credentialCodeOrigin = null;
        if (!materialResult.isPresent()){
            return Try.failure(new Exception("Failure when find material to update with id: "+dto.getId()));
        }
        credentialCodeOrigin = materialResult.get().getCredentialCode();

        val materialUpdate = centralMapper.toMaterialUpdate(materialResult.get(), dto);



        // relationship space
        if (dto.getEmbedded() != null){

            val productId = dto.getEmbedded().getProductId();
            if (productId != null && !productId.equals(materialUpdate.getProduct().getId())){
                val product = productRepository.findById(productId);
                product.ifPresent(materialUpdate::setProduct);
            }
            val additionalId = dto.getEmbedded().getAdditionalId();
            if (additionalId != null && !additionalId.equals(materialUpdate.getAdditional().getId())){
                val additional = additionalRepository.findById(additionalId);
                additional.ifPresent(materialUpdate::setAdditional);
            }
            val placeId = dto.getEmbedded().getPlaceId();
            if (placeId != null){
                val place = placeRepository.findById(placeId);
                place.ifPresent(materialUpdate::setCurrentPlace);
            }
            val userId = dto.getEmbedded().getUserId();
            if (userId != null){
                val user = userRepository.findById(userId);
                user.ifPresent(materialUpdate::setUser);
            }
        }
        // relationship space

        // add to update QR Code
        System.out.println("======>>>> Compare : " + credentialCodeOrigin +" and "+ dto.getCredentialCode());
        if (! dto.getCredentialCode().equalsIgnoreCase(credentialCodeOrigin)){
            System.out.println(">>>>>>------------ Has change Credential code");
            Boolean deleteOld = qrCodeUtil.deleteFile(credentialCodeOrigin);
            if (!deleteOld){
                return Try.failure(new Exception("Failure when update new QR code image"));
            }
            String infoCompressToQRCode = String.valueOf(materialUpdate.getId());
            byte [] qrCode = qrCodeUtil.generateQRCode(dto.getCredentialCode(),infoCompressToQRCode, 100, 100);
            if (qrCode == null){
                return Try.failure(new Exception("Failure when update QR code"));
            }
        }
        // add to update QR Code

        return Try.of(() -> materialRepository.save(materialUpdate))
            .orElse(() -> Try.failure(new Exception("Failure when update material")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        val material = materialRepository.findById(id);
        material.ifPresent(value -> qrCodeUtil.deleteFile(value.getCredentialCode()));
        return Try.of(() -> {
            materialRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete material by id: "+id)));
    }


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @Override
    public List<MaterialResponseDTO> fetchAll() {
        List<Material> materialList = materialRepository.findAll();
        return
        materialList.stream()
        .map(entity -> {
            MaterialResponseDTO.ExtendedInfo extendedInfo = new MaterialResponseDTO.ExtendedInfo();
//            MaterialRepository.ExtendedInfo extendedInfoQuery
//                    = materialRepository.getExtendedInfo(entity.getId());

            // add price origin into extended info
            Float priceOrigin = materialRepository.getPrice(entity.getId());
            extendedInfo.setPriceOrigin(BigDecimal.valueOf(priceOrigin)
                        .setScale(2,BigDecimal.ROUND_HALF_UP));
            // add price origin into extended info

            LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Ho_Chi_Minh"));
            LocalDateTime old = LocalDateTime.ofInstant(entity.getTimeStartDepreciation(), ZoneId.of("Asia/Ho_Chi_Minh"));

            Double currentValue = 0.0;

            if (entity.getProduct().getTimeAllocationType().equals(TimeAllocationType.YEAR)){
                long numberDiffYear = ChronoUnit.YEARS.between(old,now);
                System.out.println(">>> DIFF YEAR : " + numberDiffYear);
                currentValue = priceOrigin -
                        ((priceOrigin*(entity.getProduct().getDepreciationRate()/100))*numberDiffYear);
            }
            if (entity.getProduct().getTimeAllocationType().equals(TimeAllocationType.MONTH)){
                long numberDiffMonth = ChronoUnit.MONTHS.between(old,now);
                System.out.println(">>> DIFF MONTH : " + numberDiffMonth);
                currentValue = priceOrigin -
                        (priceOrigin*(entity.getProduct().getDepreciationRate()/100)*numberDiffMonth);
            }
            extendedInfo.setCurrentValue(BigDecimal.valueOf(currentValue)
                        .setScale(2,BigDecimal.ROUND_HALF_UP));

            MaterialResponseDTO responseDTO = new MaterialResponseDTO();
            BeanUtils.copyProperties(entity,responseDTO);
            responseDTO.setExtendedInfo(extendedInfo);
            return responseDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Material> fetchByCategory(Long categoryId) {
        return materialRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public List<MaterialResponseDTO> filter(MaterialFilterRequestDTO dto) {
        List<Material> materialList = materialRepository.findByFilter(
            (dto.getMaterialStatus() != null) ? dto.getMaterialStatus().name() : null,
            dto.getCategoryId(),
            dto.getPlaceId(),
            dto.getDepartmentId(),
            dto.getProductId()
            );
        return materialList.stream()
        .map(entity -> {
            MaterialResponseDTO.ExtendedInfo extendedInfo = new MaterialResponseDTO.ExtendedInfo();
            // add price origin into extended info
            Float priceOrigin = materialRepository.getPrice(entity.getId());
            extendedInfo.setPriceOrigin(BigDecimal.valueOf(priceOrigin)
                    .setScale(2,BigDecimal.ROUND_HALF_UP));
            // add price origin into extended info

            LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Ho_Chi_Minh"));
            LocalDateTime old = LocalDateTime.ofInstant(entity.getTimeStartDepreciation(), ZoneId.of("Asia/Ho_Chi_Minh"));

            Double currentValue = 0.0;

            if (entity.getProduct().getTimeAllocationType().equals(TimeAllocationType.YEAR)){
                long numberDiffYear = ChronoUnit.YEARS.between(old,now);
                System.out.println(">>> DIFF YEAR : " + numberDiffYear);
                currentValue = priceOrigin -
                        ((priceOrigin*(entity.getProduct().getDepreciationRate()/100))*numberDiffYear);
            }
            if (entity.getProduct().getTimeAllocationType().equals(TimeAllocationType.MONTH)){
                long numberDiffMonth = ChronoUnit.MONTHS.between(old,now);
                System.out.println(">>> DIFF MONTH : " + numberDiffMonth);
                currentValue = priceOrigin -
                        (priceOrigin*(entity.getProduct().getDepreciationRate()/100)*numberDiffMonth);
            }
            extendedInfo.setCurrentValue(BigDecimal.valueOf(currentValue)
                    .setScale(2,BigDecimal.ROUND_HALF_UP));

            MaterialResponseDTO responseDTO = new MaterialResponseDTO();
            BeanUtils.copyProperties(entity,responseDTO);
            responseDTO.setExtendedInfo(extendedInfo);
            return responseDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<Material> fetchPage(FetchPageMaterialRequestDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage(),dto.getSize());
        return materialRepository.findAll(pageable);
    }

    @Override
    public List<Statistical.HistoryTransfer> fetchHistoryTransfer(Long id) {
        return materialRepository.fetchHistoryTransfer(id);
    }
}
