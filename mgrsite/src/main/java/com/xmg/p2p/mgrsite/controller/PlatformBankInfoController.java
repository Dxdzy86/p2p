package com.xmg.p2p.mgrsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xmg.p2p.base.domain.PlatformBankInfo;
import com.xmg.p2p.base.query.PlatformBankInfoQueryObject;
import com.xmg.p2p.base.service.IPlatformBankInfoService;

/**
 * 平台账号
 * @author Dxd
 *
 */
@Controller
public class PlatformBankInfoController {
	@Autowired
	private IPlatformBankInfoService platformBankInfoService;
	
	/**
	 * 平台账号列表
	 * @param qo
	 * @param model
	 * @return
	 */
	@RequestMapping("/companyBank_list")
	public String companyBankList(@ModelAttribute("qo")PlatformBankInfoQueryObject qo,Model model) {
		model.addAttribute("pageResult", this.platformBankInfoService.query(qo));
		
		return "platformbankinfo/list";
	}
	
	@RequestMapping("/companyBank_update")
	public String companyBankUpdate(PlatformBankInfo info) {
		this.platformBankInfoService.saveOrUpdate(info);
		
		return "redirect:/companyBank_list.do";
	}
}
