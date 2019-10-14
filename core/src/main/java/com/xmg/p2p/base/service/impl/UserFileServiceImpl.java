package com.xmg.p2p.base.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.domain.UserInfo;
import com.xmg.p2p.base.mapper.UserFileMapper;
import com.xmg.p2p.base.mapper.UserInfoMapper;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.UserFileQueryObject;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.util.UserContext;
/**
 * 风控资料业务相关
 * @author Dxd
 *
 */
@Service
public class UserFileServiceImpl implements IUserFileService{
	@Autowired
	private UserFileMapper userFileMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;

	/**
	 * 每一个风控资料对应着一个UserFile对象，这里根据传入的风控资料的相对路径将风控资料对象添加到数据库中
	 */
	@Override
	public void applyUserFile(String relativePath) {
		UserFile userFile = new UserFile();//创建一个新的风控资料对象
		userFile.setApplier(UserContext.getCurrentUser());//申请人
		userFile.setApplyTime(new Date());//申请时间
		userFile.setState(RealAuth.STATE_APPLY);//当前的审核状态
		userFile.setFile(relativePath);//风控资料存放的相对路径
		this.userFileMapper.insert(userFile);
	}

	//根据是否有风控资料类型进行查询
	@Override
	public List<UserFile> queryUserFileList(Long loginInfoId, boolean hasNotFileType) {
		return this.userFileMapper.queryUserFileList(loginInfoId,hasNotFileType);
	}

	/**
	 * 对各个风控资料的风控类型进行更新
	 */
	@Override
	public void updateUserFileTypes(Long[] ids, Long[] fileTypeIds) {
		for(int i = 0;i<ids.length;i++) {
			SystemDictionaryItem fileType = new SystemDictionaryItem();
			fileType.setId(fileTypeIds[i]);//哪一个风控类型的字典
			//查询出每一个风控资料
			UserFile currentUserFile = this.userFileMapper.selectNoTypeByPrimaryKey(ids[i]);
			if(currentUserFile != null) {
				currentUserFile.setFileType(fileType);
				this.userFileMapper.updateByPrimaryKey(currentUserFile);
			}
		}
	}

	@Override
	public PageResult query(UserFileQueryObject qo) {
		int totalCount = this.userFileMapper.queryUserFileCount(qo);
		if(totalCount > 0) {
			List<UserFile> userFiles = this.userFileMapper.query(qo);
			return new PageResult(userFiles,totalCount,qo.getCurrentPage(),qo.getPageSize());
		}
		return PageResult.empty();
	}
	//后台风控资料审核
	@Override
	public void audit(Long id, int state, int score, String remark) {
		//根据前台传入的id，从数据库中查询出是哪一个风控资料对象
		UserFile curUserFile = this.userFileMapper.selectByPrimaryKey(id);
		//判断风控资料对象是不是待审核状态
		if(curUserFile != null && curUserFile.getState() == UserFile.STATE_APPLY) {
			//更新风控资料相关信息
			curUserFile.setAuditor(UserContext.getCurrentUser());
			curUserFile.setAuditTime(new Date());
			curUserFile.setRemark(remark);
			curUserFile.setScore(score);
			curUserFile.setState(state);
			//如果当前的风控资料对象是审核通过的状态，才进行用户基本信息中征信分数的更新
			if(curUserFile.getState() == UserFile.STATE_PASS) {
				//得到当前风控资料是属于哪一个用户的用户基本信息对象
				UserInfo curUserInfo = this.userInfoMapper.selectByPrimaryKey(curUserFile.getApplier().getId());
				curUserInfo.setCreditBorrowScore((curUserInfo.getCreditBorrowScore()+curUserFile.getScore()));
				this.userInfoMapper.updateByPrimaryKey(curUserInfo);
			}
			this.userFileMapper.updateByPrimaryKey(curUserFile);
		}
		
	}

}
