package org.d3;

import org.agilewiki.jactor2.core.impl.Plant;
import org.d3.launch.Bootstrap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher1 {

	public static void main(String[] args) {
		
		try {
			new Plant(Runtime.getRuntime().availableProcessors() * 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ApplicationContext context = new AnnotationConfigApplicationContext(D3SpringConfig.class);
		Bootstrap b = (Bootstrap) context.getBean("bootstrap");
		b.lanucher();
		
	}

}

