package com.ptit.asset.service.endservice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public interface StatisticalService {

    void statistical(HttpServletResponse response, int year) throws IOException, SQLException;

}
