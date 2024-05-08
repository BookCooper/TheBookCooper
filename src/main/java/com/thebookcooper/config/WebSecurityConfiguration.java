/*import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults())  // Apply CORS using default settings
            .authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
            .httpBasic(withDefaults())
            .oauth2ResourceServer().jwt();
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("*"); // Allow all origins
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setMaxAge(3600L); // Set max age for pre-flight requests

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}




/*package com.thebookcooper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz.anyRequest().authenticated()).httpBasic(withDefaults());
        http.oauth2ResourceServer().jwt();
        return http.build();
    }
}*/

package com.thebookcooper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.net.InetAddress;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and() // Enable CORS and add configuration
            .authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
            .httpBasic(withDefaults())
            .oauth2ResourceServer().jwt();
        
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        String[] defaultOrigins = {"http://localhost:3000"};
        try {
            String serverHostName = InetAddress.getLocalHost().getHostName();
            config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://" + serverHostName + ":3000"));
        } catch (Exception e) {
            System.err.println("Error determining local host name, using default origins");
            config.setAllowedOrigins(Arrays.asList(defaultOrigins));
        }

        //config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://20.232.78.162:3000/")); // Specify the exact origin
        config.setAllowedMethods(Arrays.asList("OPTIONS", "HEAD", "GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList(
            "X-Requested-With", "X-Auth-Token", "Content-Type", "Content-Length",
            "Authorization", "Access-Control-Allow-Headers", "Accept",
            "Access-Control-Allow-Methods", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply CORS configuration to all paths
        
        return new CorsFilter(source);
    }
}


