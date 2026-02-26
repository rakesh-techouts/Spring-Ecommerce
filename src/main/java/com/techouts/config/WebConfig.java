package com.techouts.config;


import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{DispatcherServlet.class, HibernateConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }

/*    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        boolean h2Enabled = Boolean.parseBoolean(resolveProperty("app.h2.console.enabled", servletContext, "false"));
        if (h2Enabled) {
            ServletRegistration.Dynamic h2 = servletContext.addServlet("H2Console", "org.h2.server.web.JakartaWebServlet");
            h2.setLoadOnStartup(2);
            h2.addMapping(resolveProperty("app.h2.console.path", servletContext, "/h2-console/*"));
        }
    }

    private String resolveProperty(String key, ServletContext servletContext, String defaultValue) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) {
            return sys;
        }
        String env = System.getenv(key.toUpperCase().replace('.', '_'));
        if (env != null && !env.isBlank()) {
            return env;
        }
        String ctx = servletContext.getInitParameter(key);
        if (ctx != null && !ctx.isBlank()) {
            return ctx;
        }
        return defaultValue;
    }*/
}
