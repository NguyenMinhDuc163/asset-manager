package com.ptit.asset.service.manager.impl;

import com.ptit.asset.external.StatisticalExcelExporterProviderService;
import com.ptit.asset.service.manager.StatisticalManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Service
public class StatisticalManagementImpl implements StatisticalManagement {

    @Autowired
    private StatisticalExcelExporterProviderService statisticalExcelExporterProviderService;


    @Override
    public void statistical(HttpServletResponse response, int year) throws IOException, SQLException {
        statisticalExcelExporterProviderService.export(response,year);
    }
}
