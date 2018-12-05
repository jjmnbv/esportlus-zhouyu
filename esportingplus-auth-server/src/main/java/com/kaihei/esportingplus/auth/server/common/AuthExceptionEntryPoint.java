package com.kaihei.esportingplus.auth.server.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException)
            throws ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), ResponsePacket
                    .onError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()));
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}