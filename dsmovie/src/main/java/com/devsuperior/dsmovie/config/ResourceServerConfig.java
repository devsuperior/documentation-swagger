package com.devsuperior.dsmovie.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Value("${cors.origins}")
	private String corsOrigins;

	@Autowired
	private Environment env;
	
	@Autowired
	private JwtTokenStore tokenStore;

	private static final String[] PUBLIC = { 
			"/oauth/token", 
			"/h2-console/**"
	};
	
	private static final String[] SWAGGER = {	
		"/v2/api-docs",
		"/configuration/ui",
		"/swagger-resources/**",
		"/configuration/security",
		"/swagger-ui.html",
		"/webjars/**"
	};

	private static final String[] ALL_USERS_GET = { "/movies/**" };

	private static final String[] ALL_USERS_PUT = { "/scores/**" };

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}
	
	public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers(HttpMethod.OPTIONS, "/**");
        // ignore swagger 
        web.ignoring().mvcMatchers(SWAGGER);
    }

	@Override
	public void configure(HttpSecurity http) throws Exception {

		// H2
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll()
		.antMatchers(SWAGGER).permitAll()
		.antMatchers(HttpMethod.GET, ALL_USERS_GET).permitAll()
		.antMatchers(HttpMethod.PUT, ALL_USERS_PUT).permitAll()
		.anyRequest().hasAnyRole("ADMIN");
		
		http.cors().configurationSource(corsConfigurationSource());
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		String[] origins = corsOrigins.split(",");

	    CorsConfiguration corsConfig = new CorsConfiguration();
	    corsConfig.setAllowedOriginPatterns(Arrays.asList(origins));
	    corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
	    corsConfig.setAllowCredentials(true);
	    corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
	 
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", corsConfig);
	    return source;
	}
	 
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
	    FilterRegistrationBean<CorsFilter> bean
	            = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
	    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
	    return bean;
	}
}