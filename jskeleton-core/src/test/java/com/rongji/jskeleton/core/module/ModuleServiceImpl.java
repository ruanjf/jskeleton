package com.rongji.jskeleton.core.module;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ModuleServiceImpl implements ModuleService {
	
	private @Value("${test}") String test;
	
	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	@Override
	public String getPropTest() {
		return test;
	}

	@Override
	public String getProp(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
