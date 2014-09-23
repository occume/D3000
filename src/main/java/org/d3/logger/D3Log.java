package org.d3.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class D3Log {
	public static void doLogger(LogType logType, String message) {
		Logger log = LoggerFactory.getLogger(logType.getLogName());
		if (log.isInfoEnabled() && message != null) {
			log.info(message);
		}
	}
}
