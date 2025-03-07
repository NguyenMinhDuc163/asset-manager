package com.ptit.asset.web.rest.v1;

import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.service.endservice.BackupRestoreService;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/system")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
public class BackupRestoreResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BackupRestoreService backupRestoreService;

    @GetMapping("/backup:fetchAllVersion")
    public ResponseEntity<?> fetchAllVersion(){
        val result = backupRestoreService.fetchAllVersion();
        return new ResponseEntity<>(
            RestResponseUtils.create(result,"Fetch all version backup success", 200), HttpStatus.OK
        );
    }

    @GetMapping("/backup:createBackup")
    public ResponseEntity<?> createBackup(){
        val result = backupRestoreService.createBackup();
        return new ResponseEntity<>(
            RestResponseUtils.create(result,"Create an backup success", 200,
                    "Create an backup failure", 500),
            result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("/restoreDatabase")
    public ResponseEntity<?> restore(@Param("versionBackup") Long versionBackup){
        val result = backupRestoreService.restoreDatabase(versionBackup);
        return new ResponseEntity<>(
            RestResponseUtils.create(result,"Restore database success", 200,
                    "Restore database failure", 500),
            result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
