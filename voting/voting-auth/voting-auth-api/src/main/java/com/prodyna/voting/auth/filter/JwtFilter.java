package com.prodyna.voting.auth.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_HEADER_PARAM = "Authorization";
    private static final int TOKEN_BEGIN_INDEX = 7;
    private String secretKey;

    public JwtFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        if (shouldSkipFilter(request)) {
            chain.doFilter(req, res);
            return;
        }

        final String authHeader = request.getHeader(AUTHORIZATION_HEADER_PARAM);
        if (noAuthToken(authHeader)) {
            response.sendError(HttpStatus.FORBIDDEN.value());
            chain.doFilter(req, res);

            return;
        }

        final String token = authHeader.substring(TOKEN_BEGIN_INDEX);

        try {
            final Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            request.setAttribute("claims", claims);
        } catch (final SignatureException e) {
            response.sendError(HttpStatus.FORBIDDEN.value());
            chain.doFilter(req, res);

            return;
        }

        chain.doFilter(req, res);
    }

    private boolean noAuthToken(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    private boolean shouldSkipFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/user/login") || (request.getRequestURI().equals("/api/votes") &&
                request.getMethod().equals(RequestMethod.GET.name()));
    }
}
