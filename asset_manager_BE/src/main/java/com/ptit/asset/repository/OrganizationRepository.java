package com.ptit.asset.repository;

import com.ptit.asset.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository <Organization,Long> {

    Page<Organization> findAll(Pageable pageable);

}
