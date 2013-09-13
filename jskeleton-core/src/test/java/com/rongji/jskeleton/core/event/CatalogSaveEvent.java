package com.rongji.jskeleton.core.event;

import com.rongji.jskeleton.core.eventListen.Event;

public class CatalogSaveEvent extends Event {
	
	public CatalogSaveEvent() {
		this("articleSave-new", "栏目保存");
	}

	public CatalogSaveEvent(String id, String name) {
		super(id, name);
	}
	
	public void sayHello() {
		System.out.println("Hello");
	}
	
}