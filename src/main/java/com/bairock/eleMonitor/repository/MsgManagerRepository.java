package com.bairock.eleMonitor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bairock.eleMonitor.data.MsgManager;

/**
 * 通信管理机
 * @author 44489
 *
 */
public interface MsgManagerRepository extends JpaRepository<MsgManager, Long> {

	Optional<MsgManager> findByCode(int code);
}
