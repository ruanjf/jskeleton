package com.rongji.jskeleton.core.event;

import com.rongji.jskeleton.core.eventListen.Listen;

public class TestListen extends Listen<TestEvent> {

	public TestListen(String eventId) {
		super(eventId);
	}

	@Override
	public boolean on(Object... args) {
		System.out.println(getEvent().getName() + " now do ");
		return true;
	}

}
