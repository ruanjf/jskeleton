package com.rongji.jskeleton.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rongji.jskeleton.core.module.ModuleConfiguration;
import com.rongji.jskeleton.core.module.SpringContextHolder;
import com.runjf.part1.service.UserService;

@RunWith(JUnit4.class)
public class ApplicationContextTest {

	/**
	 * 通常的测试
	 */
	@Test
	public void general() {
		System.out.println("general");
		ApplicationContext ctx = new AnnotationConfigApplicationContext(ModuleConfiguration.class);
		
		UserService userService = ctx.getBean(UserService.class);
		System.out.println(userService.getUser("0001").getUsername());
		
	}
	
	@Test
	public void xml() {
		System.out.println("xml");
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/system-test-config.xml");
		
		System.out.println(SpringContextHolder.getApplicationContext());
//		UserService userService = ctx.getBean(UserService.class);
//		System.out.println(userService.getUser("0001").getUsername());
		System.out.println(ctx.getBean("handlerMapping"));
		
	}
	
	@Test
	public void dept() {
		System.out.println("dept");
		System.out.println(getClass().getResource("/"));
		ApplicationContext ctx = new AnnotationConfigApplicationContext(ModuleConfiguration.class);
		
		DeptService service = ctx.getBean(DeptService.class);
		System.out.println(service.getContent());
		ctx.getBean("dept", DeptService.class);
		
		System.out.println(ctx.getBean("handlerMapping"));
		System.out.println(ctx.getBean("springContextHolder"));
		
	}

		
}
