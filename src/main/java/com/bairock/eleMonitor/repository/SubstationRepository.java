package com.bairock.eleMonitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bairock.eleMonitor.data.Substation;

/**
 * 变电站
 * @author 44489
 *
 */
public interface SubstationRepository extends JpaRepository<Substation, Long> {

	List<Substation> findByStationId(long stationId);
}
