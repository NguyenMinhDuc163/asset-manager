package com.ptit.asset.web.rest.v1;

import com.ptit.asset.domain.Campus;
import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.CampusCreateRequestDTO;
import com.ptit.asset.service.endservice.CampusService;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
//@PreAuthorize("hasRole('ADMIN') or hasRole('CHIEF_ACCOUNTANT')")
@PreAuthorize("hasRole('ACCOUNTANT')")
public class CampusResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CampusService campusService;

    @GetMapping("/campus/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get campus by id >>>>> : {}", id);
        val result = campusService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get campus success", 200,
                        "Get campus failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/campus:create")
    public ResponseEntity<?> create(@RequestBody @Valid CampusCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create campus >>>>> : {}", dto);
        val result = campusService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create campus success", 200,
                        "Create campus failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/campus:update")
    public ResponseEntity<?> update(@RequestBody @Valid Campus campus){
        logger.info(">>>>> Rest request to update campus >>>>> : {}", campus);
        val result = campusService.update(campus);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Update campus success", 200,
                        "Update campus failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/campus:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete campus >>>>> : {}", id);
        val result = campusService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete campus success", 200,
                        "Delete campus failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("/campus:fetchAll")
    public ResponseEntity<?> fetchAll(){
        logger.info(">>>>> Rest request to fetch all campus >>>>>");
        val result = campusService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all campus success", 200), HttpStatus.OK
        );
    }

}
