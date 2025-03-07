package com.ptit.asset.repository;

import com.ptit.asset.domain.CalculationUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationUnitRepository extends JpaRepository <CalculationUnit,Long> {

}
