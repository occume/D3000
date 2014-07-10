package org.d3.jactor2;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;

public class PlayerTest {

	public static void main(String[] args)  {
		
		try {
			new Plant();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Player p1 = null;
		try {
			p1 = new Player();
		} catch (Exception e) {
			e.printStackTrace();
		}
		p1.onMessage(Thread.currentThread().getName()).signal();
		
		try {
			Player p2 = new Player();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class Player extends NonBlockingBladeBase{

	public Player() throws Exception {
		super();
	}
	
	public AOp<String> onMessage(final String msg){
		return new AOp<String>("onMessage", getReactor()) {
			
			public void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
					AsyncResponseProcessor<String> _asyncResponseProcessor)
					throws Exception {
				System.out.println("msg: " + msg + ":" + Thread.currentThread().getName());
				_asyncResponseProcessor.processAsyncResponse(null);
				System.out.println(_asyncResponseProcessor);
			}
		};
	}
	
}