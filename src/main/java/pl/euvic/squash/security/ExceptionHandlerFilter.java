package pl.euvic.squash.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.euvic.squash.exception.ExceptionView;
import pl.euvic.squash.exception.NotAuthenticatedException;
import pl.euvic.squash.security.helper.ServletHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (NotAuthenticatedException | IOException e) {

            ExceptionView exceptionView = new ExceptionView(HttpStatus.UNAUTHORIZED.value(), e.getClass().getName(), e.getMessage());
            ServletHelper.sendException(exceptionView, response);
        }
    }


}
