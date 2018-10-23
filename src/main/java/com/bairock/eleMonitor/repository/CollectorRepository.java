package com.bairock.eleMonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bairock.eleMonitor.data.Collector;

/**
 * 采集终端
 * @author 44489
 *
 */
public interface CollectorRepository extends JpaRepository<Collector, Long> {

}
