package com.rongji.jskeleton.core.module;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.properties")
public class AppConfiguration {

	@Bean
	@ConditionalOnMissingBean(DeptService.class)
	public DeptService deptService(@Value("${app}") String app) {
		System.out.println("init DeptService "+app);
		return new DeptServiceImpl();
	}
	
}
