package com.rongji.jskeleton.web.impl;

import org.springframework.stereotype.Component;

import com.rongji.jskeleton.web.DeptService;

@Component("dept")
public class DeptImpl implements DeptService {

	@Override
	public String getContent() {
		return "ok!!!!";
	}

}
