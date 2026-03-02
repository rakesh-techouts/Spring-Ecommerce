package com.techouts.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserPrincipal) {
            CustomUserPrincipal user = (CustomUserPrincipal) principal;
            HttpSession session = request.getSession(true);
            session.setAttribute("USER_ID", user.getId());
            session.setAttribute("USER_NAME", user.getDisplayName());
        }
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
