package com.rongji.jskeleton.core.eventListen;

/**
 * 事件的基础信息
 * 具体的事件需继承本类，由事件提供者定义
 * 
 * 建议实现具体的事件时，提供一个或者多个的转换方法。方便于参数的传递
 * 
 * @author rjf
 *
 */
public abstract class Event {
	
	private final String id;
	private final String name;
	
	private long timeout = 1000;
	
	/**
	 * @param id 事件标识
	 * @param name 事件名称
	 */
	public Event(String id, String name) {
		if (id == null || id.isEmpty()) {
			id = "Event-"+System.currentTimeMillis();
		}
		if (name == null || name.isEmpty()) {
			name = id;
		}
		this.id = id;
		this.name = name;
	}

	/**
	 * 事件标识
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 事件名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取同步触发事件时的最大等待时间，单位毫秒。
	 * 若未设置默认等待时间为1000毫秒
	 * @return
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * 设置同步触发事件时的最大等待时间
	 * @param timeout 等待时间，单位毫秒
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	@Override
	public final int hashCode() {
		int result = 17;
		result = result * 31 + id.hashCode();
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if ( obj instanceof Event ) {
			Event event = (Event) obj;
			return this.id.equals(event.id);
		}
		return false;
	}
	
}
