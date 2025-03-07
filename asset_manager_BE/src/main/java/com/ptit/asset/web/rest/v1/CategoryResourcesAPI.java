package com.ptit.asset.web.rest.v1;

import com.ptit.asset.domain.Category;
import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.CategoryCreateRequestDTO;
import com.ptit.asset.dto.request.CategoryUpdateRequestDTO;
import com.ptit.asset.dto.request.FetchPageCategoryRequestDTO;
import com.ptit.asset.service.endservice.CategoryService;
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
public class CategoryResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get category by id >>>>> : {}", id);
        val result = categoryService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get category success", 200,
                        "Get category failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/category:create")
    public ResponseEntity<?> create(@RequestBody @Valid CategoryCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create category >>>>> : {}", dto);
        val result = categoryService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create category success", 200,
                        "Create category failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/category:update")
    public ResponseEntity<?> update(@RequestBody @Valid CategoryUpdateRequestDTO dto){
        logger.info(">>>>> Rest request to update category >>>>> : {}", dto);
        val result = categoryService.update(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Update category success", 200,
                        "Update category failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/category:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete category >>>>> : {}", id);
        val result = categoryService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete category success", 200,
                        "Delete category failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @GetMapping("/category:fetchAll")
    public ResponseEntity<?> fetchAll(){
        val result = categoryService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all category success", 200), HttpStatus.OK
        );
    }

    @PostMapping("/category:fetchPage")
    public ResponseEntity<?> fetchPage(@RequestBody @Valid FetchPageCategoryRequestDTO dto){
        val result = categoryService.fetchPage(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch page category success", 200), HttpStatus.OK
        );
    }
}
