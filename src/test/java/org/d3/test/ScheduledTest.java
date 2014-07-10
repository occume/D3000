package org.d3.test;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ScheduledTest {

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "beans.xml" });
		
		System.out.println(Arrays.asList(context.getBeanDefinitionNames()));
//		Thread.sleep(100000);
	}

}
