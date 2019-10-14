package com.xmg.p2p.mgrsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.RechargeOfflineQueryObject;
import com.xmg.p2p.base.service.IPlatformBankInfoService;
import com.xmg.p2p.base.service.IRechargeOfflineService;
import com.xmg.p2p.base.util.JSONResult;

/**
 * 后台审核用户的充值单
 * @author Dxd
 *
 */
@Controller
public class RechargeOfflineController {
	
	@Autowired
	private IPlatformBankInfoService platformBankInfoService;
	@Autowired
	private IRechargeOfflineService rechargeOfflineService;
	
	/**
	 * 查询用户充值的情况
	 * @param qo
	 * @param model
	 * @return
	 */
	@RequestMapping("/rechargeOffline")
	public String rechargeOffline(@ModelAttribute("qo")RechargeOfflineQueryObject qo,Model model) {
		model.addAttribute("banks", this.platformBankInfoService.listBanksAll());
		PageResult pageResult = this.rechargeOfflineService.query(qo);
		model.addAttribute("pageResult", pageResult);
		return "rechargeoffline/list";
	}
	
	@RequestMapping("/rechargeOffline_audit")
	@ResponseBody
	public JSONResult rechargeOfflineAudit(Long id,int state,String remark) {
		JSONResult result = new JSONResult();
		try {
			this.rechargeOfflineService.audit(id,state,remark);
			result.setSuccess(true);
		}catch(Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
}
