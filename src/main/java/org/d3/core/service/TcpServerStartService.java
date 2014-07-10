package org.d3.core.service;

import org.d3.core.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.MoreExecutors;

@Component
public class TcpServerStartService extends AbstractService {

	@Autowired
	Server tcpServer;
	
	public TcpServerStartService(){
		super();
		addListener(
		        new Listener() {
					@Override
					public void starting() {
						TcpServerStartService.this.notifyStarted();
					}
		        },
		        MoreExecutors.sameThreadExecutor());
	}
	
	@Override
	protected void doStart() {
		tcpServer.launch();
	}

	@Override
	protected void doStop() {
		tcpServer.shutDown();
	}

	
}
