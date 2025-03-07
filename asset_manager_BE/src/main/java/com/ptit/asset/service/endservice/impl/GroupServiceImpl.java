package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.Group;
import com.ptit.asset.dto.request.GroupCreateRequestDTO;
import com.ptit.asset.service.endservice.GroupService;
import com.ptit.asset.service.manager.GroupManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupManagement groupManagement;


    @Override
    public Try<Group> getOne(Long id) {
        return groupManagement.getOne(id);
    }

    @Override
    public Try<Group> create(GroupCreateRequestDTO dto) {
        return groupManagement.create(dto);
    }

    @Override
    public Try<Group> update(Group group) {
        return groupManagement.update(group);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return groupManagement.delete(id);
    }

    @Override
    public List<Group> fetchAll() {
        return groupManagement.fetchAll();
    }
}
