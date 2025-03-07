package com.ptit.asset.repository;

import com.ptit.asset.domain.Additional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalRepository extends JpaRepository <Additional,Long> {

}
