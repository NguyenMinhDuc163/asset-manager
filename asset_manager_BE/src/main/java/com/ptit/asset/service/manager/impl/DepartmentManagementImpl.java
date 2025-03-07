package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Department;
import com.ptit.asset.dto.request.DepartmentCreateRequestDTO;
import com.ptit.asset.dto.request.FetchPageDepartmentRequestDTO;
import com.ptit.asset.repository.DepartmentRepository;
import com.ptit.asset.service.manager.DepartmentManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentManagementImpl implements DepartmentManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private DepartmentRepository departmentRepository;


    @Override
    public Try<Department> getOne(Long id) {
        return Try.of(() -> departmentRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get department by id: "+id)));
    }

    @Override
    public Try<Department> create(DepartmentCreateRequestDTO dto) {
        return Try.of(() -> {
            val department = centralMapper.toDepartment(dto);
            return departmentRepository.save(department);
        }).orElse(() -> Try.failure(new Exception("Failure when save department")));
    }

    @Override
    public Try<Department> update(Department department) {
        val departmentResult = departmentRepository.findById(department.getId());
        if (! departmentResult.isPresent()){
            return Try.failure(new Exception("Failure when find department to update with id: "+department.getId()));
        }
        val departmentUpdate = centralMapper.toDepartmentUpdate(departmentResult.get(), department);
        return Try.of(() -> departmentRepository.save(departmentUpdate))
            .orElse(() -> Try.failure(new Exception("Failure when update department")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            departmentRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete department with id: "+id)));
    }


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //


    @Override
    public List<Department> fetchAll() {
        return departmentRepository.findAll(Sort.by("name").ascending());
    }

    @Override
    public Page<Department> fetchPage(FetchPageDepartmentRequestDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage(),dto.getSize());
        return departmentRepository.findAll(pageable);
    }
}
