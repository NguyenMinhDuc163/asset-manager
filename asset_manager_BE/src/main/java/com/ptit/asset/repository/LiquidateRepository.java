package com.ptit.asset.repository;

import com.ptit.asset.domain.Liquidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiquidateRepository extends JpaRepository <Liquidate,Long> {

}
