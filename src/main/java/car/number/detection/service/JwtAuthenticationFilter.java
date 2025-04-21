package car.number.detection.service;

import car.number.detection.entity.Role;
import car.number.detection.repository.TokenRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final JwtService jwtService;
    private final UserService userService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getServletPath();
        log.info("Пришёл запрос по пути: {}", path);
        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        var authHeader = request.getHeader(HEADER_NAME);
        if (ObjectUtils.isEmpty(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = authHeader.substring(BEARER_PREFIX.length());
        try {
            String username = jwtService.extractUserName(token);
            log.debug("Обработка токена пользователя: {}", username);

            if (!jwtService.extractType(token).equals("access")) {
                tokenRepository.deleteById(token);
                filterChain.doFilter(request, response);
                return;
            }

            if (!ObjectUtils.isEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                Role role = jwtService.extractRole(token);
                UserDetails userDetails = userService.getByUsernameAndRole(username, role);

                SecurityContext context = SecurityContextHolder.createEmptyContext();

                if (!jwtService.isTokenValid(token, userDetails)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        } catch (JwtException e) {
            log.debug("Ошибка в токене", e);
        } catch (UsernameNotFoundException e) {
            log.debug("Пользователь не найден при обработке токена", e);
        }
        filterChain.doFilter(request, response);
    }
}
