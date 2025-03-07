package com.ptit.asset.web.rest.v1;

import com.ptit.asset.domain.CalculationUnit;
import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.CalculationUnitCreateRequestDTO;
import com.ptit.asset.service.endservice.CalculationUnitService;
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
public class CalculationUnitResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CalculationUnitService calculationUnitService;

    @GetMapping("/calculationUnit/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get calculation Unit by id >>>>> : {}", id);
        val result = calculationUnitService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get calculation unit success", 200,
                        "Get calculation unit failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/calculationUnit:create")
    public ResponseEntity<?> create(@RequestBody @Valid CalculationUnitCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create calculation unit >>>>> : {}", dto);
        val result = calculationUnitService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create calculation unit success", 200,
                        "Create calculation unit failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/calculationUnit:update")
    public ResponseEntity<?> update(@RequestBody @Valid CalculationUnit calculationUnit){
        logger.info(">>>>> Rest request to update calculation unit >>>>> : {}", calculationUnit);
        val result = calculationUnitService.update(calculationUnit);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Update calculation unit success", 200,
                        "Update calculation unit failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/calculationUnit:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete calculation unit >>>>> : {}", id);
        val result = calculationUnitService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete calculation unit success", 200,
                        "Delete calculation unit failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("/calculationUnit:fetchAll")
    public ResponseEntity<?> fetchAll(){
        logger.info(">>>>> Rest request to fetch all calculation unit >>>>>");
        val result = calculationUnitService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all calculation unit success", 200), HttpStatus.OK
        );
    }
}
