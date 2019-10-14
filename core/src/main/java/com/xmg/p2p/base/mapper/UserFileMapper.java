package com.xmg.p2p.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.query.UserFileQueryObject;

public interface UserFileMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserFile record);

    UserFile selectByPrimaryKey(Long id);

    List<UserFile> selectAll();

    int updateByPrimaryKey(UserFile record);

	List<UserFile> queryUserFileList(@Param("loginInfoId")Long loginInfoId, @Param("hasNotFileType")boolean hasNotFileType);

	int queryUserFileCount(UserFileQueryObject qo);

	List<UserFile> query(UserFileQueryObject qo);

	UserFile selectNoTypeByPrimaryKey(Long long1);

}