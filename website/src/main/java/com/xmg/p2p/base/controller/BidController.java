package com.xmg.p2p.base.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.base.util.RequiredLogin;
import com.xmg.p2p.business.service.IBidRequestService;

/**
 * 投标相关
 * @author Dxd
 *
 */
@Controller
public class BidController {
	@Autowired
	private IBidRequestService bidRequestService;
	
	@RequiredLogin
	@RequestMapping("/borrow_bid")
	@ResponseBody
	public JSONResult borrowBid(Long bidRequestId,BigDecimal amount) {
		JSONResult result = new JSONResult();
		try {
			this.bidRequestService.bid(bidRequestId,amount);
		}catch(Exception e) {
			result.setSuccess(false);
			result.setMsg("投标失败");
		}
		return result;
	}
}
