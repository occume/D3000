package org.d3.test.lock;

import org.testng.annotations.Test;
import com.yayo.common.lock.ChainLock;
import com.yayo.common.lock.LockUtils;

public class LockUtiltest {
	
	@Test
	public void LockChainTest(){
		Object obj = new Object();
		ChainLock lock = LockUtils.getLock(obj);
		System.out.println(lock);
	}
	
}
