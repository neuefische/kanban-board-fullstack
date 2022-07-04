package de.neuefische.muc.kanban.security;

import de.neuefische.muc.kanban.user.KanbanUser;
import de.neuefische.muc.kanban.user.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getToken(request);
        if (token != null && !token.isBlank()) {
            try {
                Claims claims =  jwtService.extractAllClaims(token);
                setSecurityContext(claims);
            } catch (Exception e) {
                response.setStatus(401);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            return authorization.replace("Bearer", "").trim();
        }
        return null;
    }

    private void setSecurityContext(Claims claims) {
        List<SimpleGrantedAuthority> grantedAuthorities = claims.get("roles") == null ? List.of() : ((List<String>) claims.get("roles")).stream().map(SimpleGrantedAuthority::new).toList();

        KanbanUser user = userService.findById(claims.getSubject()).orElseThrow();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), "", grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
