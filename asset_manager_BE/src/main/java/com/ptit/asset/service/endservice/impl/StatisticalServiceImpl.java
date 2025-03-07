package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.service.endservice.StatisticalService;
import com.ptit.asset.service.manager.StatisticalManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Service
public class StatisticalServiceImpl implements StatisticalService {

    @Autowired
    private StatisticalManagement statisticalManagement;

    @Override
    public void statistical(HttpServletResponse response, int year) throws IOException, SQLException {
        statisticalManagement.statistical(response, year);
    }

}
