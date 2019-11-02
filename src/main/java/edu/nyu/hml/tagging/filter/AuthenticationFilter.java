package edu.nyu.hml.tagging.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class AuthenticationFilter implements Filter {

    @Value("${app.auth_token}")
    private String authToken;

    private static final String HEADER_KEY = "x-auth";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String authValue = req.getHeader(HEADER_KEY);
        if(authValue == null || !authToken.equals(authValue)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The x-auth header is not valid.");
            return;
        }
        chain.doFilter(request, response);
    }

}
