package com.bairock.eleMonitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.DoorCardHistory;
import com.bairock.eleMonitor.repository.DoorCardHistoryRepository;

@Service
public class DoorCardHistoryService {
	
	@Autowired
	private DoorCardHistoryRepository doorCardHistoryRepository;

	/**
	 * 获取总页数
	 * @return
	 */
	public int getPageCount() {
		long count = doorCardHistoryRepository.count();
		int page = (int) (count / 20);
		if(page == 0) {
			page = 1;
		}else if(count % 20 != 0) {
			page += 1;
		}
		return page;
	}
	
	/**
	 * 获取某卡号的总页数
	 * @return
	 */
	public int getPageCountByCardNum(String cardNum) {
		long count = doorCardHistoryRepository.countByCardNum(cardNum);
		int page = (int) (count / 20);
		if(page == 0) {
			page = 1;
		}else if(count % 20 != 0) {
			page += 1;
		}
		return page;
	}
	
	/**
	 * 倒叙查询, 从第limitStart(从0开始)行开始的20条数据
	 * @param substationId 变电站id
	 * @param limitStart 开始行数
	 * @return
	 */
	public List<DoorCardHistory> getDoorCardHistory(long substationId, int limitStart){
		return doorCardHistoryRepository.findHistory(substationId, limitStart);
	}
	
	public List<DoorCardHistory> getDoorCardHistoryByCardNum(long substationId, String cardNum, int limitStart){
		if(cardNum.isEmpty() || cardNum.equals("0")) {
			return doorCardHistoryRepository.findHistory(substationId, limitStart);
		}else {
			return doorCardHistoryRepository.findHistoryByCardNum(substationId, cardNum, limitStart);
		}
	}
}
