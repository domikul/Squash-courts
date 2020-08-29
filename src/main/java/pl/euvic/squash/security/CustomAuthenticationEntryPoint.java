package pl.euvic.squash.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pl.euvic.squash.exception.ExceptionView;
import pl.euvic.squash.security.helper.ServletHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static pl.euvic.squash.exception.ExceptionMessage.UNAUTHORIZED;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ServletHelper.sendException(
                new ExceptionView(HttpServletResponse.SC_UNAUTHORIZED, authException.getClass().getName(), UNAUTHORIZED.getMessage()),
                response);
    }
}