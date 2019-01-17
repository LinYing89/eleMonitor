package com.bairock.eleMonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bairock.eleMonitor.data.Place;

public interface PlaceRepo extends JpaRepository<Place, Long> {

}
