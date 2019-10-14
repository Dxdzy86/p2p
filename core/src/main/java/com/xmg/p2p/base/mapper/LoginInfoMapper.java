package com.xmg.p2p.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xmg.p2p.base.domain.LoginInfo;

public interface LoginInfoMapper {
    int insert(LoginInfo record);

    LoginInfo selectByPrimaryKey(Long id);

    List<LoginInfo> selectAll();

	int getCountByUsername(@Param("username")String username,@Param("userType")int userType);

	LoginInfo login(@Param("username")String username,@Param("password")String password,@Param("userType")int userType);
}