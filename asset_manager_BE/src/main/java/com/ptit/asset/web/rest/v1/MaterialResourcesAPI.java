package com.ptit.asset.web.rest.v1;

import com.google.zxing.WriterException;
import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.*;
import com.ptit.asset.service.endservice.MaterialService;
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
//@PreAuthorize("hasRole('ACCOUNTANT')")
public class MaterialResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MaterialService materialService;

    @GetMapping("/material/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get material by id >>>>> : {}", id);
        val result = materialService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get material success", 200,
                        "Get material failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/material:create")
    public ResponseEntity<?> create(@RequestBody @Valid MaterialCreateRequestDTO dto) throws WriterException, IOException {
        logger.info(">>>>> Rest request to create material >>>>> : {}", dto);
        val result = materialService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create material success", 200,
                        "Create material failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/material:update")
    public ResponseEntity<?> update(@RequestBody @Valid MaterialUpdateRequestDTO dto) throws WriterException, IOException{
        logger.info(">>>>> Rest request to update material >>>>> : {}", dto);
        val result = materialService.update(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Update material success", 200,
                        "Update material failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/material:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete material >>>>> : {}", id);
        val result = materialService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete material success", 200,
                        "Delete material failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @GetMapping("/material:fetchAll")
    public ResponseEntity<?> fetchAll(){
        val result = materialService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all material success", 200), HttpStatus.OK
        );
    }

    @PostMapping("/material:fetchPage")
    public ResponseEntity<?> fetchPage(@RequestBody @Valid FetchPageMaterialRequestDTO dto){
        val result = materialService.fetchPage(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch page material success", 200), HttpStatus.OK
        );
    }

//    @GetMapping("/material:fetchByFilter")
    @GetMapping("/material:fetchByCategory/{categoryId}")
    public ResponseEntity<?> fetchByCategory(@PathVariable("categoryId") Long categoryId){
        val result = materialService.fetchByCategory(categoryId);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all material success", 200), HttpStatus.OK
        );
    }

    @PostMapping("/material:filter")
    public ResponseEntity<?> filter(@Valid @RequestBody MaterialFilterRequestDTO dto){
        val result = materialService.filter(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch material filtered success", 200), HttpStatus.OK
        );
    }

    @GetMapping("/material:fetchHistoryTransfer/{id}")
    public ResponseEntity<?> fetchHistoryTransfer(@PathVariable("id") Long id){
        val result = materialService.fetchHistoryTransfer(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch history transfer material success", 200), HttpStatus.OK
        );
    }
}
