package com.ptit.asset.service.manager;

import com.ptit.asset.domain.Group;
import com.ptit.asset.dto.request.GroupCreateRequestDTO;
import io.vavr.control.Try;

import java.util.List;

public interface GroupManagement {

    Try<Group> getOne(Long id);

    Try<Group> create(GroupCreateRequestDTO dto);

    Try<Group> update(Group group);

    Try<Boolean> delete(Long id);

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    List<Group> fetchAll();

}
