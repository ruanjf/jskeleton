package com.rongji.jskeleton.core.event;

import com.rongji.jskeleton.core.eventListen.EventListen;


public class ArticleService {
	
	private static ArticleSaveEvent articleSaveEvent = new ArticleSaveEvent();
	
	public ArticleService() {
		EventListen.getInstance().register(articleSaveEvent);
	}

	public void doSomething() {
		System.out.println("do...");
		EventListen.getInstance().trigger(articleSaveEvent, false, this);
	}

}
