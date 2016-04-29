package com.prodyna.voting.auth;

import com.prodyna.voting.auth.user.Role;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Rest resources.
 */
public enum RestResources {

    GET_ALL_POLLS(UrlPaths.POLLS_URL, RequestMethod.GET, Role.USER);

    private String url;
    private RequestMethod httpMethod;
    private Role minRequiredRole;

    RestResources(String url, RequestMethod httpMethod, Role minRequiredRole) {
        this.url = url;
        this.httpMethod = httpMethod;
        this.minRequiredRole = minRequiredRole;
    }

    public Role getMinRequiredRole() {
        return minRequiredRole;
    }

    public static RestResources getForUrlAndPath(String url, String httpMethod) {
        for (RestResources resource : RestResources.values()) {
            if (url.startsWith(resource.url) && resource.httpMethod.toString().equals(httpMethod)) {
                return resource;
            }
        }

        throw new IllegalArgumentException("No such resource exists");
    }

    public boolean hasRole(Role userRole) {
        return userRole.getPower() >= this.getMinRequiredRole().getPower();
    }

    public static class UrlPaths {
        public static final String POLLS_URL = "/api/polls";
    }
}
