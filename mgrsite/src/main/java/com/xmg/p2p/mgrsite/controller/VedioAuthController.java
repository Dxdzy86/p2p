package com.xmg.p2p.mgrsite.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.VedioAuthQueryObject;
import com.xmg.p2p.base.service.IVedioAuthService;
import com.xmg.p2p.base.util.JSONResult;
/**
 * 视频认证相关
 * @author Dxd
 *
 */
@Controller
public class VedioAuthController {
	
	@Autowired
	private IVedioAuthService vedioAuthService;

	/**
	 * 视频认证审核列表
	 * @param qo
	 * @param model
	 * @return
	 */
	@RequestMapping("/vedioAuth")
	public String vedioAuthList(@ModelAttribute("qo")VedioAuthQueryObject qo,Model model) {
		PageResult pageResult = this.vedioAuthService.query(qo);
		model.addAttribute("pageResult",pageResult);
		return "vedioAuth/list";
	}
	
	/**
	 * 负责视频认证审核
	 * @param state
	 * @param loginInfoValue
	 * @param remark
	 * @return
	 */
	@RequestMapping("/vedioAuth_audit")
	@ResponseBody
	public JSONResult vedioAuthAudit(int state,Long loginInfoValue,String remark) {
		JSONResult result = new JSONResult();
		try {
			this.vedioAuthService.audit(state,loginInfoValue,remark);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("审核拒绝");
		}
		return result;
	}
	
	/**
	 * 自动补全
	 * @param keyword
	 * @return
	 */
	@RequestMapping("/vedioAuth_autocomplete")
	@ResponseBody
	public List<HashMap<String,Object>> autocomplete(String keyword) {
		List<HashMap<String,Object>> keywordList = this.vedioAuthService.autoCompleteList(keyword);
		return keywordList;
	}
}
