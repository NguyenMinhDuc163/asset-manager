package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.Organization;
import com.ptit.asset.dto.request.FetchPageOrganizationRequestDTO;
import com.ptit.asset.dto.request.OrganizationCreateRequestDTO;
import com.ptit.asset.service.endservice.OrganizationService;
import com.ptit.asset.service.manager.OrganizationManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationManagement organizationManagement;

    @Override
    public Try<Organization> getOne(Long id) {
        return organizationManagement.getOne(id);
    }

    @Override
    public Try<Organization> create(OrganizationCreateRequestDTO dto) {
        return organizationManagement.create(dto);
    }

    @Override
    public Try<Organization> update(Organization organization) {
        return organizationManagement.update(organization);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return organizationManagement.delete(id);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    @Override
    public List<Organization> fetchAll() {
        return organizationManagement.fetchAll();
    }

    @Override
    public Page<Organization> fetchPage(FetchPageOrganizationRequestDTO dto) {
        return organizationManagement.fetchPage(dto);
    }
}
