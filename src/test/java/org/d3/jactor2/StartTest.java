package org.d3.jactor2;

import org.agilewiki.jactor2.core.impl.Plant;

public class StartTest {

	public static void main(String[] args) throws Exception {
		
		 new Plant();
//		 Plant.getInternalReactor().
	        try {
	            Ponger ponger = new Ponger();
	            System.out.println(Thread.currentThread());
	            long j = (Long) ponger.pingSReq().call();
	            if (1 != j)
	                throw new IllegalStateException("unexpected result");
	        } finally {
	            Plant.close();
	        }
		
	}

}
