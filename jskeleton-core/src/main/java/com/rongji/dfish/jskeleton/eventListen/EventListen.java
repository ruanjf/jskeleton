package com.rongji.dfish.jskeleton.eventListen;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 事件监听机制
 * 提供事件的添加、触发、移除，监听者添加、通知、移除。
 * 
 * @author rjf
 *
 */
public final class EventListen {
	
	private Map<Event, EventObservable> observables = new ConcurrentHashMap<Event, EventObservable>();
	private Map<Listener, EventObserver> observers = new ConcurrentHashMap<Listener, EventObserver>();
	
	/**
	 * 监听器的所以监听事件
	 */
	private Map<String, Event> events = new ConcurrentHashMap<String, Event>();
	private Map<Event, List<Listen<? extends Event>>> listens = new ConcurrentHashMap<Event, List<Listen<? extends Event>>>();
	
	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	private EventListen(){};
	
	private static EventListen eventListen;
	
	public static EventListen getInstance() {
		if (eventListen == null) {
			synchronized (EventListen.class) {
				if (eventListen == null) {
					eventListen = new EventListen();
				}
			}
		}
		return eventListen;
	}
	
	/**
	 * 注册事件
	 * @param event 事件
	 * @return 添加成功返回true，否则返回false;
	 */
	public boolean register(Event event) {
		if ( event != null && event.getId() != null
				&& !event.getId().isEmpty() && !events.containsKey(event)) {
			events.put(event.getId(), event);
			observables.put(event, new EventObservable(event));
			return true;
			
		}
		return false;
	}
	
	/**
	 * 根据事件标识获取事件
	 * @param eventId 事件标识
	 * @param clazz 事件类型
	 * @return 不存在时返回null
	 */
	@SuppressWarnings("unchecked")
	public <T extends Event> T getEvent(String eventId, Class<T> clazz) {
		if ( eventId != null && ! eventId.isEmpty() && events.get(eventId) != null ) {
//			if ( clazz != null ) {
//				if ( events.get(eventId).getClass() == clazz ) {
//					return (T) events.get(eventId);
//				}
//			} else {
				return (T) events.get(eventId);
//			}
		}
		return null;
	}
	
	/**
	 * See {@link #getEvent(String, Class)}
	 * @param eventId
	 * @return
	 */
	public <T extends Event> T getEvent(String eventId) {
		return getEvent(eventId, null);
	}
	
	/**
	 * 获得所有以加入监听的事件
	 * @return 返回不可修改的事件集合
	 */
	public Collection<Event> getEvents() {
		return Collections.unmodifiableCollection(events.values());
	}
	
	/**
	 * 触发事件，支持异步操作
	 * 若设置同步则还使用到了{@link Event#getTimeout()}的最大等待时间
	 * 
	 * @param eventId 事件ID
	 * @param asynchronous 是否异步通知，异步用于结果不受监听影响的时候
	 * @param args 传递给监听的参数
	 * 
	 * @see EventListen#trigger(Event, boolean, Object...)
	 */
	public <T extends Event> void trigger(String eventId, boolean asynchronous, Object... args) {
		if (eventId != null && ! eventId.isEmpty()) {
			trigger(getEvent(eventId, null), asynchronous, args);
		}
	}
	
	/**
	 * 触发事件，支持异步操作。
	 * 若设置同步则还使用到了{@link Event#getTimeout()}的最大等待时间
	 * 
	 * @param event 事件
	 * @param asynchronous 是否异步通知，异步用于结果不受监听影响的时候
	 * @param args 传递给监听的参数
	 */
	public <T extends Event> void trigger(Event event, boolean asynchronous, Object... args) {
		if ( event != null && observables.containsKey(event)) {
//			System.out.println(event.getName()+ " 事件触发");
			observables.get(event).happen(asynchronous, args);
//			System.out.println(event.getName()+ " 事件结束");
		}
	}

