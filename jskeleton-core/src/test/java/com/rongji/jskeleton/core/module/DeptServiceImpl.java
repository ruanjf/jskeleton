package com.rongji.jskeleton.core.module;

import org.springframework.stereotype.Component;

@Component("dept")
public class DeptServiceImpl implements DeptService {

	@Override
	public String getContent() {
		return "ok!!!!";
	}

}
