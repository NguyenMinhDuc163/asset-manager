package com.ptit.asset.service.endservice;

import com.ptit.asset.domain.Department;
import com.ptit.asset.dto.request.DepartmentCreateRequestDTO;
import com.ptit.asset.dto.request.FetchPageDepartmentRequestDTO;
import io.vavr.control.Try;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService {

    Try<Department> getOne(Long id);

    Try<Department> create(DepartmentCreateRequestDTO dto);

    Try<Department> update(Department department);

    Try<Boolean> delete(Long id);

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    List<Department> fetchAll();

    Page<Department> fetchPage(FetchPageDepartmentRequestDTO dto);

}
