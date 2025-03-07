package com.ptit.asset.web.rest.v1;

import com.ptit.asset.domain.Group;
import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.GroupCreateRequestDTO;
import com.ptit.asset.service.endservice.GroupService;
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
public class GroupResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GroupService groupService;

    @GetMapping("/group/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get group by id >>>>> : {}", id);
        val result = groupService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get group success", 200,
                        "Get group failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/group:create")
    public ResponseEntity<?> create(@RequestBody @Valid GroupCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create group >>>>> : {}", dto);
        val result = groupService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create group success", 200,
                        "Create group failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/group:update")
    public ResponseEntity<?> update(@RequestBody @Valid Group group){
        logger.info(">>>>> Rest request to update group >>>>> : {}", group);
        val result = groupService.update(group);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Update group success", 200,
                        "Update group failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/group:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete group >>>>> : {}", id);
        val result = groupService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete group success", 200,
                        "Delete group failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @GetMapping("/group:fetchAll")
    public ResponseEntity<?> fetchAll(){
        val result = groupService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all group success", 200), HttpStatus.OK
        );
    }
}
