package org.d3.core.concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component(value="gate")
public class D3Gate {
	
	private int num = 25;
	private Semaphore pass = new Semaphore(num);
	
	public int acquire(){
		int qlen = 0;
		try {
			pass.acquire();
//			pass.tryAcquire(1, TimeUnit.SECONDS);
//			qlen = pass.getQueueLength();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return qlen;
	}
	
	@Scheduled(cron = "0/1 * * * * *")
	public void release(){
		if(pass.availablePermits() < num)
			pass.release(num);
	}
}
