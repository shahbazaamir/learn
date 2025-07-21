package com.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {
        // Extract and validate JWT here
                                  //  String auth=    request.getHeader("Authorization");
                                  //  System.out.println("\n\n\nauth"+auth);
                                  //  String [] arr = auth.split("\\s");
                                  //  System.out.println("\n\n\n arr"+arr[1]);
         //                               UsernamePasswordAuthenticationToken authToken =
         //                                                       new UsernamePasswordAuthenticationToken(
        // Skip setting SecurityContext if token is missing or invalid
                                      //  System.out.println("\n\n\n jwt filter");
                                     //   System.out.println(request);
        filterChain.doFilter(request, response);
    }
}
