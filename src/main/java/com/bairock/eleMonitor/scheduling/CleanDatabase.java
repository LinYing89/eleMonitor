package com.bairock.eleMonitor.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bairock.eleMonitor.repository.DeviceEventMessageRepository;
import com.bairock.eleMonitor.repository.DeviceValueHistoryRepository;

@Component
public class CleanDatabase {
	
	@Autowired
	private DeviceEventMessageRepository deviceEventMessageRepository;
	@Autowired
	private DeviceValueHistoryRepository deviceValueHistoryRepository;

//	private static final Logger logger = LoggerFactory.getLogger(CleanDatabase.class);
	 
	//每天23点清理数据库
    @Scheduled(cron="0 0 23 * * ?") 
    public void executeCleanDatabase() {
//        logger.info("executeCleanDatabase");
        
        //删除七天前的数据
        deviceEventMessageRepository.removeBeforeSevenDay();
        deviceValueHistoryRepository.removeBeforeSevenDay();
    }
}
