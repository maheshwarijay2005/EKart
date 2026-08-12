package com.jaykabra.jaykart;

import com.jaykabra.jaykart.dto.ContactInfoDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@EnableConfigurationProperties(value = {ContactInfoDto.class})
public class JayKartApplication {

    public static void main(String[] args) {
        SpringApplication.run(JayKartApplication.class, args);
    }

}
