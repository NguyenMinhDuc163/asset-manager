package com.ptit.asset.web.rest.v1;

import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.*;
import com.ptit.asset.service.endservice.PlaceService;
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
//@PreAuthorize("hasRole('ACCOUNTANT')")
public class PlaceResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PlaceService placeService;

    @GetMapping("/place/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get place by id >>>>> : {}", id);
        val result = placeService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get place success", 200,
                        "Get place failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/place:create")
    public ResponseEntity<?> create(@RequestBody @Valid PlaceCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create place >>>>> : {}", dto);
        val result = placeService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create place success", 200,
                        "Create place failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/place:update")
    public ResponseEntity<?> update(@RequestBody @Valid PlaceUpdateRequestDTO dto){
        logger.info(">>>>> Rest request to update place >>>>> : {}", dto);
        val result = placeService.update(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Update place success", 200,
                        "Update place failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/place:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete place >>>>> : {}", id);
        val result = placeService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete place success", 200,
                        "Delete place failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @GetMapping("/place:fetchAll")
    public ResponseEntity<?> fetchAll(){
        val result = placeService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all place success", 200), HttpStatus.OK
        );
    }

    @PostMapping("/place:fetchPage")
    public ResponseEntity<?> fetchPage(@RequestBody @Valid FetchPagePlaceRequestDTO dto){
        val result = placeService.fetchPage(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch page place success", 200), HttpStatus.OK
        );
    }
}
