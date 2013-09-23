package com.rongji.jskeleton.core.module;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rongji.jskeleton.core.tools.ToolService;


@RunWith(JUnit4.class)
public class ModuleConfigurationTest {

	/**
	 * 通常的测试
	 */
	@Test
	public void general() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(ModuleConfiguration.class);
		SpringContextHolder sch = ctx.getBean(SpringContextHolder.class);
		
		print(sch);
		
		System.out.println(ctx.getBean(DeptService.class).getContent());
		System.out.println(ctx.getBean(ToolService.class).getContent());
		System.out.println(ctx.getBean(ModuleService.class).getPropTest());
		
	}

	/**
	 * 未获取bean时取值
	 */
	@Test
	public void staticFunc() {
		new AnnotationConfigApplicationContext(ModuleConfiguration.class);
		print();
	}
	
	/**
	 * xml获取bean
	 */
	@Test
	public void xml() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/system-test-config.xml");
		SpringContextHolder sch = ctx.getBean(SpringContextHolder.class);
		print(sch);
		print();
		System.out.println(ctx.getBean(DeptService.class).getContent());
		System.out.println(ctx.getBean(ToolService.class).getContent());
		System.out.println(ctx.getBean(ModuleService.class).getPropTest());
	}
	
	@Test
	public void prop() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ModuleConfiguration.class);
		System.out.println(ctx.getBean(ModuleService.class).getPropTest());
	}
	
	private void print() {
		System.out.println(SpringContextHolder.getApplicationContext());
	}
	
	private void print(SpringContextHolder sch) {
		Assert.assertNotNull(sch);
		System.out.println(sch.getApplicationContext());
	}
	
}
