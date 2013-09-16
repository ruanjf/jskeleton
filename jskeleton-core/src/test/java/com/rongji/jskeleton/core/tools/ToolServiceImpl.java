package com.rongji.jskeleton.core.tools;

import org.springframework.stereotype.Service;

@Service
public class ToolServiceImpl implements ToolService {

	@Override
	public String getContent() {
		return "ok!!!!Tool";
	}

}
