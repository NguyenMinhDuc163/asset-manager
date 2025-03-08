package com.ptit.asset.web.rest.v1;

import com.ptit.asset.domain.Department;
import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.DepartmentCreateRequestDTO;
import com.ptit.asset.dto.request.FetchPageDepartmentRequestDTO;
import com.ptit.asset.service.endservice.DepartmentService;
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
public class DepartmentResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/department/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get department by id >>>>> : {}", id);
        val result = departmentService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get department success", 200,
                        "Get department failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/department:create")
    public ResponseEntity<?> create(@RequestBody @Valid DepartmentCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create department >>>>> : {}", dto);
        val result = departmentService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create department success", 200,
                        "Create department failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/department:update")
    public ResponseEntity<?> update(@RequestBody @Valid Department department){
        logger.info(">>>>> Rest request to update department >>>>> : {}", department);
        val result = departmentService.update(department);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Update department success", 200,
                        "Update department failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/department:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete department >>>>> : {}", id);
        val result = departmentService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete department success", 200,
                        "Delete department failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @GetMapping("/department:fetchAll")
    public ResponseEntity<?> fetchAll(){
        val result = departmentService.fetchAll();
        System.out.print("------------>" + result.toString());
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all department success", 200), HttpStatus.OK
        );
    }

    @PostMapping("/department:fetchPage")
    public ResponseEntity<?> fetchPage(@RequestBody @Valid FetchPageDepartmentRequestDTO dto){
        val result = departmentService.fetchPage(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch page department success", 200), HttpStatus.OK
        );
    }

}
