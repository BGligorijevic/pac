package com.prodyna.voting.auth;

import com.prodyna.voting.auth.filter.JwtFilter;
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
public class JwtFilterTest {

    private static final String URL_POLLS = "/api/polls";
    private static final String URL_LOGIN = "/user/login";
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    private JwtFilter jwtFilter;

    @Before
    public void setUp() {
        jwtFilter = new JwtFilter("4252");

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @Test
    public void skipsFilterForGetAllPolls() throws IOException, ServletException {
        when(request.getMethod()).thenReturn(RequestMethod.GET.name());
        when(request.getRequestURI()).thenReturn(URL_POLLS);

        jwtFilter.doFilter(request, response, chain);
        verify(response, times(0)).sendError(anyInt());
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void returnsForbiddenStatusForPollsWithPost() throws IOException, ServletException {
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getRequestURI()).thenReturn(URL_POLLS);

        jwtFilter.doFilter(request, response, chain);
        verify(response, times(1)).sendError(HttpStatus.FORBIDDEN.value());
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void skipsFilterForLogin() throws IOException, ServletException {
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getRequestURI()).thenReturn(URL_LOGIN);

        jwtFilter.doFilter(request, response, chain);
        verify(response, times(0)).sendError(anyInt());
        verify(chain, times(1)).doFilter(request, response);
    }
}
