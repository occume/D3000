package org.d3.launch;

import java.util.Set;

import org.d3.D3Context;
import org.d3.core.service.Jactor2StartService;
import org.d3.core.service.ReportClientStartService;
import org.d3.core.service.TcpServerStartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.common.util.concurrent.ServiceManager.Listener;

/**
 * 
 * @author d_jin
 *
 */
@Component
public class Bootstrap{

//	@Autowired
//	private BufferFacade bufferFacade;

	private Logger LOG = LoggerFactory.getLogger(getClass());
	
	ServiceManager serviceManager;
	
	public Bootstrap(){
		
		
	}
	
	public void lanucher(){
		
		Set<Service> services = Sets.newLinkedHashSet();
		/**
		 * TCP服务
		 */
		TcpServerStartService tcpService = (TcpServerStartService) D3Context.getBean("tcpServerStartService");
		services.add(tcpService);
		/**
		 * 节点状态报告服务
		 */
		ReportClientStartService clientService = (ReportClientStartService) D3Context.getBean("reportClientStartService");
		services.add(clientService);
		
		/**
		 * Jactor2服务
		 */
		Jactor2StartService jactor2Service = (Jactor2StartService) D3Context.getBean("jactor2StartService");
		services.add(jactor2Service);
		
		serviceManager = new ServiceManager(services);
		serviceManager.addListener(new Listener() {

			@Override
			public void failure(Service service) {
				LOG.error("Service does not launch: " + service.state());
			}

			@Override
			public void healthy() {
				System.out.println("all service has been started");
			}

			@Override
			public void stopped() {
				LOG.info("all Service has been stopped");
			}
			
		}, MoreExecutors.sameThreadExecutor());
		
		serviceManager.startAsync();
	}
	
	public void shoutDown(){
		serviceManager.stopAsync();
	}
	
	/** 启动定时任务 */
	 @Scheduled(cron = "0/5 * * * * *")  
	protected void startScheduleTask() {
		
		//schedule tasks

	}
	
}
