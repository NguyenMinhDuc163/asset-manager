package com.ptit.asset.web.rest.v1;

import com.ptit.asset.service.endservice.StatisticalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
//@PreAuthorize("hasRole('ACCOUNTANT')")
//@PreAuthorize("hasRole('ADMIN') or hasRole('CHIEF_ACCOUNTANT')")
public class StatisticalResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StatisticalService statisticalService;

    @GetMapping("/statistical/{year}")
    public ResponseEntity<?> statistical(HttpServletResponse response, @PathVariable("year") int year) throws IOException, SQLException {

        logger.info(">>>>> Rest request to statistical >>>>>");
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=inventory_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        statisticalService.statistical(response, year);
        return new ResponseEntity<>("Export Success", HttpStatus.OK);
    }

}
