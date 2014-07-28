package org.d3.core.service;

import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.impl.mtPlant.PlantConfiguration;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.MoreExecutors;

@Component
public class Jactor2StartService extends AbstractService {

	public Jactor2StartService(){
		super();
		addListener(
		        new Listener() {
					@Override
					public void starting() {
						Jactor2StartService.this.notifyStarted();
					}
		        },
		        MoreExecutors.sameThreadExecutor());
	}
	
	@Override
	protected void doStart() {
		try {
			new Plant(Runtime.getRuntime().availableProcessors() * 2);
			PlantConfiguration config = new PlantConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doStop() {
		try {
			Plant.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
