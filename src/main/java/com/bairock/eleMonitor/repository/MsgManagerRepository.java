package com.bairock.eleMonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bairock.eleMonitor.data.MsgManager;

/**
 * 通信管理机
 * @author 44489
 *
 */
public interface MsgManagerRepository extends JpaRepository<MsgManager, Long> {

}
