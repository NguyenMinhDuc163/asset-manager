package com.ptit.asset.web.rest.v1;

import com.ptit.asset.domain.Organization;
import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.FetchPageOrganizationRequestDTO;
import com.ptit.asset.dto.request.OrganizationCreateRequestDTO;
import com.ptit.asset.service.endservice.OrganizationService;
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
public class OrganizationResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/organization/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get organization by id >>>>> : {}", id);
        val result = organizationService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get organization success", 200,
                        "Get organization failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/organization:create")
    public ResponseEntity<?> create(@RequestBody @Valid OrganizationCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create organization >>>>> : {}", dto);
        val result = organizationService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create organization success", 200,
                        "Create organization failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/organization:update")
    public ResponseEntity<?> update(@RequestBody @Valid Organization organization){
        logger.info(">>>>> Rest request to update organization >>>>> : {}", organization);
        val result = organizationService.update(organization);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Update organization success", 200,
                        "Update organization failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/organization:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete organization >>>>> : {}", id);
        val result = organizationService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete organization success", 200,
                        "Delete organization failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @GetMapping("/organization:fetchAll")
    public ResponseEntity<?> fetchAll(){
        val result = organizationService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all organization success", 200), HttpStatus.OK
        );
    }

    @PostMapping("/organization:fetchPage")
    public ResponseEntity<?> fetchPage(@RequestBody @Valid FetchPageOrganizationRequestDTO dto){
        val result = organizationService.fetchPage(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch page organization success", 200), HttpStatus.OK
        );
    }

}
