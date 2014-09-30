package org.d3.storage.ocs;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import net.spy.memcached.internal.OperationFuture;

public class OCSTest {

	public static void main(String[] args) {

		final String host = "8ff65ec93a0711e4.m.cnhzalicm10pub001.ocs.aliyuncs.com";// 控制台上的“内网地址”

		final String port = "11211"; // 默认端口 11211，不用改

		final String username = "8ff65ec93a0711e4";// 控制台上的“访问账号”

		final String password = "a43d_656c";// 邮件或短信中提供的“密码”

		MemcachedClient cache = null;

		try {

			AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" },
					new PlainCallbackHandler(username, password));

			cache = new MemcachedClient(

			new ConnectionFactoryBuilder().setProtocol(Protocol.BINARY)

			.setAuthDescriptor(ad)

			.build(),

			AddrUtil.getAddresses(host + ":" + port));

			System.out.println("OCS Sample Code");

			// 向OCS中存一个key为"ocs"的数据，便于后面验证读取数据

			OperationFuture future = cache.set("ocs", 1000,
					" Open Cache Service,  from www.Aliyun.com");

			// 向OCS中存若干个数据，随后可以在OCS控制台监控上看到统计信息

			for (int i = 0; i < 100; i++) {

				String key = "key-" + i;

				String value = "value-" + i;

				// 执行set操作，向缓存中存数据

				cache.set(key, 1000, value);

			}

			System.out.println("Set操作完成!");

			future.get(); // 确保之前(mc.set())操作已经结束

			// 执行get操作，从缓存中读数据,读取key为"ocs"的数据

			System.out.println("Get操作:" + cache.get("ocs"));

		} catch (IOException e) {

			e.printStackTrace();

		} catch (InterruptedException e) {

			e.printStackTrace();

		} catch (ExecutionException e) {

			e.printStackTrace();

		}

		if (cache != null) {

			cache.shutdown();

		}

	}

}
