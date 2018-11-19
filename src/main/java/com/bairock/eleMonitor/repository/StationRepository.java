package com.bairock.eleMonitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bairock.eleMonitor.data.Station;

/**
 * 站点
 * @author 44489
 *
 */
public interface StationRepository extends JpaRepository<Station, Long> {

	List<Station> findAllByUserId(long id);
}
