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

/**
 * Json web token filter which intercepts all incoming requests.
 * Checks prove that valid token is sent as part of the incoming request (header).
 * There are certain points which are allowed through filter without check (e.g. /login).
 */
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
        } catch (final Exception e) {
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
        for (SkippedResources skippedResource : SkippedResources.values()) {
            if (request.getRequestURI().equals(skippedResource.getUrl()) &&
                    skippedResource.getHttpMethod().name().equals(request.getMethod())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Resources for which to skip filter checks.
     */
    private enum SkippedResources {

        LOGIN("/user/login", RequestMethod.POST),
        GET_VOTES("/api/votes", RequestMethod.GET);

        private String url;

        private RequestMethod httpMethod;

        SkippedResources(String url, RequestMethod httpMethod) {
            this.url = url;
            this.httpMethod = httpMethod;
        }

        public RequestMethod getHttpMethod() {
            return httpMethod;
        }

        public String getUrl() {
            return url;
        }
    }
}
