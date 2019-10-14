package com.xmg.p2p.mgrsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xmg.p2p.base.query.IpLogQueryObject;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.service.IIpLogService;
import com.xmg.p2p.base.util.UserContext;
/**
 * 后台登录日志
 * @author Dxd
 *
 */
@Controller
public class IpLogController {
	@Autowired
	private IIpLogService ipLogService;
	
	@RequestMapping("/ipLog")
	public String iplogList(Model model,@ModelAttribute("qo")IpLogQueryObject qo) {
		qo.setUsername(UserContext.getCurrentUser().getUsername());
	    PageResult pageResult = ipLogService.query(qo);
		model.addAttribute("pageResult",pageResult);
		return "/ipLog/list";
	}
}
