package org.d3;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class D3Context implements ApplicationContextAware{

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		D3Context.applicationContext = applicationContext;
	}

	private static ApplicationContext applicationContext;
	
	public static Object getBean(String beanName){
		if (null == beanName){
			return null;
		}
		return applicationContext.getBean(beanName);
	}
	
}