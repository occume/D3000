package org.d3.core.cache;

import java.io.Closeable;
import java.util.ArrayList;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisPools implements Closeable {
	private static final int DEFAULT_TIMEOUT = 120000;
	JedisPool[] pools;
	String[] addrs;

	public JedisPools(String addresses) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setTestOnBorrow(true);
		config.setMaxActive(100);
		config.setMaxIdle(50);
		config.setMaxWait(10000);
		if (addresses == null) {
			pools = new JedisPool[] {new JedisPool(config,
					"localhost", 6379, DEFAULT_TIMEOUT)};
			return;
		}
		ArrayList<JedisPool> pools_ = new ArrayList<>();
		ArrayList<String> addrs_ = new ArrayList<>();
		for (String address : addresses.split("[,;]")) {
			String[] ss = address.split("[:/]");
			if (ss.length >= 2) {
				int port = Integer.valueOf(ss[1]);
				if (port == 0) {
					continue;
				}
				addrs_.add(ss[0] + ':' + ss[1]);
				pools_.add(new JedisPool(config, ss[0], port,
						DEFAULT_TIMEOUT, ss.length >= 3 ? ss[2] : null));
			}
		}
		if (pools_.isEmpty()) {
			pools = new JedisPool[] {new JedisPool(config, "localhost")};
			addrs = new String[] {"localhost"};
		} else {
			pools = pools_.toArray(new JedisPool[0]);
			addrs = addrs_.toArray(new String[0]);
		}
	}

	private static void invoke(JedisPool pool, Invocable invocable) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			invocable.invoke(jedis);
			pool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			if (jedis != null) {
				pool.returnBrokenResource(jedis);
			}
		}
	}

	public void invoke(int poolId, Invocable invocable) {
		invoke(pools[poolId], invocable);
	}

	public void invokeAll(Invocable invocable) {
		for (JedisPool pool : pools) {
			invoke(pool, invocable);
		}
	}

	public int getPoolNum() {
		return pools.length;
	}

	@Override
	public void close() {
		for (JedisPool pool : pools) {
			pool.destroy();
		}
	}
}