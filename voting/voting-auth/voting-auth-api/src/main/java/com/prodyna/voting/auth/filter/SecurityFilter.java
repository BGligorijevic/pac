package com.prodyna.voting.auth.filter;

import com.prodyna.voting.auth.user.Role;
import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.common.Reject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Security filter which intercepts all incoming requests under {@link SecurityFilter#SECURED_API_PATH}.
 * Authorization is implemented with Json Web Token.
 * Checks prove that valid token is sent as part of the incoming request (header).
 */
@Component
@Log4j
public class SecurityFilter extends GenericFilterBean {

    public static final String SECURED_API_PATH = "/api/*";
    private static final String AUTHORIZATION_HEADER_PARAM = "Authorization";
    private static final int TOKEN_BEGIN_INDEX = 7;

    @Value("${voting.app.secret.key}")
    private String secretKey;

    public SecurityFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        if (request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
            chain.doFilter(req, res);
            return;
        }

        final String authHeader = request.getHeader(AUTHORIZATION_HEADER_PARAM);
        if (noAuthToken(authHeader)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        try {
            String token = authHeader.substring(TOKEN_BEGIN_INDEX);
            User user = parseToken(token);
            request.setAttribute("user", user);
            chain.doFilter(req, res);
        } catch (final Exception e) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return;
        }
    }

    private User parseToken(String token) {
        final Claims jwtClaims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        if (jwtClaims == null || jwtClaims.get("role") == null || jwtClaims.getSubject() == null) {
            Reject.always("Illegal token received.");
        }
        String roleName = (String) jwtClaims.get("role");
        Role role = Role.forRoleValue(roleName);

        User user = new User();
        user.setUserName(jwtClaims.getSubject());
        user.setRole(role);

        return user;
    }

    private boolean noAuthToken(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }
}
