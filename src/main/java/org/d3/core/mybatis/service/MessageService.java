package org.d3.core.mybatis.service;

import java.util.List;

import org.d3.core.mybatis.domain.Message;
import org.d3.core.mybatis.mapper.MessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

	private static Logger LOG = LoggerFactory.getLogger(MessageService.class);
	
	@Autowired
	private MessageMapper messageMapper;

	@Transactional
	public void addMessage(Message message) {
		messageMapper.addMessage(message);
	}

	public Message getByUid1(int uid1) {
		return messageMapper.getByUid1(uid1);
	}

	public Message getByUid2(int uid2) {
		return messageMapper.getByUid2(uid2);
	}

	public List<Message> getMsgsByRcvId(int rcvId){
		return messageMapper.getMsgsByRcvId(rcvId);
	}
	
	public Message getMsg(Message message){
		return messageMapper.getMsg(message);
	}
}
