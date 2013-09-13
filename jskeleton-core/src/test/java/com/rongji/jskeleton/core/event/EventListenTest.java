package com.rongji.jskeleton.core.event;

import static com.rongji.jskeleton.core.tools.Assert.assertConcurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.rongji.jskeleton.core.eventListen.Event;
import com.rongji.jskeleton.core.eventListen.EventListen;
import com.rongji.jskeleton.core.eventListen.Listen;
import com.rongji.jskeleton.core.eventListen.Listener;

@RunWith(JUnit4.class)
public class EventListenTest {

	@Test
	public void test() {
		
		final int eventCount = 100;
		int listenerCount = 100; 
		int runCount = 500; // 相当于同时操作用户
		final Random random = new Random();
		
		List<Runnable> runs = new ArrayList<Runnable>();
		
		final List<Event> events = new ArrayList<Event>();
		List<Listener> listeners = new ArrayList<Listener>();
		
		final EventListen el = EventListen.getInstance();
		
		for (int i = 1; i <= eventCount; i++) {
			
			TestEvent event = new TestEvent("event-"+i, "事件-"+i );
			el.register( event );
			events.add(event);
			
		}
		
		for (int i = 1; i <= listenerCount; i++) {
			
			Listener listener = new Listener("listener-"+i, "监听者-"+i);
			listeners.add(listener);
			
		}
		
		
		int i = 1000;
		while (i > 0) {
			i--;

			String eventId = "event-" + random.nextInt(eventCount);
			
			Listen<TestEvent> listen = new Listen<TestEvent>( eventId ) {
				@Override
				public boolean on(Object... args) {
					
					int t = random.nextInt(3000);
//					System.out.println(getListener().getName() + " now listened " + getEvent().getName() + " run " + t);
					
					long l = System.currentTimeMillis();
					while ( System.currentTimeMillis() - l < t ) {
						;
					}
					
//					try {
//						Thread.sleep(t);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					
					return false;
				}
			};
			
			Listener listener = listeners.get( random.nextInt(listenerCount) );
			listener.addListen(listen);
			
		}
		
		for (int j = 0; j < runCount; j++) {
			Runnable run = new Runnable() {
				@Override
				public void run() {
					Event event = events.get( random.nextInt(eventCount) );
					if (event != null) {
						el.trigger(event, false);
					}
				}
			};
			runs.add(run);
		}
		
		
		try {
			assertConcurrent("hello", runs, 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}


}