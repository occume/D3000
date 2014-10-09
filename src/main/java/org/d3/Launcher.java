package org.d3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.d3.launch.Bootstrap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {

	public static void main(String[] args) throws IOException {
		
//		try {
//			new Plant(100);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		ApplicationContext context = new AnnotationConfigApplicationContext(D3SpringConfig.class);
		Bootstrap b = (Bootstrap) context.getBean("bootstrap");
		b.lanucher();
		
	}

}

