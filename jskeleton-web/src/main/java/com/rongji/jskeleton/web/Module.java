package com.rongji.jskeleton.web;

import org.apache.commons.fileupload.FileItemFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;

/**
 * 
 *
 * @author rjf
 * @since 0.1 2013-9-17
 *
 */
@Configuration
@ComponentScan({"com.rongji.jskeleton.web"})
public class Module {
	
	/**
	 * 定义默认的应设关系。bean 的name同URI，这样直接对应访问地址和处理类
	 * @return
	 */
	@Bean
	public BeanNameUrlHandlerMapping handlerMapping() {
		return new BeanNameUrlHandlerMapping();
	}
	
	/**
	 * 文件上传部分, 使用common upload。要保证环境中有apache的相关lib
	 * @return
	 */
	@Bean
	@ConditionalOnClass(FileItemFactory.class)
	public CommonsMultipartResolver multipartResolver(@Value("${web.maxUploadSize}") Long maxUploadSize) {
		
		CommonsMultipartResolver cmr = new CommonsMultipartResolver();
		cmr.setMaxUploadSize(maxUploadSize);
		
		return cmr;
	}
	
	/**
	 * 默认按act划分方法。如果那个处理类继承于 MultiActionController
	 * @return
	 */
	@Bean
	public ParameterMethodNameResolver paramResolver(@Value("${web.paramName}") String paramName,
			@Value("${web.defaultMethodName}") String defaultMethodName) {
		
		System.out.println(paramName);
		ParameterMethodNameResolver pmnr = new ParameterMethodNameResolver();
		pmnr.setParamName(paramName);
		pmnr.setDefaultMethodName(defaultMethodName);
		
		return pmnr;
	}

}
