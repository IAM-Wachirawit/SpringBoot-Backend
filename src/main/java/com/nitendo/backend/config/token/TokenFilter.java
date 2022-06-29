package com.nitendo.backend.config.token;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nitendo.backend.service.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokenFilter extends GenericFilterBean {

    private final TokenService tokenService;

    public TokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Change Servlet to Http
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Get data token by header
        String authorization = httpRequest.getHeader("Authorization");

        // check have header = Authorization
        if (ObjectUtils.isEmpty(authorization)) {   // Is empty return
            chain.doFilter(request, response);
            return;
        }

        // check Authorization type Bearer
        if ( !authorization.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get data token
        String token = authorization.substring(7); // position 7

        // verify token
        DecodedJWT decoded = tokenService.verify(token);
        if ( decoded == null) {
            chain.doFilter(request, response);
            return;
        }

        // token Pass get value principal and rolw
        String  principal = decoded.getClaim("principal").asString();   // user id
        String role = decoded.getClaim("role").asString();

        // Create list role Authority spring
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        // authenticationToken user, password, role)
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal,
                "protected",
                authorities);

        // set security context ว่า user คนนี้ login เข้ามาแล้วด้วยค่าดังต่อไปนี้
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}
