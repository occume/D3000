package org.d3.test;

import com.yayo.common.lock.ChainLock;
import com.yayo.common.lock.LockUtils;

public class ChainLockTest {
	
	public static void main(String...strings) throws InterruptedException{
		
		final ChainLockTest test = new ChainLockTest();
		
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				for(int i = 0; i < 1000000; i++){
					test.increment();
				}
			}
		});
		
		
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				for(int i = 0; i < 1000000; i++){
					test.decrement();
				}
			}
		});
		
		long start = System.currentTimeMillis();
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
		System.out.println(System.currentTimeMillis() - start);
		System.out.println(test.getTotal());
	}
	
	private int total = 1000;
	private ChainLock lock = LockUtils.getLock(this);
	
	public void increment()
	{
		try{
			lock.lock();
			total = total + 10;
		}
		finally{
			lock.unlock();
		}
	}
	
	public void decrement(){
		try{
			lock.lock();
			total = total - 10;
		}
		finally{
			lock.unlock();
		}
	}
	
//	public synchronized void increment()
//	{
//		total = total + 10;
//	}
//	
//	public synchronized void decrement()
//	{
//		total = total - 10;
//	}
	
	public int getTotal(){
		return total;
	}
}
