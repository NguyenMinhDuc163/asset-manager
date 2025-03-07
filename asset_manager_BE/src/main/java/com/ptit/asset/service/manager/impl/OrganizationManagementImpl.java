package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Organization;
import com.ptit.asset.dto.request.FetchPageOrganizationRequestDTO;
import com.ptit.asset.dto.request.OrganizationCreateRequestDTO;
import com.ptit.asset.repository.OrganizationRepository;
import com.ptit.asset.service.manager.OrganizationManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationManagementImpl implements OrganizationManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private OrganizationRepository organizationRepository;


    @Override
    public Try<Organization> getOne(Long id) {
        return Try.of(() -> organizationRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get organization by id: "+id)));
    }

    @Override
    public Try<Organization> create(OrganizationCreateRequestDTO dto) {
        return Try.of(() -> {
           val organization = centralMapper.toOrganization(dto);
            return organizationRepository.save(organization);
        }).orElse(() -> Try.failure(new Exception("Failure when save organization")));
    }

    @Override
    public Try<Organization> update(Organization organization) {
        val organizationResult = organizationRepository.findById(organization.getId());
        if (! organizationResult.isPresent()){
            return Try.failure(new Exception("Failure when find organization to update with id: "+organization.getId()));
        }
        val organizationUpdate = centralMapper.toOrganizationUpdate(organizationResult.get(),organization);
        return Try.of(() -> organizationRepository.save(organizationUpdate))
            .orElse(() -> Try.failure(new Exception("Failure when update organization")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            organizationRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete organization with id: "+id)));
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @Override
    public List<Organization> fetchAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Page<Organization> fetchPage(FetchPageOrganizationRequestDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage(),dto.getSize());
        return organizationRepository.findAll(pageable);
    }
}
