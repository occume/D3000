package org.d3.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.d3.core.mybatis.domain.Message;

public interface MessageMapper {

  void addMessage(Message message);

  Message getByUid1(int uid1);
  
  Message getByUid2(int uid2);
  
  List<Message> getMsgsByRcvId(@Param(value="uid")int uid);
}
