package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.EmailVerify;

public interface EmailVerifyMapper {
    int insert(EmailVerify record);

    EmailVerify selectByPrimaryKey(Long id);

    int updateByPrimaryKey(EmailVerify record);

	EmailVerify selectByRandomCode(String randomcode);
}