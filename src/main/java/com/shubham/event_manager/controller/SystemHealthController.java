package com.shubham.event_manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "System",
        description = "System health and version info")
public class SystemHealthController {

    private final JdbcTemplate jdbcTemplate;
    private final RedisTemplate<String, Object>
            redisTemplate;

    @GetMapping("/health")
    @Operation(
            summary = "Custom system health check",
            description = "Checks MySQL, Redis, " +
                    "and application status"
    )
    public ResponseEntity<Map<String, Object>>
    health() {

        Map<String, Object> health = new HashMap<>();
        health.put("timestamp",
                LocalDateTime.now().toString());
        health.put("application", "UP");

        // Check MySQL
        try {
            jdbcTemplate.queryForObject(
                    "SELECT 1", Integer.class);
            health.put("mysql", "UP");
        } catch (Exception e) {
            health.put("mysql", "DOWN");
            health.put("mysqlError", e.getMessage());
            log.error("MySQL health check failed: {}",
                    e.getMessage());
        }

        // Check Redis
        try {
            redisTemplate.getConnectionFactory()
                    .getConnection().ping();
            health.put("redis", "UP");
        } catch (Exception e) {
            health.put("redis", "DOWN");
            health.put("redisError", e.getMessage());
            log.error("Redis health check failed: {}",
                    e.getMessage());
        }

        // Overall status
        boolean allUp =
                "UP".equals(health.get("mysql"))
                        && "UP".equals(health.get("redis"));

        health.put("status", allUp ? "UP" : "DEGRADED");

        return ResponseEntity.ok(health);
    }

    @GetMapping("/version")
    @Operation(summary = "Get application version info")
    public ResponseEntity<Map<String, String>>
    version() {
        return ResponseEntity.ok(Map.of(
                "application", "Event Management System",
                "version", "4.0.0",
                "phase", "Phase 4 — Security and Features",
                "java", System.getProperty("java.version"),
                "springBoot", "3.4.1"
        ));
    }
}