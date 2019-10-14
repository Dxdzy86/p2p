package com.xmg.p2p.base.controller;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.service.ISystemDictionaryItemService;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.util.UploadUtil;
import com.xmg.p2p.base.util.UserContext;

/**
 * 风控资料相关
 * @author Dxd
 *
 */
@Controller
public class UserFileController {
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private IUserFileService userFileService;
	
	@Autowired
	private ISystemDictionaryItemService systemDictionaryItemService;
	/**
	 * 根据风控资料是否已经被选择类型来显示对应的用户风控资料信息列表
	 * @return
	 */
	@RequestMapping("/userFile")
	public String userFileList(Model model) {
		//这里queryUserFileList第二个参数为true，说明是查询没有选择风控类型的材料
		List<UserFile> userFiles = this.userFileService.queryUserFileList(UserContext.getCurrentUser().getId(),true);
		//如果风控资料没有还没有选择分控资料类型，则跳转到"userFiles_commit.ftl".
		if(userFiles.size() > 0) {
			model.addAttribute("userFiles",userFiles);//前台风控资料信息显示
			model.addAttribute("fileTypes",this.systemDictionaryItemService.listItemByParentSn("userFileType"));
			return "userFiles_commit";
		}else {
			//如果风控资料已经选择过了分控资料类型，则跳转到"userFiles.ftl",下面第二个参数表明风控资料已经有类型了，可以按照类型查找
			userFiles = this.userFileService.queryUserFileList(UserContext.getCurrentUser().getId(), false);
			model.addAttribute("userFiles",userFiles);//前台风控资料信息显示
			return "userFiles";
		}
	}
	
	/**
	 * 接收上传的风控资料
	 */
	@RequestMapping("/userFileUpload")
	@ResponseBody
	public void userFileUpload(MultipartFile file) {
		String realPath = servletContext.getRealPath("/upload");//风控资料文件的存放路径
		String fileName = UploadUtil.upload(file, realPath);
		String relativePath = "/upload/" + fileName;//风控资料相对路径
		this.userFileService.applyUserFile(relativePath);
	}
	
	/**
	 * 接收前台设置的各个风控资料的id和风控资料的风控类型
	 */
	@RequestMapping("/userFile_selectType")
	public String userFile_selectType(Long[] id,Long[] fileType) {
		if(id != null && fileType != null && id.length == fileType.length) {
			this.userFileService.updateUserFileTypes(id,fileType);
		}
		return "redirect:/userFile.do";
	}
}
