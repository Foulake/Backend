package com.team.dev.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Configuration
/*@EnableMethodSecurity(
		prePostEnabled = false,
		securedEnabled = true,
		jsr250Enabled = true
)*/
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	private static final String[] WHITE_LIST_URL = {
			"/api/v1/auth/**",
			"/api/v1/photo/**",
			"/api/v1/entreprises/create",
			//"/**/entreprises/create",
			"api/v1/sensor/**",
			"/api/v1/*/image/**",
			"/v2/api-docs",
			"/v3/api-docs",
			"/v3/api-docs/**",
			"/swagger-resources",
			"/swagger-resources/**",
			"/configuration/ui",
			"/configuration/security",
			"/swagger-ui/**",
			"/webjars/**",
			"/swagger-ui.html"};

	private static final String[] LIST_ARROSEUR = {
			"/api/v1/temperatures/**",
			"/api/v1/moteur/**",
			};

	private static final String[] LIST_MAGASINIER = {
			"/api/v1/products/**",
			"/api/v1/categories/**",
			"/api/v1/sortieProduits/**",
			"/api/v1/magasins/**",
			};

	private static final String[] LIST_GESTIONNAIRE = {
			"/api/v1/champs/**",
			"/api/v1/activites/**",
			"/api/v1/planifications/**",
			"/api/v1/sous-champs/**",
			"/api/v1/localites/**",
	};

	private final JwtAuthenticationFilter jwtAuthFilter;

	private final AuthenticationProvider authenticationProvider;

	//private  final CorsConfig corsConfig;

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//http.cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()));
		http.cors(AbstractHttpConfigurer::disable);
		//http.cors(Customizer.withDefaults());
		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests( httpRequest ->
						httpRequest.requestMatchers(WHITE_LIST_URL)

					.permitAll()
								//.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
					.requestMatchers(HttpHeaders.ALLOW).permitAll()
								//.requestMatchers(HttpMethod.POST, "/api/v1/entreprises").permitAll()
					//new 3 lignes have added
					.requestMatchers("/api/v1/entreprises/**").hasAuthority("SUPER_ADMIN")
					//.requestMatchers("/api/v1/users/**").hasAnyAuthority("USER")
					.requestMatchers(LIST_GESTIONNAIRE).hasAnyAuthority("GESTIONNAIRE","ADMIN","SUPER_ADMIN")
					.requestMatchers(LIST_MAGASINIER).hasAnyAuthority("MAGASINIER","ADMIN","SUPER_ADMIN")
					.requestMatchers(LIST_ARROSEUR).hasAnyAuthority("ARROSEUR","ADMIN","SUPER_ADMIN")
					.requestMatchers("/api/v1/**")
					.hasAnyAuthority("USER","ARROSEUR","MAGASINIER", "GESTIONNAIRE", "ADMIN", "SUPER_ADMIN")
					.anyRequest()
					.authenticated())
				//.exceptionHandling(exception -> exception.authenticationEntryPoint())
				.sessionManagement(httpSession ->
						httpSession.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider);
		http.exceptionHandling(httpExceptionHandling ->
				httpExceptionHandling.authenticationEntryPoint(
						(request, response, ex) -> {
							response.sendError(
									HttpServletResponse.SC_UNAUTHORIZED,
									ex.getMessage()
							);
						}
				));
		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/*@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("*");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}*/

}
