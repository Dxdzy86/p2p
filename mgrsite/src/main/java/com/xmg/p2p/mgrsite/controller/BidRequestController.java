package com.xmg.p2p.mgrsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.domain.UserInfo;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.service.IUserInfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.domain.BidRequestAuditHistory;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.service.IBidRequestAuditHistoryService;
import com.xmg.p2p.business.service.IBidRequestService;
/**
 * 后台标的审核相关
 * @author Dxd
 *
 */
@Controller
public class BidRequestController {

	@Autowired
	private IBidRequestService bidRequestService;
	@Autowired
	private IBidRequestAuditHistoryService bidRequestAuditHistory;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private IRealAuthService realAuthService;
	@Autowired
	private IUserFileService userFileService;
	/**
	 * 发标前审核
	 * @return
	 */
	@RequestMapping("/bidrequest_publishaudit_list")
	public String bidrequestPublishAuditList(@ModelAttribute("qo")BidRequestQueryObject qo,Model model) {
		//这里一定要设置后台查询标的状态，因为后台只能对(待发布的标,满标一审、满标二审)进行审核
		qo.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);
		PageResult pageResult = this.bidRequestService.query(qo);
		model.addAttribute("pageResult",pageResult);
		return "bidrequest/publish_audit";
	}
	
	/**
	 * 后台发表前审核通过还是拒绝
	 * @param id
	 * @param state
	 * @param remark
	 * @return
	 */
	@RequestMapping("/bidrequest_publishaudit")
	@ResponseBody
	public JSONResult bidrequestPublishAudit(Long id,int state,String remark) {
		JSONResult result = new JSONResult();
		try {
			this.bidRequestService.publishAudit(id,state,remark);
			result.setSuccess(true);
		}catch(Exception e) {
			result.setSuccess(false);
			result.setMsg("审核失败");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 点击标的借款标题，去借款详情页面查看标的详细借款信息
	 */
	@RequestMapping("/borrow_info")
	public String borrowInfo(Long id,Model model) {
		//当前标的BidRequest对象回显到前台
		BidRequest bidRequest = this.bidRequestService.get(id);
		model.addAttribute("bidRequest", bidRequest);
		
		//标的审核历史信息
		List<BidRequestAuditHistory> audits = this.bidRequestAuditHistory.queryAuditHistoryByBidRequestId(id);
		model.addAttribute("audits",audits);
		
		//借款人的实名认证信息
		UserInfo userInfo = this.userInfoService.get(bidRequest.getCreateUser().getId());//当前标属于哪一个用户的用户基本信息 
		RealAuth realAuth = realAuthService.getRealAuth(userInfo.getRealAuthId());
		model.addAttribute("realAuth", realAuth);
		model.addAttribute("userInfo", userInfo);
		
		//风控审核材料
		model.addAttribute("userFiles", this.userFileService.queryUserFileList(userInfo.getId(), false));
		return "bidrequest/borrow_info";
	}
	
	/**
	 * 处于满标一审状态的借款标的列表
	 */
	@RequestMapping("/bidrequest_audit1_list")
	public String bidrequestAudit1List(@ModelAttribute("qo")BidRequestQueryObject qo,Model model) {
		//这里一定要设置后台查询标的状态，因为后台只能对(待发布的标,满标一审、满标二审)进行审核
		qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
		PageResult pageResult = this.bidRequestService.query(qo);
		model.addAttribute("pageResult", pageResult);
		return "bidrequest/audit1";
	}
	
	/**
	 * 后台进行满标一审审核
	 * @param id
	 * @param state
	 * @param remark
	 * @return
	 */
	@RequestMapping("/bidrequest_audit1")
	@ResponseBody
	public JSONResult bidrequestAudit1(Long id,int state,String remark) {
		JSONResult result = new JSONResult();
		try{
			this.bidRequestService.fullAudit1(id,state,remark);
		}catch(Exception e) {
			result.setMsg("满标一审失败");
			result.setSuccess(false);
		}
		
		return result;
	}
	
	/**
	 * 后台处于满标二审状态的列表
	 */
	@RequestMapping("/bidrequest_audit2_list")
	public String bidrequestAudit2List(@ModelAttribute("qo")BidRequestQueryObject qo,Model model) {
		//给高级查询设置要查询的标的状态
		qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
		PageResult pageResult = this.bidRequestService.query(qo);
		model.addAttribute("pageResult",pageResult);
		return "bidrequest/audit2";
	}
	
	/**
	 * 进行满标二审审核
	 * @param id
	 * @param state
	 * @param remark
	 * @return
	 */
	@RequestMapping("/bidrequest_audit2")
	@ResponseBody
	public JSONResult bidrequestAudit2(Long id,int state,String remark) {
		JSONResult result = new JSONResult();
		try {
			this.bidRequestService.fullAudit2(id,state,remark);
		}catch(Exception e) {
			result.setSuccess(false);
			result.setMsg("满标二审失败");
		}
		
		return result;
	}
}
