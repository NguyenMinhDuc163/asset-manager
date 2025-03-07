package com.ptit.asset.web.rest.v1;

import com.ptit.asset.domain.User;
import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.FetchPageUserRequestDTO;
import com.ptit.asset.dto.request.UserUpdateRequestDTO;
import com.ptit.asset.external.UserExcelExporterProvider;
import com.ptit.asset.service.endservice.UserService;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
public class UserResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get user by id >>>>> : {}", id);
        val result = userService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get user success", 200,
                        "Get user failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/user:update")
    public ResponseEntity<?> update(@RequestBody @Valid UserUpdateRequestDTO dto){
        logger.info(">>>>> Rest request to update user >>>>> : {}", dto);
        val result = userService.update(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Update user success", 200,
                        "Update user failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @GetMapping("/user:fetchAll")
    public ResponseEntity<?> fetchAll(){
        val result = userService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all user success", 200), HttpStatus.OK
        );
    }

    @PostMapping("/user:fetchPage")
    public ResponseEntity<?> fetchPage(@RequestBody @Valid FetchPageUserRequestDTO dto){
        val result = userService.fetchPage(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch page user success", 200), HttpStatus.OK
        );
    }



    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @GetMapping("/user:exportExcel")
    public ResponseEntity<?> exportExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<User> listUsers = userService.fetchAll();

        UserExcelExporterProvider userExcelExporterProvider = new UserExcelExporterProvider(listUsers);

        userExcelExporterProvider.export(response);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}
