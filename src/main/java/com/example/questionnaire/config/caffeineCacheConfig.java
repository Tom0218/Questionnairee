package com.example.questionnaire.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@EnableCaching //Spring framework 中用來驅動緩存的註解
				//容器內至少要有一個cacheManager的Bean
@Configuration //配置成設定黨，並交由Spring Boot 託管
public class caffeineCacheConfig {
	
	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager();
		cacheManager.setCaffeine(Caffeine.newBuilder()
				//設定時間:1.最後一次寫入或 2.訪問後就過固定一段時間
				.expireAfterAccess(600, TimeUnit.SECONDS)
				//初始的緩存空間大小
				.initialCapacity(100)
				//緩存的最大筆數
				.maximumSize(500));
		return cacheManager;
	}
	

}
