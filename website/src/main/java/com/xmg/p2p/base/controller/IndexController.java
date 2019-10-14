package com.xmg.p2p.base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.service.IBidRequestService;
/**
 * 首页相关
 * @author Dxd
 *
 */
@Controller
public class IndexController {
	@Autowired
	private IBidRequestService bidRequestService;
	
	//首页需要显示的标
	@RequestMapping("/index")
	public String index(Model model) {
		//查询出(投标中；还款中；已完成的标) 这3种标，按照投标中>还款中>已完成的标排序，显示5条标的记录
		List<BidRequest> bidRequests = this.bidRequestService.queryIndexBidRequests(5);
		model.addAttribute("bidRequests", bidRequests);
		return "main";
	}
	
	//进入投资列表
	@RequestMapping("/invest")
	public String invest(Model model) {
		return "invest";
	}
	
	//根据前台的请求，将内容列表返回给前台进行替换
	@RequestMapping("/invest_list")
	public String investList(@ModelAttribute("qo")BidRequestQueryObject qo,Model model) {
		PageResult pageResult = this.bidRequestService.query(qo);
		model.addAttribute("pageResult", pageResult);
		return "invest_list";
	}
}
