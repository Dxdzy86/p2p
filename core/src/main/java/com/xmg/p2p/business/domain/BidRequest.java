package com.xmg.p2p.business.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.xmg.p2p.base.domain.BaseDomain;
import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.util.DecimalFormatUtil;

import lombok.Getter;
import lombok.Setter;
@Setter@Getter
public class BidRequest extends BaseDomain{
	private int	version;//乐观锁
	private	int	returnType = BidConst.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL;//还款方式 按月分期
	private	int	bidRequestType = BidConst.BIDREQUEST_TYPE_NORMAL;//借款类型
	private	int	bidRequestState = BidConst.BIDREQUEST_STATE_PUBLISH_PENDING;//标的状态
	private	BigDecimal bidRequestAmount = BidConst.SMALLEST_BIDREQUEST_AMOUNT;//借款金额
	private	BigDecimal currentRate;//年化利率
	private	BigDecimal minBidAmount = BidConst.SMALLEST_BID_AMOUNT;//最小投标金额
	private	int	monthes2Return;//借款期限
	private	int	bidCount=0;//投标次数
	private	BigDecimal totalRewardAmount = BidConst.ZERO;//总回报利率(总利息)
	private	BigDecimal currentSum = BidConst.ZERO;//当前已经被投了多少钱
	private	String title="";//借款主题
	private	String description="";//借款描述信息
	private	String note="";//风控评审意见
	private	Date disableDate;//标的截至日期
	private	int disableDays;//招标的天数
	private	LoginInfo createUser;//当前借款人
	private	List<Bid> bids = new ArrayList<>();//当前借款的投标记录
	private	Date applyTime;//标的申请时间
	private	Date publishTime;//标的发布时间

	//还剩下多少借款没借到
	public BigDecimal getRemainAmount() {
		return DecimalFormatUtil.formatBigDecimal(bidRequestAmount.subtract(currentSum), BidConst.DISP_SCALE);
	}
	
	//投款进度
	public BigDecimal getPersent() {
		return currentSum.divide(bidRequestAmount, BidConst.DISP_SCALE, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
	}
	
	public String getJsonString() {
		Map<String,Object> map = new HashMap<>();
		map.put("id", super.getId());
		map.put("username", createUser.getUsername());
		map.put("title", title);
		map.put("bidRequestAmount", bidRequestAmount);
		map.put("currentRate", currentRate);
		map.put("monthes2Return", monthes2Return);
		map.put("returnType",getReturnTypeDisplay());
		map.put("totalRewardAmount", totalRewardAmount);
		
		return JSONObject.toJSONString(map);
	}
	
	public String getReturnTypeDisplay() {
		return returnType == BidConst.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL?"按月分期":"按月到期";
	}
	
	public String getBidRequestStateDisplay() {
		switch(bidRequestState) {
			case BidConst.BIDREQUEST_STATE_PUBLISH_PENDING:return "待发布";
			case BidConst.BIDREQUEST_STATE_BIDDING:return "招标中";
			case BidConst.BIDREQUEST_STATE_UNDO:return "已撤销";
			case BidConst.BIDREQUEST_STATE_BIDDING_OVERDUE:return "流标";
			case BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1:return "满标一审";
			case BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2:return "满标二审";
			case BidConst.BIDREQUEST_STATE_REJECTED:return "满审被拒";
			case BidConst.BIDREQUEST_STATE_PAYING_BACK:return "还款中";
			case BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK:return "已还清";
			case BidConst.BIDREQUEST_STATE_PAY_BACK_OVERDUE:return "逾期";
			case BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE:return "发标被拒";
			default:
				return "状态未知，请联系管理员";
		}
	}
}    