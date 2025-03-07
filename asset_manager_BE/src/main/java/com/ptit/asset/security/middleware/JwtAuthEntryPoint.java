package com.ptit.asset.security.middleware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {

        logger.error("Asset Management System Log [ Unauthorized error ] : Message -> {}" , e.getMessage());

        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED , "Error -> Unauthorized");
    }

}
// TODO - Link : https://subscription.packtpub.com/book/application_development/9781788995979/4/ch04lvl1sec36/custom-authenticationentrypoint
