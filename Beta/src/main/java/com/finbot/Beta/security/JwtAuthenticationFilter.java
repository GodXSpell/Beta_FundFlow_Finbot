package com.finbot.Beta.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTtokenizer jwtTokenizer;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // Get JWT token from request
            String jwt = getJwtFromRequest(request);

            log.debug("JWT Token extracted: {}", jwt != null ? "Present" : "Absent");

            if (StringUtils.hasText(jwt)) {
                log.debug("Attempting to validate JWT token");

                // First validate the token structure and signature
                if (jwtTokenizer.validateToken(jwt)) {
                    log.debug("JWT token is valid");

                    // Extract user ID from token
                    String userId = jwtTokenizer.extractUserId(jwt);
                    log.debug("Extracted user ID from token: {}", userId);

                    // Load user details
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                    log.debug("User details loaded for userId: {}", userId);

                    // Create authentication object
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );

                    authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Set authentication in security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Authentication set in security context for user: {}", userId);
                } else {
                    log.warn("JWT token validation failed");
                }
            }
        } catch (UsernameNotFoundException ex) {
            log.error("User not found: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context: {}", ex.getMessage(), ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