	/**
	 * 向监听器中添加指定的监听
	 * @param listen 监听
	 * @return 添加成功返回true，否则返回false;
	 */	
	protected boolean addListen(Listen<? extends Event> listen) {
		if ( listen != null && listen.getEvent() != null&& listen.getListener() != null 
				&& ! observables.containsValue(listen.getEvent()) ) {
			
			addListener(listen.getListener());
			
			Event event = listen.getEvent();
			if ( observers.containsKey(listen.getListener()) ) {
				if ( ! listens.containsKey(event) ) {
					listens.put(event, Collections.synchronizedList(new LinkedList<Listen<? extends Event>>()));
				}
				
				synchronized (listens.get(event)) {
					listens.get(event).add(listen);
					Collections.sort(listens.get(event), new Comparator<Listen<? extends Event>>() {
						@Override
						public int compare(Listen<? extends Event> o1,
								Listen<? extends Event> o2) {
							return o1.getOrder() - o2.getOrder();
						}
					});
					observables.get(event).deleteObservers();
					for (Listen<? extends Event> l : listens.get(event)) {
						observables.get(event).addObserver( observers.get(l.getListener()) );
					}
				}
				
				return true;
			}
			
		}
		return false;
	}
	
	/**
	 * 向监听器中删除指定的监听
	 * @param listen 监听
	 * @return 添加成功返回true，否则返回false;
	 */
	protected boolean deleteListen(Listen<? extends Event> listen) {
		if ( listen != null && listen.getEvent() != null && listen.getListener() != null
				&& observables.containsValue(listen.getEvent())
				&& observers.containsKey(listen.getListener()) ) {
			
			observables.get(listen.getEvent()).deleteObserver( observers.get(listen.getListener()) );
			listens.remove(listen.getEvent());
			return true;
			
		}
		return false;
	}
	
	/**
	 * 向监听器中添加监听者
	 * @param listener 监听者
	 * @return 添加成功返回true，否则返回false;
	 */
	private boolean addListener(Listener listener) {
		if ( listener != null && ! observers.containsKey(listener) ) {
			observers.put(listener, new EventObserver(listener));
			return true;
		}
		return false;
	}
	
	/**
	 * 被可观察的事件
	 * @author rjf
	 *
	 */
	private class EventObservable extends Observable {
		
		private Event event;
		
		public EventObservable(Event event){
			this.event = event;
		}
		
		public void happen(boolean asynchronous, final Object... args) {
			Callable<Boolean> callable = new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					boolean flag = false;
					try {							
//						eventNotify(args);
						setChanged();
						notifyObservers( new Object[]{ event, args} );
						flag = true;
					} catch (Throwable e) {
						System.out.println(e);
					}
					return flag;
				}
			};
			if ( asynchronous ) {
				executorService.submit(callable);
				
			} else {
				FutureTask<Boolean> future = new FutureTask<Boolean>(callable);
				long ct = System.currentTimeMillis();
//				eventNotify(args);
				try {
					future.get(event.getTimeout(), TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					System.out.println("超过事件触发的最大等待时间："+event.getTimeout());
					e.printStackTrace();
				}
				System.out.println("同步的监听[" + event.getName() + "]触发，总耗时[" + (System.currentTimeMillis()-ct) + "]毫秒");
				
			}
		}
		
//		private void eventNotify(Object... args) {
//			setChanged();
//			notifyObservers( new Object[]{ event, args} );
//		}
		
	}
	
	/**
	 * 事件观察者
	 * @author rjf
	 *
	 */
	private class EventObserver implements Observer {
		
		private Listener listener;
		
		public EventObserver(Listener listener){
			this.listener = listener;
		}

		@Override
		public void update(Observable o, Object arg) {
			Object[] args = (Object[]) arg;
			Event event = (Event) args[0];
			Listen<? extends Event> listen = listener.getListen(event);
			if (listen != null && listen.getEvent() == event) {
				long ct = System.currentTimeMillis();
				listen.on( (Object[])args[1] );
				System.out.println("[" + listener.getName() + "]监听[" + event.getName() + "]，调用[" + listen.getClass().getName() + "#on(Object...)]方法耗时[" + (System.currentTimeMillis()-ct) + "]毫秒");
			}
		}
		
	}

}
