package com.prodyna.voting.auth;

import com.prodyna.voting.auth.filter.SecurityFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class SecurityFilterTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    private SecurityFilter securityFilter;

    @Before
    public void setUp() {
        securityFilter = new SecurityFilter("4252");

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @Test
    public void returnsUnauthorizedStatusForGetAllPost() throws IOException, ServletException {
        when(request.getMethod()).thenReturn(RequestMethod.GET.name());
        when(request.getRequestURI()).thenReturn("/api/polls");

        securityFilter.doFilter(request, response, chain);
        verify(response, times(1)).sendError(HttpStatus.UNAUTHORIZED.value());
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    public void returnsUnauthorizedStatusForPollsWithPost() throws IOException, ServletException {
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getRequestURI()).thenReturn("/api/polls");

        securityFilter.doFilter(request, response, chain);
        verify(response, times(1)).sendError(HttpStatus.UNAUTHORIZED.value());
        verify(chain, never()).doFilter(request, response);
    }
}
