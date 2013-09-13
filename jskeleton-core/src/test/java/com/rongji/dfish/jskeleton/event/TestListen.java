package com.rongji.dfish.jskeleton.event;

import com.rongji.dfish.jskeleton.eventListen.Listen;

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
