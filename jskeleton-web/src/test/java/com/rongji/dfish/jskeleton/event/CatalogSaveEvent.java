package com.rongji.dfish.jskeleton.event;

import com.rongji.dfish.jskeleton.eventListen.Event;

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