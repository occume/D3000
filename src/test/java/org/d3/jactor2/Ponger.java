package org.d3.jactor2;

import org.agilewiki.jactor2.core.blades.BladeBase;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.SyncRequest;

public class Ponger extends BladeBase {
	
	private long count;
	
	public Ponger() throws Exception {
        _initialize(new NonBlockingReactor());
    }
    
    public Ponger(final Reactor _reactor) {
        _initialize(_reactor);
    }
    
    private long ping() {
        count += 1;
        return count;
    }

    //Directly callable
    public long ping(final Reactor _sourceReactor) {
        directCheck(_sourceReactor);
        return ping();
    }

    public SyncRequest pingSReq() {
        return new SyncBladeRequest() {
            @Override
            public Long processSyncRequest() {
            	System.out.println(Thread.currentThread());
                return ping();
            }
        };
    }
	
}
