package org.d3.test.redis;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;

import org.d3.D3SpringConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

//@Component
public class RedisExample {
	
//	 @Autowired
//	 private RedisTemplate<String, String> template;

	    // inject the template as ListOperations
	    // can also inject as Value, Set, ZSet, and HashOperations
//	 @Resource(name="redisTemplate")
//	 private ListOperations<String, String> listOps;

	 public void addLink(String userId, URL url) {
//		 listOps.leftPush(userId, url.toExternalForm());
	     // or use template directly
//		 template.boundListOps(userId).leftPush(url.toExternalForm());
	 }

	public static void main(String[] args) throws MalformedURLException {
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(D3SpringConfig.class);
		
		RedisExample e = (RedisExample) context.getBean("redisExample");
		e.addLink("occume", new URL("http://www.t7joy.com"));
	}

}
