package com.ptit.asset.repository;

import com.ptit.asset.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository <Group,Long> {

}
