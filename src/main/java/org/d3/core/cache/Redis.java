package org.d3.core.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.d3.std.Stopwatch;

import redis.clients.jedis.Jedis;

public class Redis {
	
	static JedisPools pools;
	static int POOL_NUM;

	static {
		pools = new JedisPools("127.0.0.1:6379");
		POOL_NUM = pools.getPoolNum();
	}

	public static void shutdown() {
		pools.close();
	}
	
	public static void main(String...strings){
		Stopwatch sw = Stopwatch.newStopwatch();
		for(int i = 0; i < 1000000; i++){
			add("d3user" + i, "d3user" + i);
		}
		load("d3user261170");
		System.out.println(sw.longTime());
//		add("Auth", "sth5");
//		addSet("aaa", "bbb", "ccc");
//		clearList();
//		pushList("111", "222", "333");
//		System.out.println(listSize());
	}
	
	private static final String NS_USER = "d3:user";
	
	public static void pushList(final String ns, final String...members){
		pools.invoke(3, new Invocable() {
			@Override
			public void invoke(Jedis jedis) {
				jedis.lpush(NS_USER + ns, members);
			}
		});
	}
	
	public static long listSize(final String ns){
		final List<Long> list = new ArrayList<>();
		pools.invoke(3, new Invocable() {
			@Override
			public void invoke(Jedis jedis) {
				list.add(jedis.llen(NS_USER + ns));
			}
		});
		return list.get(0);
	}
	
	public static String popList(final String ns) {
		final List<String> list = new ArrayList<>();
		pools.invoke(3, new Invocable() {
			@Override
			public void invoke(Jedis jedis) {
				list.add(jedis.lpop(NS_USER + ns));
			}
		});
		return list.get(0);
	}
	
	public static void clearList(final String ns) {
		pools.invoke(3, new Invocable() {
			@Override
			public void invoke(Jedis jedis) {
				long len = jedis.llen(NS_USER + ns);
				jedis.ltrim(NS_USER, 1, 0);
			}
		});
	}
	
	public static void addSet(final String...members){
		pools.invoke(3, new Invocable() {
			@Override
			public void invoke(Jedis jedis) {
				jedis.sadd(NS_USER, members);
			}
		});
	}
	
	public static void clearSet(){
		pools.invoke(3, new Invocable() {
			@Override
			public void invoke(Jedis jedis) {
				Set<String> mbs = jedis.smembers(NS_USER);
				for(String mb: mbs){
					jedis.srem(NS_USER, mb);
				}
			}
		});
	}
	
	public static void add(final String key, final String value){
		pools.invoke(0, new Invocable() {
			@Override
			public void invoke(Jedis jedis) {
				jedis.hset(NS_USER, key, value);
			}
		});
	};
	
	public static void update(String key, String content){
		add(key, content);
	}
	
	public static String loadWriter(){
		return load("Writer");
	}
	
	public static String loadDispatcher(){
		return load("Dispatcher");
	}
	
	public static String loadAuth(){
		return load("Auth");
	}
	
	public static String load(final String name) {
		final List<String> list = new ArrayList<>();
		pools.invoke(0, new Invocable() {
			@Override
			public void invoke(Jedis jedis) {
				list.add(jedis.hget(NS_USER, name));
			}
		});
		return list.get(0);
	}
	
}