package com.ptit.asset.repository;

import com.ptit.asset.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository <Category,Long> {

    Page<Category> findAll(Pageable pageable);

    List<Category> findAllByGroupId(Long id);

}
