package com.ptit.asset.repository;

import com.ptit.asset.domain.Role;
import com.ptit.asset.domain.enumeration.RoleName;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository <Role,Long> {

    Option<Role> findByRoleName(RoleName roleName);

}
