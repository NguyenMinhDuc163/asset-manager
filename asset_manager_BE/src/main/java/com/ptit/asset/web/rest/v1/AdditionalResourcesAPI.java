package com.ptit.asset.web.rest.v1;

import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.AdditionalChangeStatusRequestDTO;
import com.ptit.asset.dto.request.AdditionalCreateRequestDTO;
import com.ptit.asset.dto.request.AdditionalUpdateRequestDTO;
import com.ptit.asset.service.endservice.AdditionalService;
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
public class AdditionalResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdditionalService additionalService;

    @GetMapping("/additional/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get additional by id >>>>> : {}", id);
        val result = additionalService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get additional success", 200,
                        "Get additional failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/additional:create")
    public ResponseEntity<?> create(@RequestBody @Valid AdditionalCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create additional >>>>> : {}", dto);
        val result = additionalService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create additional success", 200,
                        "Create additional failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/additional:update")
    public ResponseEntity<?> update(@RequestBody @Valid AdditionalUpdateRequestDTO dto){
        logger.info(">>>>> Rest request to update additional >>>>> : {}", dto);
        val result = additionalService.update(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Update additional success", 200,
                        "Update additional failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/additional:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete additional >>>>> : {}", id);
        val result = additionalService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete additional success", 200,
                        "Delete additional failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("/additional:fetchAll")
    public ResponseEntity<?> fetchAll(){
        logger.info(">>>>> Rest request to fetch all additional >>>>>");
        val result = additionalService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all additional success", 200), HttpStatus.OK
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    @PutMapping("/additional:changeStatus")
    public ResponseEntity<?> changeStatus(@Valid @RequestBody AdditionalChangeStatusRequestDTO dto){
        logger.info(">>>>> Rest request to change status of additional >>>>>");
        val result = additionalService.changeStatus(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Change additional status success", 200,
                        "Change additional status failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


}
