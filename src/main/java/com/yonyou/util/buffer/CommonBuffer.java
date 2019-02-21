package com.yonyou.util.buffer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * @author zzh
 * @version 创建时间：2017年3月8日
 * 类说明 
 */
public abstract class CommonBuffer<E> {
	
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 缓冲队列
	 */
	protected ConcurrentLinkedQueue<E> buf = new ConcurrentLinkedQueue<E>();
	/**
	 * 最大容量
	 */
	protected int capcity = 1024;
	/**
	 * 上次刷新时间
	 */
	protected long last_flush = System.currentTimeMillis();
	/**
	 * 最大未刷新时间
	 */
	protected long expire_interval = 1000 * 30 ;//生产环境设置为     1000 * 60 * 5 

	/**
	 * 指定的下次刷新时刻
	 */
	protected long expire_time = Long.MAX_VALUE/2;
	/**
	 * 刷新锁，防止并发刷新
	 */
	protected Lock flushLock = new ReentrantLock();
	
	/**
	 * 构造函数
	 */
	public CommonBuffer() {
		//绑定到刷新器
		FlushQueueThread.getSingleton().addCommonBuffer(this);
	}
	
	/**
	 * @return 缓冲器名称
	 */
	public String getName() {
		return this.getClass().getName();
	}

	/**
	 * 增加内容,刷新时依然可插入
	 * 
	 * @param e
	 */
	public void add(E e) {
		if(e == null){
			return ;
		}
		
		if(buf.size() >= capcity * 10){
			//容量过大
			logger.error("buf.clear() -> count:" + buf.size() + ", 缓冲区溢出, 请联系维护人员检查是否部署环境异常!");
			buf.clear();
		}
		//注释掉以下代码，杜绝在发起调用的业务线程中更新日志
//		if (buf.size() >= 0.1 * capcity) {
//			tryFlush();
//		}
		buf.add(e);
	}

	/**
	 * 尝试刷新
	 */
	public final void tryFlush() {
		// 防止并发刷新
		if (!flushLock.tryLock()) {
			return;
		}
		try {
			long startTime = System.currentTimeMillis();
			int oldSize = buf.size();
			flush(buf);
			int thisCount = oldSize-buf.size();
			if(thisCount > 0) {
				logger.info("flush " + thisCount + " records, cost " + (System.currentTimeMillis()-startTime) + " ms");
			}
		} finally {
			last_flush = System.currentTimeMillis();
			flushLock.unlock();
		}
	}

	/**
	 * 刷新缓冲区内数据，由子类提供
	 */
	protected abstract void flush(Queue<E> buf);

	/**
	 * 检查刷新条件 若满足，则刷新缓冲区
	 */
	public void checkFlush() {
		if ((last_flush + expire_interval < System.currentTimeMillis())
				|| (expire_time < System.currentTimeMillis())
				|| (buf.size() > capcity * 0.1) 
				|| (needFlush())) {
			if (expire_time < System.currentTimeMillis()) {
				expire_time = Long.MAX_VALUE / 2;
			}
			tryFlush();
		}
	}

	/**
	 * 由子类复写,提供自定义刷新条件
	 * 
	 * @return
	 */
	protected boolean needFlush() {
		return false;
	}
	
	/**
	 * 得到当前容量
	 * @return
	 */
	public int getSize() {
		return buf.size();
	}

	/**
	 * 得到最大容量
	 * @return
	 */
	public int getCapcity() {
		return capcity;
	}

	/**
	 * 设置最大容量
	 * @param capcity
	 */
	public void setCapcity(int capcity) {
		this.capcity = capcity;
	}

	/**
	 * 得到刷新间隔
	 * @return
	 */
	public long getExpire_interval() {
		return expire_interval;
	}

	/**
	 * 设置刷新间隔
	 * @param expireInterval
	 */
	public void setExpire_interval(long expireInterval) {
		expire_interval = expireInterval;
	}
	
	/**
	 * 得到刷新到期时间
	 * @return
	 */
	public long getExpire_time() {
		return expire_time;
	}
	
	/**
	 * 设置刷新到期时间
	 * @param expireInterval
	 */
	public void setExpire_time(long expireTime) {
		expire_time = expireTime;
	}

}

/***********测试类*************/
class SysOutBuffer extends CommonBuffer<String> {
	public SysOutBuffer() {
		super();
		expire_interval = 5000;
	}

	@Override
	protected void flush(Queue<String> buf) {
		System.out.print("\nFlushing...\n");
		while(!buf.isEmpty()) {
			System.out.println("SysOutBuffer:" + buf.poll());
		}
	}
}