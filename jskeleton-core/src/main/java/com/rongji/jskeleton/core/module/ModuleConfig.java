package com.rongji.jskeleton.core.module;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * 基本思路是采用注解避免配置来实现模块的管理
 * 
 * 注基于的版本为spring3.2.4
 * 
 * 启用模块化支持需设置如下两种方式中一种：
 * <ul>
 * <li>xml加载方式（经典的xml启动spring的application.xml，放于配置文件顶部）</li>
 * <pre>{@code
 * <import resource="classpath:/com/rongji/jskeleton/core/module/module.xml"/>
 * }</pre>
 * 
 * <li>全注解配置方式</li>
 * <pre>{@code
 * ApplicationContext ctx = new AnnotationConfigApplicationContext(ModuleConfig.class);
 * }</pre>
 * </ul>
 * 
 * 
 * @author rjf
 * @since 0.1 2013-09-14
 *
 */
@Configuration // 标记为配置信息
//@PropertySource({"classpath:/com/rongji/jskeleton/core/module/module.properties"}) // 读取配置文件
@ComponentScan(basePackages="*.*", scopedProxy=ScopedProxyMode.INTERFACES) // 标记注解范围
public class ModuleConfig {

	/**
	 * 支持properties配置文件读取，注意配置文件为UTF-8格式。
	 * 自定义配置文件地址为classpath:module.properties
	 * @return
	 */
	@Bean // 标记为bean
	@Lazy(false) // 直接初始化
	@Scope("singleton") // 作用范围唯一实例
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		ppc.setFileEncoding("UTF-8");
		
		List<Resource> locations = new ArrayList<Resource>();
		locations.add(new ClassPathResource("/com/rongji/jskeleton/core/module/module.properties"));
		
		// 自定义配置文件
		ClassPathResource cpr = new ClassPathResource("/module.properties");
		if (cpr.exists()) {
			locations.add(cpr);
		}
		FileSystemResource fsr = new FileSystemResource(new ModuleConfig().getClass().getResource("/").getPath()+"../module.properties");
		if (fsr.exists()) {
			locations.add(fsr);
		}
		
		ppc.setLocations(locations.toArray(new Resource[]{}));
		
		return ppc;
	}
	
	/**
	 * 获取spring配置的上下文，可用于今天方法
	 * @return 返回spring上下文
	 */
	@Bean // 标记为bean
	@Lazy(false) // 直接初始化
	@Scope("singleton") // 作用范围唯一实例
	public SpringContextHolder springContextHolder() {
		return new SpringContextHolder();
	}
	
}
