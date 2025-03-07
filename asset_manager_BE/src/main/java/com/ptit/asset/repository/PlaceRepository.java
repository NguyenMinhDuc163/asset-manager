package com.ptit.asset.repository;

import com.ptit.asset.domain.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository <Place,Long> {

    Page<Place> findAll(Pageable pageable);

}
