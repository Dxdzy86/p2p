package com.xmg.p2p.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.mapper.AccountMapper;
import com.xmg.p2p.base.service.IAccountService;
@Service
public class AccountServiceImpl implements IAccountService{
	@Autowired
	private AccountMapper accountMapper;
	@Override
	public Account get(Long id) {
		return accountMapper.selectByPrimaryKey(id);
	}

	@Override
	public int update(Account account) {
		return accountMapper.updateByPrimaryKey(account);
	}

}
