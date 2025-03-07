package com.ptit.asset.web.rest.v1;

import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.LiquidateChangeStatusRequestDTO;
import com.ptit.asset.dto.request.LiquidateCreateRequestDTO;
import com.ptit.asset.dto.request.LiquidateUpdateRequestDTO;
import com.ptit.asset.service.endservice.LiquidateService;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ACCOUNTANT')")
public class LiquidateResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LiquidateService liquidateService;

    @GetMapping("/liquidate/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get liquidate by id >>>>> : {}", id);
        val result = liquidateService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get liquidate success", 200,
                        "Get liquidate failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/liquidate:create")
    public ResponseEntity<?> create(@RequestBody @Valid LiquidateCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create liquidate >>>>> : {}", dto);
        val result = liquidateService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create liquidate success", 200,
                        "Create liquidate failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/liquidate:update")
    public ResponseEntity<?> update(@RequestBody @Valid LiquidateUpdateRequestDTO dto){
        logger.info(">>>>> Rest request to update liquidate >>>>> : {}", dto);
        val result = liquidateService.update(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Update liquidate success", 200,
                        "Update liquidate failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/liquidate:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete liquidate >>>>> : {}", id);
        val result = liquidateService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete liquidate success", 200,
                        "Delete liquidate failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("/liquidate:fetchAll")
    public ResponseEntity<?> fetchAll(){
        logger.info(">>>>> Rest request to fetch all liquidate >>>>>");
        val result = liquidateService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all liquidate success", 200), HttpStatus.OK
        );
    }

     // ----------------------------------------------------------------------------------------------------------------
    @PutMapping("/liquidate:changeStatus")
    public ResponseEntity<?> changeStatus(@Valid @RequestBody LiquidateChangeStatusRequestDTO dto){
        logger.info(">>>>> Rest request to change status of liquidate >>>>>");
        val result = liquidateService.changeStatus(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Change liquidate status success", 200,
                        "Change liquidate status failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
