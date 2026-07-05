package com.app.sakila.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SingleSessionFilter extends OncePerRequestFilter {

    private final ConcurrentHashMap<String, String> activeSessions = new ConcurrentHashMap<>();
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public SingleSessionFilter(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.contains("/api/auth/login") || path.contains("/api/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsernameFromToken(token);
            String existing = activeSessions.get(username);

            if (existing != null && !existing.equals(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                objectMapper.writeValue(response.getOutputStream(),
                        Map.of("error", "Another session is active for this user"));
                return;
            }

            activeSessions.put(username, token);
        }

        filterChain.doFilter(request, response);
    }

    public void registerSession(String username, String token) {
        activeSessions.put(username, token);
    }

    public void removeSession(String username) {
        activeSessions.remove(username);
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
