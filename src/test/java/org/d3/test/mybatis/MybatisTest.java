package org.d3.test.mybatis;

import org.d3.D3SpringConfig;
import org.d3.core.mybatis.domain.Account;
import org.d3.core.mybatis.domain.User;
import org.d3.core.mybatis.service.UserService;
import org.d3.std.Stopwatch;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MybatisTest {
	
	public static ApplicationContext ctx;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ctx = new AnnotationConfigApplicationContext(D3SpringConfig.class);
	}
	
	@Test
	public void addUser(){
		
		UserService userService = (UserService) ctx.getBean("userService");
		
		Stopwatch sw = Stopwatch.newStopwatch();
		for(int i = 0; i < 100; i++){
			
			User user = new User();
			user.setName("d3" + i);
			user.setPassword("123");
			user.setEmail("d3"+ i +"@d3.com");
			
			userService.addUser(user);
		}
		System.out.println("it cost " + sw.longTime());
	}
	
	@Test
	public void f() {

//		AccountDao accountServie = (AccountDao) ctx.getBean("accountService");
		Account account = new Account();
		account.setUsername("ccoo");
		account.setPassword("456");
		account.setEmail("occume@gmail.com");
//		accountServie.insertAccount(account);
		
	}
	
	@Test
	public void g() {

//		AccountDao accountServie = (AccountDao) ctx.getBean("accountService");
//		Account account = new Account();
//		account.setUsername("foo");
//		account.setPassword("123");
//		account.setEmail("occume@gmail.com");
//		Account a = accountServie.getAccount(1);
//		System.out.println(a.getUsername());
	}
}
