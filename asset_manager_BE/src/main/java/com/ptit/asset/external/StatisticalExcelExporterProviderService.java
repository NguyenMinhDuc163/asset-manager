package com.ptit.asset.external;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public interface StatisticalExcelExporterProviderService {

    void export(HttpServletResponse response, int year) throws IOException, SQLException;

}
