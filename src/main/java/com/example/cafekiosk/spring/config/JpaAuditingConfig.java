package com.example.cafekiosk.spring.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configurable
@EnableJpaAuditing
public class JpaAuditingConfig {
}
