package com.calculator.manager.domain.security;

import com.calculator.manager.domain.security.impl.AuthenticationFilterImpl;
import com.calculator.manager.domain.service.ITokenService;
import com.calculator.manager.domain.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Class to configure security to block and allow routes.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private ITokenService tokenService;

	@Autowired
	private IUserService userService;

	/**
	 * Bean to configure authentication.
	 * This method is configured to use internal classes
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	/**
	 * Password Encoding
	 * Type of encoding to compare passwords
	 * This encoding is using to check passwords
 	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 *  Bean to configure routes that have access without and with token.
	 *  This method configure also the class filter to authentication.
	 */
	@Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf-> csrf.disable())
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
						.requestMatchers(
								"/v3/api-docs/**",
								"/v3/api-docs",
								"/swagger-ui.html",
								"/swagger-ui/**").permitAll()
						.anyRequest().authenticated()
				)
				.addFilterBefore(new AuthenticationFilterImpl(tokenService, userService), UsernamePasswordAuthenticationFilter.class)
				.build();
    }

	/**
	 * Configure cors to allow access from front end.
	 * If is necessary configure a specific cors to access is here that will be it.
	 * @return
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(Arrays.asList("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return source;
	}
}
