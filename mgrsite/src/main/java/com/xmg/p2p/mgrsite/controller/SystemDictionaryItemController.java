package com.xmg.p2p.mgrsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.SystemDictionaryItemQueryObject;
import com.xmg.p2p.base.service.ISystemDictionaryItemService;
import com.xmg.p2p.base.service.ISystemDictionaryService;
/**
 * 数据字典明细
 * @author Dxd
 *
 */
@Controller
public class SystemDictionaryItemController {
	@Autowired
	private ISystemDictionaryItemService systemDictionaryItemService;
	@Autowired
	private ISystemDictionaryService systemDictionaryService;
	
	/**
	 * 高级查询
	 * @param qo
	 * @param model
	 * @return
	 */
	@RequestMapping("systemDictionaryItem_list")
	public String systemDictionaryItemList(@ModelAttribute("qo")SystemDictionaryItemQueryObject qo,Model model) {
		PageResult pageResult = systemDictionaryItemService.query(qo);
		model.addAttribute("pageResult", pageResult);
		//数据字典分组
		model.addAttribute("systemDictionaryGroups",this.systemDictionaryService.listAllDics());
		return "systemdic/systemDictionaryItem_list";
	}
	/**
	 * 新增或更新
	 * @param item
	 * @return
	 */
	@RequestMapping("/systemDictionaryItem_update")
	public String saveOrUpdate(SystemDictionaryItem item) {
		if(item.getId() != null) {
			systemDictionaryItemService.update(item);
		}else {
			systemDictionaryItemService.insert(item);
		}
		return "redirect:systemDictionaryItem_list.do?parentId="+item.getParentId();
	}
	
	@RequestMapping("/systemDictionaryItem_get")
	@ResponseBody
	public SystemDictionaryItem getSystemDictionaryItem(Long id) {
		return systemDictionaryItemService.get(id);
	}
}
