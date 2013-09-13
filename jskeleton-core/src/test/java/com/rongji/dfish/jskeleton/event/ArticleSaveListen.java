package com.rongji.dfish.jskeleton.event;

import com.rongji.dfish.jskeleton.eventListen.Listen;

public class ArticleSaveListen extends Listen<ArticleSaveEvent> {

	public ArticleSaveListen(String eventId) {
		super(eventId);
	}

	@Override
	public boolean on(Object... args) {
		System.out.println(getEvent().getName() + " 收到");
		getEvent().sayHello();
		return false;
	}

}
