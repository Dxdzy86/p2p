package com.xmg.p2p.mgrsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.RealAuthQueryObject;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.util.JSONResult;

/**
 * 后台实名认证相关
 * @author Dxd
 *
 */
@Controller
public class RealAuthController {
	@Autowired
	private IRealAuthService realAuthService;
	
	/**
	 * 实名认证审核
	 * @return
	 */
	@RequestMapping("/realAuth")
	public String realAuth(@ModelAttribute("qo")RealAuthQueryObject qo,Model model) {
		PageResult pageResult = this.realAuthService.query(qo);
		model.addAttribute("pageResult",pageResult);
		return "realAuth/list";
	}
	/**
	 * 后台审核用户实名认证操作
	 * @param realAuth
	 * @return
	 */
	@RequestMapping("/realAuth_audit")
	@ResponseBody
	public JSONResult realAuthAudit(Long id,int state,String remark) {
		JSONResult result = new JSONResult();
		try {
			this.realAuthService.audit(id,state,remark);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("审核失败");
		}
		return result;
	}
}
