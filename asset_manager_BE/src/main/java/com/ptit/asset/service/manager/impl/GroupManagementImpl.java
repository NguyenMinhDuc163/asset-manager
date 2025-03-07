package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Group;
import com.ptit.asset.dto.request.GroupCreateRequestDTO;
import com.ptit.asset.repository.GroupRepository;
import com.ptit.asset.service.manager.GroupManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupManagementImpl implements GroupManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private GroupRepository groupRepository;


    @Override
    public Try<Group> getOne(Long id) {
        return Try.of(() -> groupRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get group by id: "+id)));
    }

    @Override
    public Try<Group> create(GroupCreateRequestDTO dto) {
        return Try.of(() -> {
            val group = centralMapper.toGroup(dto);
            return groupRepository.save(group);
        }).orElse(() -> Try.failure(new Exception("Failure when save group")));
    }

    @Override
    public Try<Group> update(Group group) {
        val groupResult = groupRepository.findById(group.getId());
        if (!groupResult.isPresent()){
            return Try.failure(new Exception("Failure when find group to update with id: "+group.getId()));
        }
        val groupUpdate = centralMapper.toGroupUpdate(groupResult.get(), group);
        return Try.of(() -> groupRepository.save(groupUpdate))
            .orElse(() -> Try.failure(new Exception("Failure when update group")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            groupRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete group with id: "+id)));
    }

    @Override
    public List<Group> fetchAll() {
        return groupRepository.findAll();
    }
}
