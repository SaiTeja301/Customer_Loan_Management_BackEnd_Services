package com.SprCustomers.Config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CorsConfigTest {

    @Test
    void testAddCorsMappings() {
        CorsConfig corsConfig = new CorsConfig();
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);

        when(registry.addMapping("/**")).thenReturn(registration);
        when(registration.allowedOrigins(anyString(), anyString())).thenReturn(registration);
        when(registration.allowedMethods(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(registration);
        when(registration.allowedHeaders(anyString())).thenReturn(registration);
        when(registration.allowCredentials(true)).thenReturn(registration);
        when(registration.maxAge(3600)).thenReturn(registration);

        corsConfig.addCorsMappings(registry);

        verify(registry).addMapping("/**");
        verify(registration).allowedOrigins("http://localhost:4200", "http://localhost:3000");
        verify(registration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(registration).allowedHeaders("*");
        verify(registration).allowCredentials(true);
        verify(registration).maxAge(3600);
    }
}
