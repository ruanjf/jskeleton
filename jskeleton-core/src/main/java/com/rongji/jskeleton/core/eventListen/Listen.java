package com.rongji.jskeleton.core.eventListen;

import com.rongji.jskeleton.core.tools.GenericsUtils;

/**
 * 监听的基础信息
 * 具体的监听需继承本类，需指明监听的事件。
 * 
 * @author rjf
 *
 * @param <T> 监听的事件
 */
public abstract class Listen<T extends Event> {
	
	private int order = 0;
	private final T event;
	private Listener listener;
	
	/**
	 * 创建监听
	 * @param eventId 事件标识
	 */
	public Listen(String eventId) {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) GenericsUtils.getSuperClassGenricType(getClass());
		this.event = EventListen.getInstance().getEvent(eventId, clazz);
	}
	
	/**
	 * 获得监听事件
	 * @return 返回{@link Event}
	 */
	public T getEvent() {
		return event;
	}

	/**
	 * 获得监听者 
	 * @return
	 */
	public Listener getListener() {
		return listener;
	}

	/**
	 * 获得优先级
	 * @return
	 */
	public int getOrder() {
		return order;
	}


	/**
	 * 设置优先级
	 * 注：在{@link Listener#addListen(Listen)}之后设置优先级是无效果的
	 * @param order 越大则越早执行，最小值为0
	 * @return 返回当前实例
	 */
	public Listen<T> setOrder(int order) {
		this.order = order > 0 ? order : 0;
		return this;
	}

	/**
	 * 执行动作
	 * 其中监听者可通过{@link #getListener()}获取，监听事件可通过{@link #getEvent()}获取
	 * @param args 传递的参数参数，建议在事件中{@link Event}提供一个转换的方法以便方便调用这的使用
	 * @return
	 */
	public abstract boolean on(Object... args);
	
	/**
	 * 设置监听者
	 * @param listener 监听者
	 */
	protected final void setListener(Listener listener) {
		
		if (listener != null && this.listener != listener) {
			if ( this.listener != null ) {
				this.listener.deleteListen(this);
				this.listener = listener;
				this.listener.addListen(this);
			} else {
				this.listener = listener;
			}
		}
		
	}

	@Override
	public final int hashCode() {
		int result = 17;
		result = result * 31 + event.hashCode();
		result = result * 31 + listener.hashCode();
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof Listen) {
			Listen<?> listen = (Listen<?>) obj;
			return event.equals(listen.getEvent()) && listener.equals(listen.getListener());
		}
		return false;
	}
	
	
	
}

