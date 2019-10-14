package com.xmg.p2p.base.controller;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.domain.UserInfo;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.service.IUserInfoService;
import com.xmg.p2p.base.util.UploadUtil;
import com.xmg.p2p.base.util.UserContext;
/**
 * 实名认证
 * @author Dxd
 *
 */
@Controller
public class RealAuthController {
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private IRealAuthService realAuthService;
	@Autowired
	private ServletContext servletContext;
	
	/**
	 * 跳转身份认证页面(可能没有审核，或者正在审核，或者已经审核通过)
	 * @param model
	 * @return
	 */
	@RequestMapping("/realAuth")
	public String realAuthIndex(Model model) {
		//得到userInfo对象
		UserInfo userInfo = this.userInfoService.get(UserContext.getCurrentUser().getId());
		if(userInfo != null) {
			//用户已经绑定身份认证,说明进行了实名认证，跳转到实名认证页面
			if(userInfo.getHasIdentityAuth()) {
				RealAuth realAuth = this.realAuthService.getRealAuth(userInfo.getRealAuthId());
				model.addAttribute("realAuth",realAuth);
				model.addAttribute("auditing",false);
				return "realAuth_result";
			}else if(userInfo.getRealAuthId()!=null){//如果auditing=true,userInfo中的实名认证信息id不为空，说明是正在审核，跳转到正在审核页面
				model.addAttribute("auditing", true);
				return "realAuth_result";
			}else {
				//用户没有进行实名认证，需要跳转到实名认证页面
				return "realAuth";
			}
		}
		return "realAuth";
	}
	
	/**
	 * 上传文件
	 * @param file
	 * @return
	 */
	@RequestMapping("/realAuth_upload")
	@ResponseBody
	public String realAuthUpload(MultipartFile file) {
		String realPath = servletContext.getRealPath("/upload");//文件存放的绝对路径
		String fileName = UploadUtil.upload(file, realPath);//更改后的随机文件名
		return "/upload/"+fileName;
	}
	
	/**
	 * 提交认证
	 * @param model
	 * @return
	 */
	@RequestMapping("/realAuth_save")
	public String realAuth_save(RealAuth realAuth,Model model) {
		this.realAuthService.realAuthApply(realAuth);
		return "redirect:realAuth.do";
	}
}
