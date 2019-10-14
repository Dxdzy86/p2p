package com.xmg.p2p.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.domain.UserInfo;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.service.IUserInfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.BitStatesUtil;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.service.IBidRequestService;
/**
 * 借款界面
 * @author Dxd
 *
 */
@Controller
public class BorrowController {
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IBidRequestService bidRequestService;
	@Autowired
	private IRealAuthService realAuthService;
	@Autowired
	private IUserFileService userFileService;
	/**
	 * 跳转借款页面方法
	 * @param model
	 * @return
	 */
	@RequestMapping("/borrow")
	public String borrow(Model model) {
		//首先判断用户是否登录，若已经登录，则跳转到动态"借款"页面；否则，跳转到"借款"静态页面
		LoginInfo currentUser = UserContext.getCurrentUser();
		if(currentUser != null) {
			UserInfo userInfo = userInfoService.get(currentUser.getId());
			if(userInfo != null) {
				model.addAttribute("userInfo",userInfo);
				model.addAttribute("account",accountService.get(currentUser.getId()));
				model.addAttribute("creditBorrowScore",BitStatesUtil.CREDIT_BORROW_SCORES);
			}
			return "borrow";
		}
		return "redirect:/borrow.html";//重定向到根目录下的静态页面
	}
	
	/**
	 * 申请借款
	 */
	@RequestMapping("/borrowInfo")
	public String borrowInfo(Model model) {
		LoginInfo loginInfo = UserContext.getCurrentUser();
		//跳转到借款信息页面之前，需要验证一下用户是否满足4个借款条件以及用户当前没有借款状态(未借款)
		if(this.bidRequestService.canApplyBidRequest(loginInfo)) {
			model.addAttribute("minBidRequestAmount",BidConst.SMALLEST_BIDREQUEST_AMOUNT);//系统最小借款金额
			model.addAttribute("account",this.accountService.get(loginInfo.getId()));
			model.addAttribute("minBidAmount",BidConst.SMALLEST_BID_AMOUNT);
			return "borrow_apply";
		}else {
			return "borrow_apply_result";
		}
	}
	
	/**
	 * 提交标的相关信息
	 * @param bidRequest
	 * @return
	 */
	@RequestMapping("borrow_apply")
	public String borrowApply(BidRequest bidRequest) {
		this.bidRequestService.applyBidRequest(bidRequest);
		return "redirect:/borrowInfo.do";
	}
	
	//投资列表中 选中一个标，查看所选中标的详细信息
	@RequestMapping("/borrow_info")
	public String borrowInfoDetail(Long id,Model model) {
		//当前选中的是哪一个标
		BidRequest bidRequest = this.bidRequestService.get(id);
		model.addAttribute("bidRequest", bidRequest);
		
		//当前查看的标是哪一个用户发出来的
		UserInfo bidRequestOwner = this.userInfoService.get(bidRequest.getCreateUser().getId());
		model.addAttribute("userInfo", bidRequestOwner);
		model.addAttribute("realAuth", this.realAuthService.getRealAuth(bidRequestOwner.getRealAuthId()));
		model.addAttribute("userFiles", this.userFileService.queryUserFileList(bidRequestOwner.getId(), false));
		//如果当前没有用户登录，只是查看标的状态，这时候前台显示的是“登录投标”
		if(UserContext.getCurrentUser() == null) {
			model.addAttribute("self", false);
		}else {
			//如果已经登录的用户查看的标是自己发出的标，就显示“投标中”
			if(bidRequestOwner.getId().equals(UserContext.getCurrentUser().getId())) {
				model.addAttribute("self", true);
			}else {
				//如果已经登录的用户查看的标不是自己发出的标，就显示“马上投标”
				//根据当前登录用户得到该用户的账户信息
				Account account = this.accountService.get(UserContext.getCurrentUser().getId());
				model.addAttribute("self",false);
				model.addAttribute("account", account);
			}
		}
		return "borrow_info";
	}
}
