package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.Account;

public interface IAccountService {
	Account get(Long id);
	
	int update(Account account);
}
