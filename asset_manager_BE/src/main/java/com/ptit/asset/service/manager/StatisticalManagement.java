package com.ptit.asset.service.manager;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public interface StatisticalManagement {

    void statistical(HttpServletResponse response, int year) throws IOException, SQLException;

}
