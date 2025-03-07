package com.ptit.asset.web.rest.v1;

import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.FetchPageProductRequestDTO;
import com.ptit.asset.dto.request.ProductCreateRequestDTO;
import com.ptit.asset.dto.request.ProductUpdateRequestDTO;
import com.ptit.asset.service.endservice.ProductService;
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
public class ProductResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get product by id >>>>> : {}", id);
        val result = productService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get product success", 200,
                        "Get product failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/product:create")
    public ResponseEntity<?> create(@RequestBody @Valid ProductCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create product >>>>> : {}", dto);
        val result = productService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create product success", 200,
                        "Create product failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/product:update")
    public ResponseEntity<?> update(@RequestBody @Valid ProductUpdateRequestDTO dto){
        logger.info(">>>>> Rest request to update product >>>>> : {}", dto);
        val result = productService.update(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Update product success", 200,
                        "Update product failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/product:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete product >>>>> : {}", id);
        val result = productService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete product success", 200,
                        "Delete product failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @GetMapping("/product:fetchAll")
    public ResponseEntity<?> fetchAll(){
        val result = productService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all product success", 200), HttpStatus.OK
        );
    }

    @PostMapping("/product:fetchPage")
    public ResponseEntity<?> fetchPage(@RequestBody @Valid FetchPageProductRequestDTO dto){
        val result = productService.fetchPage(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch page product success", 200), HttpStatus.OK
        );
    }
}
