package org.d3.core.mybatis.service;

import javax.annotation.Resource;

import org.d3.core.mybatis.domain.Account;
import org.d3.core.mybatis.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

  @Autowired
  private AccountMapper accountMapper;
  @Transactional
  public void insertAccount(Account account) {
    accountMapper.insertAccount(account);
  }
  
//  @ReadThroughSingleCache(namespace = "occume", expiration = 3600)
  public Account getAccount(int id) {
	  System.out.println(123);
	  return  accountMapper.getAccount(id);
  }
  
}
