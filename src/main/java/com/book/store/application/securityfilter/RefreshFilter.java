package com.book.store.application.securityfilter;




import com.book.store.application.enums.UserRole;
import com.book.store.application.jwt.JwtService;
import com.book.store.application.jwt.RefreshToken;
import com.book.store.application.repository.RefreshTokenRepository;
import com.book.store.application.util.FilterExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class RefreshFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String rt = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("rt"))
                    rt = cookie.getValue();
            }
        } else {
            FilterExceptionHandler.handleJwtExpire(response,
                    HttpStatus.UNAUTHORIZED.value(),
                    "Failed to check refresh token",
                    "Refresh Token is not present");
            return;
        }

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByRefreshToken(rt);
        if (refreshToken.isPresent() && !refreshToken.get().isBlocked()) {
            String username = jwtService.extractUserName(rt);
            UserRole userRole = jwtService.extractUserRole(rt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority(userRole.name())));
                upat.setDetails(new WebAuthenticationDetails(request));
//              we store inside security context holder
                SecurityContextHolder.getContext().setAuthentication(upat);
            }
        } else {
            FilterExceptionHandler.handleJwtExpire(response,
                    HttpStatus.UNAUTHORIZED.value(),
                    "Failed to check refresh token",
                    "Refresh Token is already expired");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
