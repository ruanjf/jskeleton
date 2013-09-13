package com.rongji.jskeleton.core.eventListen;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class Listener {
	
	private final String id;
	private final String name;
	private final Map<Event, Listen<? extends Event>> listens = new ConcurrentHashMap<Event, Listen<? extends Event>>();
	
	public Listener(String id, String name){
		if (id == null || id.isEmpty()) {
			id = "Listener-"+System.currentTimeMillis();
		}
		if (name == null || name.isEmpty()) {
			name = id;
		}
		this.id = id;
		this.name = name;
	}
	
	/**
	 * 监听者标识
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 监听者名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 根据事件获取监听（只读）
	 * @param event 事件
	 * @return
	 */
	public Listen<? extends Event> getListen(Event event) {
		return listens.get(event);
	}
	
	/**
	 * 添加监听
	 * 注：本方法之后设置优先级{@link Listen#setOrder(int)}是无效果的
	 * @param listen
	 * @return 监听是否添加成功
	 */
	public boolean addListen(Listen<? extends Event> listen) {
		if ( listen != null && listen.getEvent() != null
				&& ! listens.containsKey(listen.getEvent()) ) {
			listen.setListener(this);
			listens.put(listen.getEvent(), listen);
			if ( ! EventListen.getInstance().addListen(listen) ) {
				deleteListen(listen);
			} else {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 删除监听
	 * @param listen
	 */
	public void deleteListen(Listen<? extends Event> listen) {
		if ( listen != null && listen.getEvent() != null
				&& listens.containsKey(listen.getEvent()) ) {
			EventListen.getInstance().deleteListen(listen);
			listens.remove(listen.getEvent());
		}
	}
	
	/**
	 * 获得所有监听（只读）
	 * @return
	 */
	public Map<Event, Listen<? extends Event>> getListens() {
		return Collections.unmodifiableMap(listens);
	}
	

	@Override
	public int hashCode() {
		int result = 17;
		result = result * 31 + id.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if ( obj instanceof Listener ) {
			Listener listener = (Listener) obj;
			return this.id.equals(listener.id);
		}
		return false;
	}
	
}
