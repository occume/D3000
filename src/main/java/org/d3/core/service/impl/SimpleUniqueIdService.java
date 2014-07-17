package org.d3.core.service.impl;

import java.util.concurrent.atomic.AtomicLong;

import org.d3.core.service.UniqueIdService;
import org.springframework.stereotype.Service;

@Service
public class SimpleUniqueIdService implements UniqueIdService {

	private static final AtomicLong uniqueId = new AtomicLong(1);
	private static final String		MARK = "D3_PLAYER_";
	
	public String generate() {
		return MARK + uniqueId.getAndIncrement();
	}

}
