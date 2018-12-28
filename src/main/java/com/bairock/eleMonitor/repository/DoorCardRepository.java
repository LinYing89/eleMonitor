package com.bairock.eleMonitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bairock.eleMonitor.data.DoorCard;

public interface DoorCardRepository extends JpaRepository<DoorCard, Long> {

	List<DoorCard> findBySubstationId(long substationId);
}
