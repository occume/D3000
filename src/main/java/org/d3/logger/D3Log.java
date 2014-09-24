package org.d3.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class D3Log {
	
	public static final String D3_LOG_DEBUG = "[D3-DEBUG]>>";
	public static final String D3_LOG_INFO = "[D3-INFO]>>";
	public static final String D3_LOG_ERROR = "[D3-ERROR]>>";
	
	public static void doLogger(LogType logType, String message) {
		Logger log = LoggerFactory.getLogger(logType.getLogName());
		if (log.isInfoEnabled() && message != null) {
			log.info(message);
		}
	}
}
