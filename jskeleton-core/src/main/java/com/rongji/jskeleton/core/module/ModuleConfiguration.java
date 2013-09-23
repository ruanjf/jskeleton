package com.rongji.jskeleton.core.module;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.reactor.ReactorAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * 基本思路是采用注解避免配置来实现模块的管理<br/>
 * 
 * 注基于的版本为spring3.2.4<br/>
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
 * ApplicationContext ctx = new AnnotationConfigApplicationContext(ModuleConfiguration.class);
 * }</pre>
 * </ul>
 * 
 * <li>Web全注解配置方式（web.xml中配置）</li>
 * <pre>{@code
 *	<!-- Configure ContextLoaderListener to use AnnotationConfigWebApplicationContext 
 *		instead of the default XmlWebApplicationContext -->
 *	<context-param>
 *		<param-name>contextClass</param-name>
 *		<param-value>
 *			org.springframework.web.context.support.AnnotationConfigWebApplicationContext
 *		</param-value>
 *	</context-param>
 *
 *	<!-- Configuration locations must consist of one or more comma- or space-delimited 
 *		fully-qualified @Configuration classes. Fully-qualified packages may also 
 *		be specified for component-scanning -->
 *	<context-param>
 *		<param-name>contextConfigLocation</param-name>
 *		<param-value>com.rongji.gateway.init.AppConfiguration</param-value>
 *	</context-param>
 *
 *	<!-- Bootstrap the root application context as usual using ContextLoaderListener -->
 *	<listener>
 *		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
 *	</listener>
 * }</pre>
 * </ul>
 * 
 * @author rjf
 * @since 0.1 2013-09-14
 *
 */
@Configuration // 标记为配置信息
@EnableAutoConfiguration(exclude = {
		MessageSourceAutoConfiguration.class,
//		PropertyPlaceholderAutoConfiguration.class,
		BatchAutoConfiguration.class,
		JpaRepositoriesAutoConfiguration.class,
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,
		ReactorAutoConfiguration.class,
		ThymeleafAutoConfiguration.class,
		EmbeddedServletContainerAutoConfiguration.class,
		ServerPropertiesAutoConfiguration.class,
		MultipartAutoConfiguration.class,
		WebMvcAutoConfiguration.class,
		WebSocketAutoConfiguration.class })
@ComponentScan({"com.rongji.jskeleton.core"})
public class ModuleConfiguration {

	/**
	 * 支持properties配置文件读取，注意配置文件为UTF-8格式。
	 * 自定义配置文件地址为classpath:module.properties
	 * @return
	 */
	@Bean // 标记为bean
	@Lazy(false) // 直接初始化
	@Scope("singleton") // 作用范围唯一实例
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		System.out.println("init PropertyPlaceholderConfigurer");
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		ppc.setFileEncoding("UTF-8");
		
		List<Resource> locations = new ArrayList<Resource>();
		locations.add(new ClassPathResource("/com/rongji/jskeleton/core/module/module.properties"));
		
		// 自定义配置文件
		ClassPathResource cpr = new ClassPathResource("/module.properties");
		if (cpr.exists()) {
			locations.add(cpr);
		}
		FileSystemResource fsr = new FileSystemResource(new ModuleConfiguration().getClass().getResource("/").getPath()+"../module.properties");
		if (fsr.exists()) {
			locations.add(fsr);
		}
		
		ppc.setLocations(locations.toArray(new Resource[locations.size()]));
		
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
		System.out.println("init SpringContextHolder");
		return new SpringContextHolder();
	}
	
}
