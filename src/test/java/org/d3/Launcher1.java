package org.d3;

import org.agilewiki.jactor2.core.impl.Plant;
import org.d3.launch.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher1 {
	
	static Logger LOG = LoggerFactory.getLogger(Launcher1.class);

	public static void main(String[] args) {
		
//		try {
//			new Plant(Runtime.getRuntime().availableProcessors() * 2);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		ApplicationContext context = new AnnotationConfigApplicationContext(D3SpringConfig.class);
//		Bootstrap b = (Bootstrap) context.getBean("bootstrap");
//		b.lanucher();
		LOG.info("info");
	}

}

