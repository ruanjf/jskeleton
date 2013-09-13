package com.rongji.jskeleton.core.event;

import java.util.ArrayList;
import java.util.List;

import com.rongji.jskeleton.core.eventListen.Event;

public class ArticleSaveEvent extends Event {
	
	public ArticleSaveEvent() {
		this("articleSave-new", "新闻模型文章保存");
	}

	public ArticleSaveEvent(String id, String name) {
		super(id, name);
	}
	
	public void sayHello() {
		System.out.println("Hello");
	}
	
	public List<String> format(Object... args) {
		List<String> list = new ArrayList<String>();
		for (Object obj : args) {
			list.add(obj+" format");
		}
		return list;
	}
	
}