package com.xmg.p2p.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.domain.UserInfo;
import com.xmg.p2p.base.service.ISystemDictionaryItemService;
import com.xmg.p2p.base.service.IUserInfoService;
import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.base.util.RequiredLogin;
import com.xmg.p2p.base.util.UserContext;
/**
 * 用户基本信息相关服务
 * @author Dxd
 *
 */
@Controller
public class BasicInfoController {
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private ISystemDictionaryItemService systemDictionaryItemService;
	
	/**
	 * 获得用户基本信息请求，这里也可以加上@RequiredLogin注解(判断用户是否已经登录)
	 * @param model
	 * @return
	 */
	@RequiredLogin
	@RequestMapping("/basicInfo")
	public String basicInfo(Model model) {
		LoginInfo currentUser = UserContext.getCurrentUser();
		//首先判断用户是否登录
		if(currentUser != null) {
			//根据登录用户得到用户基本信息
			UserInfo userInfo = userInfoService.get(currentUser.getId());
			model.addAttribute("userInfo",userInfo);//用户基本信息回显到前台
			//数据字典相关
			model.addAttribute("educationBackgrounds", systemDictionaryItemService.listItemByParentSn("educationBackground"));
			model.addAttribute("incomeGrades", systemDictionaryItemService.listItemByParentSn("incomeGrade"));
			model.addAttribute("marriages", systemDictionaryItemService.listItemByParentSn("marriage"));
			model.addAttribute("kidCounts", systemDictionaryItemService.listItemByParentSn("kidCount"));
			model.addAttribute("houseConditions", systemDictionaryItemService.listItemByParentSn("houseCondition"));
		}
		return "userInfo";
	}
	/**
	 * 个人基本信息保存操作
	 * @param userInfo
	 * @return
	 */
	@RequestMapping("/basicInfo_save")
	@ResponseBody
	public JSONResult basicInfoSave(UserInfo userInfo) {
		JSONResult result = new JSONResult();
		try {
			//为了数据的安全性，这里不是直接使用service类中的update(userInfo)的方法
			//这样做的目的是防止把userInfo里不为空的属性值给覆盖了(就是把那些本来有值的变成空值了)
			this.userInfoService.updateBasicInfo(userInfo);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("保存数据失败!");
		}
		return result;
	}
}
