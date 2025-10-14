package com.yarmook.realstate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("com.yarmook.realstate.repository")
@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class DatabaseConfiguration {}
