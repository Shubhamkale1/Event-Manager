package com.shubham.event_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
//@EnableJpaRepositories(basePackages = "com.shubham.event_manager.repository.jpa")
//@EnableElasticsearchRepositories(basePackages = "com.shubham.event_manager.repository.elasticsearch")
//@EnableRedisRepositories(basePackages = "com.shubham.event_manager.repository.redis") // dummy or empty
public class EventManagerApplication {

	public static void main(String[] args) {

		SpringApplication.run(EventManagerApplication.class, args);
	}

}
