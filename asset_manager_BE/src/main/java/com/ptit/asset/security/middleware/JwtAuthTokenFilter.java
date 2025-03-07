package com.ptit.asset.security.middleware;

import com.ptit.asset.security.UserDetailServiceHandler;
import com.ptit.asset.security.jwt.JwtProvider;
import org.springframework.web.filter.OncePerRequestFilter;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserDetailServiceHandler userDetailService;

    private Try<String> getJwt(HttpServletRequest httpServletRequest) {

        return Option.of(httpServletRequest.getHeader("Authorization"))
        .toTry(() -> new Exception("Authorization is null"))
        .filter((authHeader) -> authHeader.startsWith("Bearer "))
        .flatMap(authHeader -> {
            return Try.of(() -> {
                return authHeader.replace("Bearer ", "");
            });
        });
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        Try<String> jwt = getJwt(httpServletRequest);
        if (jwt.isSuccess()) {
            if (jwtProvider.validateJwtToken(jwt.get())) {

                String username = jwtProvider.getUserNameFromJwtToken(jwt.get());

                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

}
