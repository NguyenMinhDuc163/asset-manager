package com.ptit.asset.web.rest.v1;

import com.ptit.asset.domain.Category;
import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.CategoryCreateRequestDTO;
import com.ptit.asset.dto.request.InventoryChangeStatusRequestDTO;
import com.ptit.asset.dto.request.InventoryCreateRequestDTO;
import com.ptit.asset.dto.request.LiquidateChangeStatusRequestDTO;
import com.ptit.asset.service.endservice.InventoryService;
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
public class InventoryResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/inventory/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get inventory by id >>>>> : {}", id);
        val result = inventoryService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get inventory success", 200,
                        "Get inventory failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/inventory:create")
    public ResponseEntity<?> create(@RequestBody @Valid InventoryCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create inventory >>>>> : {}", dto);
        val result = inventoryService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create inventory success", 200,
                        "Create inventory failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/inventory:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete inventory >>>>> : {}", id);
        val result = inventoryService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete inventory success", 200,
                        "Delete inventory failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("/inventory:fetchAll")
    public ResponseEntity<?> fetchAll(){
        logger.info(">>>>> Rest request to fetch all inventory >>>>>");
        val result = inventoryService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all inventory success", 200), HttpStatus.OK
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    @PutMapping("/inventory:changeStatus")
    public ResponseEntity<?> changeStatus(@Valid @RequestBody InventoryChangeStatusRequestDTO dto){
        logger.info(">>>>> Rest request to change status of inventory >>>>>");
        val result = inventoryService.changeStatus(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Change inventory status success", 200,
                        "Change inventory status failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
