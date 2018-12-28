package com.bairock.eleMonitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bairock.eleMonitor.data.DoorCardHistory;

public interface DoorCardHistoryRepository extends JpaRepository<DoorCardHistory, Long> {

	/**
	 * 卡号为cardNum的总条数
	 * @param cardNum
	 * @return
	 */
	long countByCardNum(String cardNum);
	
	/**
	 * 倒叙查询, 从第limitStart(从0开始)开始的20条数据
	 * @param limitStart
	 * @return
	 */
	@Query(value="select * from door_card_history where substation_id=:substationId order by id desc limit :limitStart,20", nativeQuery=true)
	List<DoorCardHistory> findHistory(@Param("substationId") long substationId, @Param("limitStart") int limitStart);
	
	/**
	 * 从id为startId开始, 倒叙查询卡号为cardNum20条数据
	 * @param substationId
	 * @param cardNum
	 * @param startId
	 * @return
	 */
	@Query(value="select * from door_card_history where substation_id=:substationId and card_num=:cardNum order by id desc limit :limitStart,20", nativeQuery=true)
	List<DoorCardHistory> findHistoryByCardNum(@Param("substationId") long substationId, @Param("cardNum") String cardNum, @Param("limitStart") long limitStart);
}
