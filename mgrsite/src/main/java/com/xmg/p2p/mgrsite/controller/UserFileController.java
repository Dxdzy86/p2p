package com.xmg.p2p.mgrsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.UserFileQueryObject;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.util.JSONResult;

/**
 * 后台风控资料审核
 * @author Dxd
 *
 */
@Controller
public class UserFileController {
	@Autowired
	private IUserFileService userFileService;
	
	//后台风控资料列表
	@RequestMapping("/userFileAuth")
	public String userFileAuth(Model model,@ModelAttribute("qo")UserFileQueryObject qo) {
		PageResult pageResult = this.userFileService.query(qo);
		model.addAttribute("pageResult",pageResult);
		return "userFileAuth/list";
	}
	
	//风控资料审核操作
	@RequestMapping("/userFile_audit")
	@ResponseBody
	public JSONResult userFileAudit(Long id,int state,int score,String remark) {
		JSONResult result = new JSONResult();
		try {
			this.userFileService.audit(id,state,score,remark);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("审核失败");
		}
		return result;
	}
}
