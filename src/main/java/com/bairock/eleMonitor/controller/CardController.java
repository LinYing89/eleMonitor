package com.bairock.eleMonitor.controller;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bairock.eleMonitor.data.DoorAuthority;
import com.bairock.eleMonitor.data.DoorCard;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.service.DoorCardService;
import com.bairock.eleMonitor.service.SubstationService;

@Controller
@RequestMapping("/card")
public class CardController {

	@Autowired
	private SubstationService substationService;
	@Autowired
	private DoorCardService doorCardService;

	@GetMapping("/history")
	public String getDevices() {
		return "/card/cardHistory";
	}

	@GetMapping("/add/{substationId}")
	public String addCard(@PathVariable long substationId, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);
		model.addAttribute("substation", substation);
		return "/card/cardAdd";
	}

	@PostMapping("/add/{substationId}")
	public String addCardPost(@PathVariable long substationId, @ModelAttribute DoorCard doorCard) {
		doorCardService.addDoorCard(substationId, doorCard);
		return "redirect:/card/user/" + substationId;
	}

	@PostMapping("/edit/{doorCardId}")
	public String editCardPost(@PathVariable long doorCardId, @ModelAttribute DoorCard doorCard) {
		DoorCard dc = doorCardService.editDoorCard(doorCardId, doorCard);
		return "redirect:/card/user/" + dc.getSubstation().getId();
	}

//	@PostMapping("/edit/{doorCardId}")
//	public String editCardPost(@PathVariable long doorCardId, @ModelAttribute DoorCard doorCard) {
//		doorCardService.editDoorCard(doorCardId, doorCard);
//		return "redirect:/card/user/" + doorCard.getSubstation().getId();
//	}

	@GetMapping("/user/{substationId}")
	public String getUsers(@PathVariable long substationId, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);

		model.addAttribute("substation", substation);
		List<DoorCard> list = doorCardService.findBySubstationId(substationId);
		for (DoorCard dc : list) {
			Collections.sort(dc.getListDoorAuthority());
		}

		model.addAttribute("listDoorCard", list);
		return "/card/cardUsers";
	}

	@ResponseBody
	@GetMapping("/user/authority/{cardId}")
	public List<DoorAuthority> getDoorAuthority(@PathVariable long cardId) {
		List<DoorAuthority> listDoorAuthority = doorCardService.getDoorAuthority(cardId);
		Collections.sort(listDoorAuthority);
		return listDoorAuthority;
	}

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
