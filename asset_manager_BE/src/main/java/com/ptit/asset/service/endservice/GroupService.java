package com.ptit.asset.service.endservice;

import com.ptit.asset.domain.Group;
import com.ptit.asset.dto.request.GroupCreateRequestDTO;
import io.vavr.control.Try;

import java.util.List;

public interface GroupService {

    Try<Group> getOne(Long id);

    Try<Group> create(GroupCreateRequestDTO dto);

    Try<Group> update(Group group);

    Try<Boolean> delete(Long id);

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    List<Group> fetchAll();

}
