package com.ptit.asset.web.rest.v1;

import com.ptit.asset.domain.TypePlace;
import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.TypePlaceCreateRequestDTO;
import com.ptit.asset.service.endservice.TypePlaceService;
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
public class TypePlaceResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TypePlaceService typePlaceService;

    @GetMapping("/typePlace/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get type place by id >>>>> : {}", id);
        val result = typePlaceService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get type place success", 200,
                        "Get type place failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/typePlace:create")
    public ResponseEntity<?> create(@RequestBody @Valid TypePlaceCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create type place >>>>> : {}", dto);
        val result = typePlaceService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create type place success", 200,
                        "Create type place failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/typePlace:update")
    public ResponseEntity<?> update(@RequestBody @Valid TypePlace typePlace){
        logger.info(">>>>> Rest request to update type place >>>>> : {}", typePlace);
        val result = typePlaceService.update(typePlace);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Update type place success", 200,
                        "Update type place failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/typePlace:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete type place >>>>> : {}", id);
        val result = typePlaceService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete type place success", 200,
                        "Delete type place failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("typePlace:fetchAll")
    public ResponseEntity<?> fetchAll(){
        logger.info(">>>>> Rest request to fetch all type place >>>>>");
        val result = typePlaceService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all type place success", 200), HttpStatus.OK
        );
    }

}
