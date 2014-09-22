package org.d3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.agilewiki.jactor2.core.impl.Plant;
import org.d3.launch.Bootstrap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {

	public static void main(String[] args) throws IOException {
		
		try {
			new Plant(Runtime.getRuntime().availableProcessors() * 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ApplicationContext context = new AnnotationConfigApplicationContext(D3SpringConfig.class);
		Bootstrap b = (Bootstrap) context.getBean("bootstrap");
		b.lanucher();
//		int socketCount = 0;
//		ServerSocket ss = new ServerSocket(10086);
//		while(true){
//			Socket s = ss.accept();
//			socketCount++;
//			System.out.println("socketCount: " + socketCount);
//		}
		
	}

}

