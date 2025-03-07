package com.ptit.asset.web.rest.v1;

import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.LiquidateMaterialCreateRequestDTO;
import com.ptit.asset.service.endservice.LiquidateMaterialService;
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
public class LiquidateMaterialResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LiquidateMaterialService liquidateMaterialService;


    @GetMapping("/liquidateMaterial/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get liquidate material by id >>>>> : {}", id);
        val result = liquidateMaterialService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get liquidate material success", 200,
                        "Get liquidate material failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/liquidateMaterial:create")
    public ResponseEntity<?> create(@RequestBody @Valid LiquidateMaterialCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create liquidate material >>>>> : {}", dto);
        val result = liquidateMaterialService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create liquidate material success", 200,
                        "Create liquidate material failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/liquidateMaterial:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete liquidate material >>>>> : {}", id);
        val result = liquidateMaterialService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete liquidate material success", 200,
                        "Delete liquidate material failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("/liquidateMaterial:fetchAll")
    public ResponseEntity<?> fetchAll(){
        logger.info(">>>>> Rest request to fetch all liquidate material >>>>>");
        val result = liquidateMaterialService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all liquidate material success", 200), HttpStatus.OK
        );
    }

}
