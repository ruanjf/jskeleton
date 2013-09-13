package com.rongji.jskeleton.core.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.rongji.jskeleton.core.eventListen.Event;
import com.rongji.jskeleton.core.eventListen.EventListen;
import com.rongji.jskeleton.core.eventListen.Listen;
import com.rongji.jskeleton.core.eventListen.Listener;

@RunWith(JUnit4.class)
public class ArticleSaveEventListenTest {
	
	private EventListen el = EventListen.getInstance();
	private Random random = new Random();

	@Test
	public void test() {
		
		List<Runnable> list = new ArrayList<Runnable>();
		
		for (int i = 0; i < 10; i++) {
			list.add(new Runnable() {
				@Override
				public void run() {
					if (random.nextInt(10)/2 == 0) {
						el.trigger("saveArticle", false);
					} else {
						el.trigger("saveInfoArticle", false);
					}
				}
			});
		}
		
//		try {
//			Assert.assertConcurrent("", list, 100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		System.out.println("start");
		el.trigger("saveArticle", false);
		System.out.println("end");
		
	}
	
	@Before
	public void before() {
		
		el.register(new SaveArticleEvent("saveArticle", "保存文章"));
		el.register(new SaveInfoArticleEvent("saveInfoArticle", "保存信息公开文章"));
		
		
		
		Listener listener2 = new Listener("msg", "留言模块");
		Listen<SaveArticleEvent> li = new Listen<SaveArticleEvent>("saveArticle") {
			@Override
			public boolean on(Object... args) {
				System.out.println(getEvent().cover(args));
				done(this);
				return false;
			}
		};
		li.setOrder(1);
		listener2.addListen(li);
		
		Listener listener1 = new Listener("log", "日志模块");
		listener1.addListen(new Listen<SaveArticleEvent>("saveArticle") {
			@Override
			public boolean on(Object... args) {
				System.out.println(getEvent().cover(args));
				done(this);
				return false;
			}
		});
		listener1.addListen(new Listen<SaveInfoArticleEvent>("saveInfoArticle") {
			@Override
			public boolean on(Object... args) {
				System.out.println(getEvent().coverxxxxx(args));
				done(this);
				return false;
			}
		});
		
		
	}
	
	private void done(@SuppressWarnings("rawtypes") Listen listen) {
		System.out.println(listen.getListener().getName() + "    " + listen.getEvent().getName());
		try {
			Thread.sleep(5000);
//			Thread.sleep(random.nextInt(30000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

class SaveArticleEvent extends Event {

	public SaveArticleEvent(String id, String name) {
		super(id, name);
	}
	
	public String cover(Object... args) {
		return this.getClass().getSimpleName();
	}
	
}

class SaveInfoArticleEvent extends Event {
	
	public SaveInfoArticleEvent(String id, String name) {
		super(id, name);
	}
	
	public String coverxxxxx(Object... args) {
		return this.getClass().getSimpleName();
	}
	
}

