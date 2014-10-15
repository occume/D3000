package org.d3.core.mybatis.mapper;

import org.d3.core.mybatis.domain.Account;

public interface AccountMapper {

  void insertAccount(Account account);
  
  Account getAccount(int id);

}
