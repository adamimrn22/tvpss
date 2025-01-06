package com.beyondtech.tvpss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@SuppressWarnings({ "deprecation", "removal" })
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// Configuring authorization rules
		http.authorizeRequests(requests -> requests.requestMatchers(new AntPathRequestMatcher("/")).authenticated()
				.requestMatchers(new AntPathRequestMatcher("/home")).permitAll() // No authentication needed for /home
				.requestMatchers(new AntPathRequestMatcher("/register")).permitAll() // No authentication needed for
				.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN") // Only accessible to ADMIN //
																							// role
				.anyRequest().authenticated() // Authenticate all other URLs
		);
		// Enabling form login
		http.formLogin(login -> login.permitAll());

		// Enabling logout
		http.logout(logout -> logout.permitAll());

		// Disabling CSRF
		http.csrf(csrf -> csrf.disable());

		return http.build();
	}

	@Bean
	public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
		return new HandlerMappingIntrospector();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		manager.createUser(User.withUsername("user").password(passwordEncoder().encode("user")).roles("USER").build());
		manager.createUser(
				User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("USER", "ADMIN").build());

		return manager;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
