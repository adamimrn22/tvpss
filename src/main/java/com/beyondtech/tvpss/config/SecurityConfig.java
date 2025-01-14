package com.beyondtech.tvpss.config;

import com.beyondtech.tvpss.auth.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.beyondtech.tvpss.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private CustomAuthenticationSuccessHandler successHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/resources/**", "/images/**", "/css/**", "/js/**").permitAll()
						.requestMatchers("/SuperAdmin/**").hasRole("superadmin")
						.requestMatchers("/StateAdmin/**").hasRole("stateadmin")
						.requestMatchers("/AdminPPD/**").hasRole("ppdadmin")
						.requestMatchers("/SchoolAdmin/**", "/SubmitTVPSSVersion").hasRole("schooladmin")
						.requestMatchers("/InformasiTVPSS").hasAnyRole("stateadmin", "ppdadmin")
						.requestMatchers("/schools/district/**").authenticated()
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.permitAll()
						.defaultSuccessUrl("/", true)
						.successHandler(successHandler))
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login")
						.permitAll())
				.userDetailsService(userDetailsService);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
