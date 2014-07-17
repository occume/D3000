package org.d3;

import org.d3.launch.Bootstrap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {

	public static void main(String[] args) {
		
		ApplicationContext context = new AnnotationConfigApplicationContext(D3SpringConfig.class);
		Bootstrap b = (Bootstrap) context.getBean("bootstrap");
		b.lanucher();
		
	}

}
