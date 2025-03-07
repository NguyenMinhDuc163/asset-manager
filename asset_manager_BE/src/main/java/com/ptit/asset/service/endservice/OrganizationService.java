package com.ptit.asset.service.endservice;

import com.ptit.asset.domain.Organization;
import com.ptit.asset.dto.request.FetchPageOrganizationRequestDTO;
import com.ptit.asset.dto.request.OrganizationCreateRequestDTO;
import io.vavr.control.Try;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrganizationService {

    Try<Organization> getOne(Long id);

    Try<Organization> create(OrganizationCreateRequestDTO dto);

    Try<Organization> update(Organization organization);

    Try<Boolean> delete(Long id);

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    List<Organization> fetchAll();

    Page<Organization> fetchPage(FetchPageOrganizationRequestDTO dto);

}
