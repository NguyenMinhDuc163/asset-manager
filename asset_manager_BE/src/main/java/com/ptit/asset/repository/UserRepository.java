package com.ptit.asset.repository;

import com.ptit.asset.domain.User;
import io.vavr.control.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User,Long> {

    Option<User> findByUsername(String username);

    Option<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

}
