package com.beyondtech.tvpss.config;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
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

	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		// Configure multipart handling
		String location = System.getProperty("java.io.tmpdir");

		// MultipartConfigElement parameters:
		// location: temporary file upload directory
		// maxFileSize: maximum size of a single file
		// maxRequestSize: maximum size of a request (including all files)
		// fileSizeThreshold: size threshold after which files will be written to disk
		MultipartConfigElement multipartConfig = new MultipartConfigElement(
				location,
				10 * 1024 * 1024,  // 10MB max file size
				50 * 1024 * 1024,  // 50MB max request size
				0  // Size threshold after which files will be written to disk
		);

		registration.setMultipartConfig(multipartConfig);
	}
}
