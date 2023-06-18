package com.fileoperations.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
	

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


	 @Override
	    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
	            throws ServletException, IOException {
	            String token= getJWTfromRequest(httpServletRequest);
	            if(StringUtils.hasText(token)&& tokenProvider.validateToken(token)){
	                String username= tokenProvider.getUsernameFromJWT(token);
	                UserDetails userDetails= customUserDetailsService.loadUserByUsername(username);
	                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
	                        userDetails,null,userDetails.getAuthorities()
	                );
	                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
	                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	            }
	             filterChain.doFilter(httpServletRequest,httpServletResponse);

	    }

	
	
    private String getJWTfromRequest(HttpServletRequest request){
        String bearerToken=request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }


}
