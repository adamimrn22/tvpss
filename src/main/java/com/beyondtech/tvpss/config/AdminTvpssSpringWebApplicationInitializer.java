package com.beyondtech.tvpss.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AdminTvpssSpringWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { RootConfig.class }; // Root configuration class
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebAppConfig.class }; // Web configuration class
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" }; // Servlet mapping
	}

	@Override
	protected String getServletName() {
		return "tvpss-dispatcher"; //  dispatcher servlet
	}
}
