package com.portal.exam; 

    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.web.AuthenticationEntryPoint;
    import org.springframework.stereotype.Component;

    import javax.servlet.ServletException;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.IOException;

    /**
     * This class will be invoked when an unauthenticated user tries to access a protected resource.
     * It sends an HTTP 401 Unauthorized response, which is suitable for REST APIs.
     */
    @Component
    public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException authException) throws IOException, ServletException {
           
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized : Server");
        }
    }