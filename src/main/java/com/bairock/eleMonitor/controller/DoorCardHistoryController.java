package com.bairock.eleMonitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bairock.eleMonitor.data.DoorCardHistory;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.service.DoorCardHistoryService;
import com.bairock.eleMonitor.service.SubstationService;

@Controller
@RequestMapping("/doorCardHistory")
public class DoorCardHistoryController {

	@Autowired
	private DoorCardHistoryService doorCardHistoryService;
	@Autowired
	private SubstationService substationService;
	
	/**
	 * 
	 * @param substationId
	 * @param page 从1开始
	 * @param model
	 * @return
	 */
	@GetMapping("/history/{substationId}/{page}")
	public String getDoorCardHistory(@PathVariable long substationId, @PathVariable int page, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);
		int pageCount = doorCardHistoryService.getPageCount();
		int[] pageCountArry = new int[pageCount];
		for(int i = 0; i < pageCount; i++) {
			pageCountArry[i] = i + 1;
		}
		List<DoorCardHistory> list = doorCardHistoryService.getDoorCardHistory(substationId, (page - 1) * 20);
		model.addAttribute("substation", substation);
		model.addAttribute("pageCountArry", pageCountArry);
		model.addAttribute("page", page);
		model.addAttribute("cardNum", "0");
		model.addAttribute("listDoorCardHistory", list);
		return "card/cardHistory";
	}
	
	@GetMapping("/history/{substationId}/{page}/{cardNum}")
	public String getDoorCardHistoryByCardNum(@PathVariable long substationId, @PathVariable int page, @PathVariable String cardNum, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);
		int pageCount = 0;
		List<DoorCardHistory> list;
		//cardNum为空表示全部纪录, 不为空表示搜索卡号的纪录
		if(cardNum.isEmpty() || cardNum.equals("0")) {
			pageCount = doorCardHistoryService.getPageCount();
			list = doorCardHistoryService.getDoorCardHistory(substationId, (page - 1) * 20);
		}else {
			pageCount = doorCardHistoryService.getPageCountByCardNum(cardNum);
			list = doorCardHistoryService.getDoorCardHistoryByCardNum(substationId, cardNum, (page - 1) * 20);
		}
		int[] pageCountArry = new int[pageCount];
		for(int i = 0; i < pageCount; i++) {
			pageCountArry[i] = i + 1;
		}
		
		model.addAttribute("substation", substation);
		model.addAttribute("pageCountArry", pageCountArry);
		model.addAttribute("page", page);
		model.addAttribute("cardNum", cardNum);
		model.addAttribute("listDoorCardHistory", list);
		return "card/cardHistory";
	}
}
