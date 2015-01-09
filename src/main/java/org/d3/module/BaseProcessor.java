package org.d3.module;

import java.io.IOException;

import javax.annotation.PostConstruct;

//import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
//import org.agilewiki.jactor2.core.requests.AOp;
//import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
//import org.agilewiki.jactor2.core.requests.ExceptionHandler;
//import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.d3.D3Context;
import org.d3.net.packet.InPacket;
import org.d3.net.packet.Packet;
import org.d3.net.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseProcessor implements Processor {
//	public abstract class BaseProcessor  extends NonBlockingBladeBase implements Processor {

	private static Logger LOG = LoggerFactory.getLogger(BaseProcessor.class);

	public void process(Session session, InPacket pkt) {
		
//		try {
//			process0(session, pkt).call();
//		} catch (Throwable e) {
//			if(LOG.isDebugEnabled())
//				e.printStackTrace();
//			LOG.error(e.getMessage());
//		}
		doProcess(session, pkt);
		
	}
	
//	public AOp<Packet> process0(final Session session, final InPacket pkt){
//		return new AOp<Packet>("transfer-onMessage", getReactor()) {
//
//			@SuppressWarnings({ "rawtypes", "unchecked" })
//			public void processAsyncOperation(
//					AsyncRequestImpl _asyncRequestImpl,
//					AsyncResponseProcessor<Packet> _asyncResponseProcessor)
//					{
//
//				_asyncRequestImpl.setExceptionHandler(new ExceptionHandler<String>() {
//					@Override
//						public String processException(final Exception _exception) {
//							System.out.println("--------------");
//							return null;
//						}
//					}
//				);
//				
//				try {
////					validate(session, pkt);
//					doProcess(session, pkt);
//					_asyncResponseProcessor.processAsyncResponse(null);
//				} catch (Throwable e) {
//					if(LOG.isDebugEnabled())
//						e.printStackTrace();
//				}
//				
//			}
//		};
//	}
	
	
	
	public abstract void doProcess(Session session, InPacket pkt);
	
	public abstract String getModuleName();
	
	public abstract int getType();
	
	public abstract String getDescription();
	
//	public abstract void validate(Session session, InPacket pkt);
	
	@PostConstruct
	public void register() {
//		Registry registry = (Registry) D3Context.getBean(getModuleName());
//		if(registry != null){
//			if(LOG.isDebugEnabled()){
//				LOG.debug("register " +getDescription());
//			}
////			registry.register(getType(), this);
//		}
	}

}
