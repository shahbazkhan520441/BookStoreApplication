package com.book.store.application.securityfilter;

import com.book.store.application.enums.UserRole;
import com.book.store.application.jwt.AccessToken;
import com.book.store.application.jwt.JwtService;
import com.book.store.application.jwt.RefreshToken;
import com.book.store.application.repository.AccessTokenRepository;
import com.book.store.application.repository.RefreshTokenRepository;
import com.book.store.application.util.FilterExceptionHandler;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;



@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenRepository accessTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("in jwt auth filter");
        Cookie[] cookies = request.getCookies();
        System.out.println(cookies);
        String rt = null;
        String at = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("rt")) {
                    rt = cookie.getValue();
                    System.out.println(rt + "refresh token auth filter");
                }
                else if (cookie.getName().equals("at")) {
                    at = cookie.getValue();
                    System.out.println(at + "access token auth filter");
                }
            }
        }
        if (at != null && rt != null) {
            Optional<RefreshToken> optionalRT = refreshTokenRepository.findByRefreshToken(rt);
            Optional<AccessToken> optionalAT = accessTokenRepository.findByAccessToken(at);

            if (optionalRT.isPresent() && optionalAT.isPresent()) {
                RefreshToken refreshToken = optionalRT.get();
                AccessToken accessToken = optionalAT.get();
                if (!refreshToken.isBlocked() & !accessToken.isBlocked()) {
                    try {
                        Date expireDate = jwtService.extractExpirationDate(at);
                        String username = jwtService.extractUserName(at);
                        UserRole userRole = jwtService.extractUserRole(at);

                        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                            UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority(userRole.name())));
                            upat.setDetails(new WebAuthenticationDetails(request));
//              we store inside security context holder // test commit
                            SecurityContextHolder.getContext().setAuthentication(upat);
                        }
                    } catch (ExpiredJwtException e) {
                        FilterExceptionHandler.handleJwtExpire(response,
                                HttpStatus.UNAUTHORIZED.value(),
                                "Failed to authenticate",
                                "Token has already expired");
                        return;
                    } catch (JwtException e) {
                        FilterExceptionHandler.handleJwtExpire(response,
                                HttpStatus.UNAUTHORIZED.value(),
                                "Failed to authenticate",
                                "you are not allowed to access this resource");
                        return;
                    }
                }
            } else {
                FilterExceptionHandler.handleJwtExpire(response,
                        HttpStatus.UNAUTHORIZED.value(),
                        "Failed to authenticate",
                        "Please login first your token is expired");
                return;
            }
        }


        System.out.println("out of jwt auth filter");
        filterChain.doFilter(request, response);
    }
}