package org.d3.core.cache;

import redis.clients.jedis.Jedis;

public interface Invocable {
	public void invoke(Jedis jedis);
}