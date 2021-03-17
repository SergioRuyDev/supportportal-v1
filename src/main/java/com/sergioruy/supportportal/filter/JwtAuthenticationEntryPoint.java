package com.sergioruy.supportportal.filter;

import com.sergioruy.supportportal.domain.HttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.sergioruy.supportportal.constant.SecurityConstant.*;
import static org.springframework.http.HttpStatus.*;

public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        HttpResponse httpResponse = new HttpResponse(
                FORBIDDEN.value(), FORBIDDEN, FORBIDDEN.getReasonPhrase().toUpperCase(),
                FORBIDDEN_MESSAGE);

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }
}
