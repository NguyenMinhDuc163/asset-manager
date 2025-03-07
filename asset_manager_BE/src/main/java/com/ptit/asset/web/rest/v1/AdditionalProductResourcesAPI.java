package com.ptit.asset.web.rest.v1;

import com.google.zxing.WriterException;
import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.AdditionalProductCreateDTO;
import com.ptit.asset.dto.request.AdditionalProductCreateInBoxNewVersionRequestDTO;
import com.ptit.asset.dto.request.AdditionalProductCreateInBoxRequestDTO;
import com.ptit.asset.exception.InvalidProductResourcesException;
import com.ptit.asset.service.endservice.AdditionalProductService;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ACCOUNTANT')")
public class AdditionalProductResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdditionalProductService additionalProductService;

    @GetMapping("/additionalProduct/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get additionalProduct by id >>>>> : {}", id);
        val result = additionalProductService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get additionalProduct success", 200,
                        "Get additionalProduct failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/additionalProduct:create")
    public ResponseEntity<?> create(@RequestBody @Valid AdditionalProductCreateDTO dto){
        logger.info(">>>>> Rest request to create additionalProduct >>>>> : {}", dto);
        val result = additionalProductService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create additionalProduct success", 200,
                        "Create additionalProduct failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/additionalProduct:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete additionalProduct >>>>> : {}", id);
        val result = additionalProductService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete additionalProduct success", 200,
                        "Delete additionalProduct failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("additionalProduct:fetchAll")
    public ResponseEntity<?> fetchAll(){
        logger.info(">>>>> Rest request to fetch all additional product >>>>>");
        val result = additionalProductService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all additional product success", 200), HttpStatus.OK
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
//    @PostMapping("/additionalProduct:createInBox")// old version
//    public ResponseEntity<?> createInBox(@Valid @RequestBody AdditionalProductCreateInBoxRequestDTO dto) throws WriterException, IOException {
//        logger.info(">>>>> Rest request to create in box additional product >>>>>");
//        val result = additionalProductService.createInBox(dto);
//        return new ResponseEntity<>(
//                RestResponseUtils.create(result,"Create additionalProduct in box success", 200,
//                        "Delete additionalProduct in box failure", 500),
//                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
//        );
//    }


    @PostMapping("/additionalProduct:createInBox") // new version
    public ResponseEntity<?> createInBox(@Valid @RequestBody AdditionalProductCreateInBoxNewVersionRequestDTO dto)
            throws WriterException, IOException, InvalidProductResourcesException {
        logger.info(">>>>> Rest request to create in box additional product >>>>>");
        val result = additionalProductService.createInBox(dto);
        return new ResponseEntity<>(
            RestResponseUtils.create(result,"Create additionalProduct in box success", 200,
                    "Create additionalProduct in box failure", 500),
            result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
