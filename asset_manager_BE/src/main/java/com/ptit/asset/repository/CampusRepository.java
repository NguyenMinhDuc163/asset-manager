package com.ptit.asset.repository;

import com.ptit.asset.domain.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampusRepository extends JpaRepository <Campus,Long> {

}
