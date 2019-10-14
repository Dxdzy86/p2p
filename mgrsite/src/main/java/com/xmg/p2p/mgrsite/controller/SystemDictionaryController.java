package com.xmg.p2p.mgrsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmg.p2p.base.domain.SystemDictionary;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.SystemDictionaryQueryObject;
import com.xmg.p2p.base.service.ISystemDictionaryService;
/**
 * 系统数据字典
 * @author Dxd
 *
 */
@Controller
public class SystemDictionaryController {
	@Autowired
	private ISystemDictionaryService systemDictionaryService;
	
	/**
	 * 显示数据字典页面
	 * @return
	 */
	@RequestMapping("/systemDictionary_list")
	public String systemDictionaryList(@ModelAttribute("qo")SystemDictionaryQueryObject qo,Model model) {
		PageResult pageResult = systemDictionaryService.query(qo);
		model.addAttribute("pageResult",pageResult);
		
		return "systemdic/systemDictionary_list";
	}
	
	/**
	 * 数据字典的新增或者更新
	 * @param systemDictionary
	 * @return
	 */
	@RequestMapping("/systemDictionary_saveOrUpdate")
	public String saveOrUpdate(SystemDictionary systemDictionary) {
		try {
			if(systemDictionary.getId() != null) {
				//更新操作
				systemDictionaryService.update(systemDictionary);
			}else {
				//新增操作
				systemDictionaryService.insert(systemDictionary);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "systemdic/systemDictionary_list";
	}
	/**
	 * 根据id查询数据字典
	 * @param id
	 * @return
	 */
	@RequestMapping("/systemDictionary_getDicById")
	@ResponseBody
	public SystemDictionary getDicById(Long id) {
		SystemDictionary systemDictionary = systemDictionaryService.getDicById(id);
		return systemDictionary;
	}
	
	@RequestMapping("/systemDictionary_deleteById")
	public String deleteById(Long id) {
		systemDictionaryService.delete(id);
		return "systemdic/systemDictionary_list";
	}
}
