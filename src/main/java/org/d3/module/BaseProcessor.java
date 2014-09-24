package org.d3.module;

import javax.annotation.PostConstruct;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.d3.D3Context;
import org.d3.net.packet.InPacket;
import org.d3.net.packet.Packet;
import org.d3.net.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseProcessor  extends NonBlockingBladeBase implements Processor {

	public BaseProcessor() throws Exception {
		super();
	}

	private static Logger LOG = LoggerFactory.getLogger(BaseProcessor.class);

	public void process(Session session, InPacket pkt) {

		try {
			process0(session, pkt).call();
		} catch (Exception e) {
			if(LOG.isDebugEnabled())
				e.printStackTrace();
			LOG.error(e.getMessage());
		}
		
	}
	
	public AOp<Packet> process0(final Session session, final InPacket pkt){
		return new AOp<Packet>("transfer-onMessage", getReactor()) {

			public void processAsyncOperation(
					AsyncRequestImpl _asyncRequestImpl,
					AsyncResponseProcessor<Packet> _asyncResponseProcessor)
					{
				
				doProcess(session, pkt);
				
				try {
					_asyncResponseProcessor.processAsyncResponse(null);
				} catch (Exception e) {
					if(LOG.isDebugEnabled())
						e.printStackTrace();
				}
			}
		};
	}
	
	
	
	public abstract void doProcess(Session session, InPacket pkt);
	
	public abstract String getModuleName();
	
	public abstract int getType();
	
	@PostConstruct
	public void register() {
		Registry registry = (Registry) D3Context.getBean(getModuleName());
		if(registry != null){
			if(LOG.isDebugEnabled()){
				LOG.debug("register LoginModule");
			}
			registry.register(getType(), this);
		}
	}

}
