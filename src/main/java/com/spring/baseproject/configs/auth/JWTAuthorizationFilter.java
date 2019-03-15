package com.spring.baseproject.configs.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.components.rbac.InMemoryRoutesDictionary;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.auth.AuthorizationException;
import com.spring.baseproject.modules.auth.models.dtos.AuthorizedUser;
import com.spring.baseproject.modules.auth.services.IAuthorization;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private IAuthorization authorization;
    private InMemoryRoutesDictionary inMemoryRoutesDictionary;

    public JWTAuthorizationFilter(IAuthorization authorization,
                                  InMemoryRoutesDictionary inMemoryRoutesDictionary) {
        this.authorization = authorization;
        this.inMemoryRoutesDictionary = inMemoryRoutesDictionary;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        Authentication authentication = null;
        AuthorizedUser authorizedUser = null;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            authorizedUser = (AuthorizedUser) authentication.getPrincipal();
        } catch (Exception ignore) {
        }

        if (authentication != null && authorizedUser != null) {
            try {
                String route = inMemoryRoutesDictionary.getRoute(uri);
                if (route != null) {
                    authorization.authorizeUser(method, route,
                            authorizedUser, authentication.getAuthorities());
                }
                filterChain.doFilter(request, response);
            } catch (AuthorizationException e) {
                responseErrorMessage(response, e.getResponseValue());
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void responseErrorMessage(HttpServletResponse response,
                                      ResponseValue responseValue) throws IOException {
        response.setStatus(responseValue.httpStatus().value());
        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(new BaseResponseBody<>(responseValue, null)));
    }
}
