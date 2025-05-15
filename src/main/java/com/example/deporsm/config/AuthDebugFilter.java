package com.example.deporsm.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class AuthDebugFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // Log request details
        System.out.println("\n🔍 DEBUG: Incoming Request");
        System.out.println("📍 URL: " + request.getMethod() + " " + request.getRequestURI());
        
        // Log session info
        HttpSession session = request.getSession(false);
        System.out.println("🔑 Session: " + (session != null ? 
            "Active (ID: " + session.getId() + ", Creation: " + session.getCreationTime() + ")" : 
            "No session"));
        
        // Log cookies
        if (request.getCookies() != null) {
            System.out.println("🍪 Cookies:");
            for (Cookie cookie : request.getCookies()) {
                System.out.println("   " + cookie.getName() + "=" + cookie.getValue() +
                    " (Domain=" + cookie.getDomain() +
                    ", Path=" + cookie.getPath() +
                    ", MaxAge=" + cookie.getMaxAge() + ")");
            }
        } else {
            System.out.println("🍪 No cookies present");
        }
        
        // Log headers
        System.out.println("📋 Headers:");
        Collections.list(request.getHeaderNames()).forEach(headerName -> 
            System.out.println("   " + headerName + ": " + request.getHeader(headerName)));

        // Log Authentication info if present
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println("👤 Authentication: " + auth.getName() + 
                             " (Authenticated: " + auth.isAuthenticated() + 
                             ", Principal: " + auth.getPrincipal().getClass().getName() +
                             ", Authorities: " + auth.getAuthorities() + ")");
        } else {
            System.out.println("👤 No Authentication present");
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);

        // Log response details
        System.out.println("📤 Response:");
        System.out.println("   Status: " + response.getStatus());
        System.out.println("   Headers: ");
        response.getHeaderNames().forEach(headerName ->
            System.out.println("      " + headerName + ": " + response.getHeader(headerName)));
        System.out.println("🔍 END DEBUG\n");
    }
}