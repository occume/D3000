package org.d3.net.client;

import java.util.Map;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import org.d3.client.Client;
import org.d3.net.session.SessionManager;
import org.d3.thread.NamedThreadFactory;
import org.d3.util.ObjectConvert;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.testng.collections.Maps;

@Component
public class ReportClient implements Client{
	
	private EventLoopGroup worker;
	private Bootstrap b;
	private Channel channel;
	
	public void start(){
		worker = new NioEventLoopGroup(1, new NamedThreadFactory("D3-REPORT"));
		
		b = new Bootstrap();
		b.group(worker)
		 .channel(NioSocketChannel.class)
		 .option(ChannelOption.SO_KEEPALIVE, true)
		 .handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				
				ch.pipeline().addLast(new StringEncoder());
				ch.pipeline().addLast(new ReportClientHandler());
				
			}
			
		});
		
		while(true){
			try {
				channel = b.connect("127.0.0.1", 21060).sync().channel();
				System.out.println("connect to 21060");
				break;
			} catch (Exception e) {
				if(tryCount > 1000){
					worker.shutdownGracefully();
				}
				tryCount++;
//				System.out.println("try " + tryCount);
			}
		}
	}
	
	public static void main(String...strings){
		Client client = new ReportClient();
		client.start();
	}
	private int tryCount = 0;
	
	/** 启动定时任务 */
	 @Scheduled(cron = "0/5 * * * * *")  
	protected void startScheduleTask() {
//		System.out.println("report heart " + channel);
		if(channel == null || !channel.isActive())
			return;
		
		Map<String, Integer> state = Maps.newHashMap();
		int channelCount = SessionManager.getInstance().getChanneCount();
		state.put("channelCount", channelCount);
		String myState = ObjectConvert.Me().ojb2json(state);
		channel.writeAndFlush(myState);
		
	}
	
	private void tryStart(){
		
	}

	public void stop() {
		worker.shutdownGracefully();
	}

}
