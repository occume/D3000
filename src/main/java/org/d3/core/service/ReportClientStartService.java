package org.d3.core.service;

import org.d3.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.MoreExecutors;

@Component
public class ReportClientStartService extends AbstractService {

	@Autowired
	Client client;
	
	public ReportClientStartService(){
		super();
		addListener(
		        new Listener() {
					@Override
					public void starting() {
						ReportClientStartService.this.notifyStarted();
					}
		        },
		        MoreExecutors.sameThreadExecutor());
	}
	
	@Override
	protected void doStart() {
		client.start();
	}

	@Override
	protected void doStop() {
		client.stop();
	}

}
