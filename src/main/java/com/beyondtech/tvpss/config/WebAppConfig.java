package com.beyondtech.tvpss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.beyondtech.tvpss")
public class WebAppConfig implements WebMvcConfigurer {

	// Mapping for static resources
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Mapping for CSS files
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");

		// Mapping for JS files
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");

		// Mapping for Image files
		registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
	}

	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding("UTF-8");
		resolver.setOrder(1);
//		resolver.setViewNames(null);
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		engine.setEnableSpringELCompiler(true); // Enables Spring Expression Language (SpEL)
		return engine;
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix("/WEB-INF/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML");
		resolver.setCharacterEncoding("UTF-8");
		resolver.setCacheable(false); // Disable caching for development
		return resolver;
	}
}
