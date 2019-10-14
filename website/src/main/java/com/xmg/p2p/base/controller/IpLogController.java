package com.xmg.p2p.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xmg.p2p.base.query.IpLogQueryObject;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.service.IIpLogService;
import com.xmg.p2p.base.util.RequiredLogin;
import com.xmg.p2p.base.util.UserContext;
/**
 * 登录日志
 * @author Dxd
 *
 */
@Controller
public class IpLogController {
	@Autowired
	private IIpLogService ipLogService;	
	@RequiredLogin
	@RequestMapping("/ipLog")
	public String ipLogList(Model model,@ModelAttribute("qo")IpLogQueryObject qo) {
		//只对当前登录用户的日志记录进行高级查询
		qo.setUsername(UserContext.getCurrentUser().getUsername());
		PageResult pageResult = ipLogService.query(qo);
		model.addAttribute("pageResult",pageResult);
		return "iplog_list";
	}
}
