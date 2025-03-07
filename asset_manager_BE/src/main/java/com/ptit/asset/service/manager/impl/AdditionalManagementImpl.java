package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Additional;
import com.ptit.asset.domain.Liquidate;
import com.ptit.asset.dto.request.AdditionalChangeStatusRequestDTO;
import com.ptit.asset.dto.request.AdditionalCreateRequestDTO;
import com.ptit.asset.dto.request.AdditionalUpdateRequestDTO;
import com.ptit.asset.repository.AdditionalRepository;
import com.ptit.asset.repository.OrganizationRepository;
import com.ptit.asset.repository.UserRepository;
import com.ptit.asset.service.manager.AdditionalManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class AdditionalManagementImpl implements AdditionalManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private AdditionalRepository additionalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationRepository organizationRepository;


    @Override
    public Try<Additional> getOne(Long id) {
        return Try.of(() -> additionalRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get additional by id: "+id)));
    }

    @Override
    public Try<Additional> create(AdditionalCreateRequestDTO dto) {
        val user = userRepository.findById(dto.getEmbedded().getUserId());
        if (!user.isPresent()){
            return Try.failure(new Exception("Failure when find user by id: "+dto.getEmbedded().getUserId()));
        }
        val organization = organizationRepository.findById(dto.getEmbedded().getOrganizationId());
        if (!organization.isPresent()){
            return Try.failure(new Exception("Failure when find organization by id: "+dto.getEmbedded().getOrganizationId()));
        }

        String confirm = validationData();
        if (!confirm.equalsIgnoreCase("ACCEPTABLE")){
            return Try.failure(new Exception(confirm));
        }

        val additional = centralMapper.toAdditional(dto, user.get(), organization.get());
        additional.setInProcess(true);

        return Try.of(() -> additionalRepository.save(additional))
            .orElse(() -> Try.failure(new Exception("Failure when save additional")));
    }

    @Override
    public Try<Additional> update(AdditionalUpdateRequestDTO dto) {
        val additionalResult = additionalRepository.findById(dto.getId());
        if (!additionalResult.isPresent()){
            return Try.failure(new Exception("Failure when find additional to update with id: "+dto.getId()));
        }

        val additionalUpdate = centralMapper.toAdditionalUpdate(additionalResult.get(),dto);

        // relationship space
        if (dto.getEmbedded() != null){

            val userId = dto.getEmbedded().getUserId();
            if (userId != null && !userId.equals(additionalUpdate.getUser().getId())){
                val user = userRepository.findById(userId);
                user.ifPresent(additionalUpdate::setUser);
            }

            val organizationId = dto.getEmbedded().getOrganizationId();
            if (organizationId != null && !organizationId.equals(additionalUpdate.getOrganization().getId())){
                val organization = organizationRepository.findById(organizationId);
                organization.ifPresent(additionalUpdate::setOrganization);
            }
        }
        // relationship space

        return Try.of(() -> additionalRepository.save(additionalUpdate))
            .orElse(() -> Try.failure(new Exception("Failure when update additional")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            additionalRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete additional with id: "+id)));
    }

    @Override
    public List<Additional> fetchAll() {
        return additionalRepository.findAll();
    }


    @Override
    public Try<Boolean> changeStatus(AdditionalChangeStatusRequestDTO dto) {
        val additional = additionalRepository.findById(dto.getId());
        if (!additional.isPresent()){
            return Try.failure(new Exception("Failure when find additional with id:"+dto.getId()));
        }

        Additional additionalUpdate = additional.get();
        // logic space
        if (dto.getStatus().equals(additionalUpdate.getInProcess())){
            return Try.failure(new Exception("Nothing change with current status"));
        }

        if (dto.getStatus()){
            return Try.failure(new Exception("Additional with in process has been created when init - not allow turn back"));
        }
        // logic space

        additionalUpdate.setInProcess(dto.getStatus());
        return Try.of(() -> {
            additionalRepository.save(additionalUpdate);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when change status of additional")));


    }

    // -----------------------------------------------------------------------------------------------------------------
    protected String validationData(){
        List<Additional> additionalList = additionalRepository.findAll();
        for (Additional additional : additionalList){
            if (additional.getInProcess()){
                return "Already existing additional in process";
            }
        }
        return "ACCEPTABLE";
    }
}
