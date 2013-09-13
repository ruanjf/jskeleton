package com.rongji.dfish.jskeleton.event;

import com.rongji.dfish.jskeleton.eventListen.EventListen;


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
