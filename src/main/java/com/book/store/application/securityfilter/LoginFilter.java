package com.book.store.application.securityfilter;


import com.book.store.application.util.FilterExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class LoginFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("in login filter");

        Cookie[] cookies = request.getCookies();
        boolean loggedIn = false;
        System.out.println(cookies);
        if (cookies != null)
            for(Cookie cookie : cookies){
                System.out.println(cookie.getName().equals("at")+ "in login filter");

                if (cookie.getName().equals("at") || cookie.getName().equals("rt")) {
                    loggedIn = true;
                }
            }
        System.out.println(loggedIn+" in login filter");

        if (loggedIn) {
            FilterExceptionHandler.handleJwtExpire(response,
                    HttpStatus.UNAUTHORIZED.value(),
                    "Failed to Login",
                    "Please Logout first");
            return;
        }
        else
            filterChain.doFilter(request, response);
    }
}