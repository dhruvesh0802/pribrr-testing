package com.pb.security;

import com.pb.constants.ResponseMessageConstant;
import com.pb.exception.CustomException;
import com.pb.utils.Constant;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtils jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(Constant.ACCESS_TOKEN);
        String userEmail = null;

        if (header != null) {
            String authToken = header;
            try {
                userEmail = jwtTokenUtil.getUsernameFromToken(authToken);

                Authentication authentication = userEmail != null ? jwtTokenUtil.getAuthentication(userEmail) : null;
               
                if(authentication != null)
                    SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (UsernameNotFoundException e){
                throw new CustomException(HttpStatus.NOT_FOUND,"Username & Password not found");
            }
            catch (ExpiredJwtException | IllegalArgumentException e) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, ResponseMessageConstant.INVALID_JWT_TOKEN);
            }
    
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                logger.info("authenticated user " + userEmail + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }

    
        chain.doFilter(req, res);
    }

}
