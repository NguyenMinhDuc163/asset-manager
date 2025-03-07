package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.Department;
import com.ptit.asset.dto.request.DepartmentCreateRequestDTO;
import com.ptit.asset.dto.request.FetchPageDepartmentRequestDTO;
import com.ptit.asset.service.endservice.DepartmentService;
import com.ptit.asset.service.manager.DepartmentManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentManagement departmentManagement;

    @Override
    public Try<Department> getOne(Long id) {
        return departmentManagement.getOne(id);
    }

    @Override
    public Try<Department> create(DepartmentCreateRequestDTO dto) {
        return departmentManagement.create(dto);
    }

    @Override
    public Try<Department> update(Department department) {
        return departmentManagement.update(department);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return departmentManagement.delete(id);
    }


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @Override
    public List<Department> fetchAll() {
        return departmentManagement.fetchAll();
    }

    @Override
    public Page<Department> fetchPage(FetchPageDepartmentRequestDTO dto) {
        return departmentManagement.fetchPage(dto);
    }
}
