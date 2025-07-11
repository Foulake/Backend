package com.team.dev.config;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
//@EnableWebMvc
public class CorsConfig{


    /*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //configuration.addAllowedOrigin("*");
        configuration.applyPermitDefaultValues();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8182"));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        //configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/

    /*{

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8182"));
            configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
            configuration.addAllowedHeader("*");
            //configuration.setAllowCredentials(false);
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }*/
    /*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
*/
    /*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/

    @Bean
    public FilterRegistrationBean filterFilterRegistrationBean() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ALLOW,
                HttpHeaders.ACCEPT
        ));
        config.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.PATCH.name()
        ));
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(-102);

        return bean;
    }

    /*implements WebMvcConfigurer{

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600L)
                .allowCredentials(true);
    }*/

   /* implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false);
    }*/


    /*
    private final long MAX_AGE_SEC = 3600;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer(){

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods(GET, PUT, POST, DELETE)
                        .allowedHeaders("*")
                        .allowedOriginPatterns("*")
                        .allowCredentials(true)
                        .maxAge(MAX_AGE_SEC);
            }
        };
    }*/

    /*@Bean
    static FilterRegistrationBean<Filter> handlerMappingIntrospectorCacheFilter(HandlerMappingIntrospector hmi) {
        Filter cacheFilter = hmi.createCacheFilter();
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>(cacheFilter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return registrationBean;
    }*/

   /* @Bean
    CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }*/

   /* @Bean
    CorsWebFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        // Possibly...
        // config.applyPermitDefaultValues()

        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://domain1.com");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }*/

    /*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
       // configuration.setAllowCredentials(false);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/

}
