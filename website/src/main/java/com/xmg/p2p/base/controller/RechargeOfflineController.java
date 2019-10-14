package com.xmg.p2p.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmg.p2p.base.domain.RechargeOffline;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.RechargeOfflineQueryObject;
import com.xmg.p2p.base.service.IPlatformBankInfoService;
import com.xmg.p2p.base.service.IRechargeOfflineService;
import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.base.util.UserContext;

/**
 * 线下充值(转账)
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
	 * 前台显示p2p平台开户银行账号
	 * @param model
	 * @return
	 */
	@RequestMapping("/recharge")
	public String recharge(Model model) {
		model.addAttribute("banks", this.platformBankInfoService.listBanksAll());
		return "recharge";
	}
	
	/**
	 * 保存线下充值账单
	 * @param rechargeOffline
	 * @return
	 */
	@RequestMapping("/recharge_save")
	@ResponseBody
	public JSONResult rechargeSave(RechargeOffline rechargeOffline) {
		JSONResult result = new JSONResult();
		try {
			this.rechargeOfflineService.apply(rechargeOffline);
		}catch(Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("保存失败");
		}
		return result;
	}
	
	/**
	 * 前台查询转账明细
	 * @param qo
	 * @param model
	 * @return
	 */
	@RequestMapping("/recharge_list")
	public String rechargeList(@ModelAttribute("qo")RechargeOfflineQueryObject qo,Model model) {
		qo.setApplierId(UserContext.getCurrentUser().getId());
		PageResult pageResult = this.rechargeOfflineService.query(qo);
		model.addAttribute("pageResult",pageResult);
		return "recharge_list";
	}
}
