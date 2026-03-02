package com.techouts.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class NoCacheFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            String uri = httpRequest.getRequestURI();
            String contextPath = httpRequest.getContextPath();

            boolean staticResource = uri.startsWith(contextPath + "/css/")
                    || uri.startsWith(contextPath + "/js/")
                    || uri.startsWith(contextPath + "/images/")
                    || uri.contains(".css")
                    || uri.contains(".js")
                    || uri.contains(".png")
                    || uri.contains(".jpg")
                    || uri.contains(".jpeg")
                    || uri.contains(".gif")
                    || uri.contains(".svg")
                    || uri.contains(".woff")
                    || uri.contains(".woff2");

            if (!staticResource) {
                httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                httpResponse.setHeader("Pragma", "no-cache");
                httpResponse.setDateHeader("Expires", 0);
            }
        }

        chain.doFilter(request, response);
    }
}
