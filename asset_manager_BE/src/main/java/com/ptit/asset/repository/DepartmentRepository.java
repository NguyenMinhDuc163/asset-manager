package com.ptit.asset.repository;

import com.ptit.asset.domain.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;

@Repository
public interface DepartmentRepository extends JpaRepository <Department,Long> {

    Page<Department> findAll(Pageable pageable);

}
