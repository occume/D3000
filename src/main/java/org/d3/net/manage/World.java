package org.d3.net.manage;

import java.util.concurrent.atomic.AtomicLong;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class World {
	
	public static ChannelGroup ALL = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	public static AtomicLong MSG_COUNT = new AtomicLong();
	
}
