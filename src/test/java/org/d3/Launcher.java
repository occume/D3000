package org.d3;

import org.d3.launch.Bootstrap;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class Launcher {

	public static void main(String[] args) {
		
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(D3SpringConfig.class);
		Bootstrap b = (Bootstrap) context.getBean("bootstrap");
		b.lanucher();
		
	}

}
