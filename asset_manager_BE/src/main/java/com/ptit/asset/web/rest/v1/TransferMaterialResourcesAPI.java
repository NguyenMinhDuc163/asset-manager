package com.ptit.asset.web.rest.v1;

import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.TransferMaterialCreateRequestDTO;
import com.ptit.asset.service.endservice.TransferMaterialService;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ACCOUNTANT')")
public class TransferMaterialResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TransferMaterialService transferMaterialService;


    @GetMapping("/transferMaterial/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get transfer material by id >>>>> : {}", id);
        val result = transferMaterialService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get transfer material success", 200,
                        "Get transfer material failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/transferMaterial:create")
    public ResponseEntity<?> create(@RequestBody @Valid TransferMaterialCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create transfer material >>>>> : {}", dto);
        val result = transferMaterialService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create transfer material success", 200,
                        "Create transfer material failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/transferMaterial:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete transfer material >>>>> : {}", id);
        val result = transferMaterialService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete transfer material success", 200,
                        "Delete transfer material failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("/transferMaterial:fetchAll")
    public ResponseEntity<?> fetchAll(){
        logger.info(">>>>> Rest request to fetch all transfer material >>>>>");
        val result = transferMaterialService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all transfer material success", 200), HttpStatus.OK
        );
    }

}
