package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.User;
import com.ptit.asset.dto.request.*;
import com.ptit.asset.dto.response.AuthResponseDTO;
import com.ptit.asset.service.endservice.UserService;
import com.ptit.asset.service.manager.UserManagement;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserManagement userManagement;

    @Override
    public Try<AuthResponseDTO> login(LoginRequestDTO dto) {
        return userManagement.login(dto);
    }

    @Override
    public Try<AuthResponseDTO> register(RegisterRequestDTO dto) {
        return userManagement.register(dto);
    }

    @Override
    public Try<Boolean> resetPassword(ResetPasswordRequestDTO dto) {
        return userManagement.resetPassword(dto);
    }

    @Override
    public Try<User> getOne(Long id) {
        return userManagement.getOne(id);
    }

    @Override
    public Try<User> update(UserUpdateRequestDTO dto) {
        return userManagement.update(dto);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    @Override
    public List<User> fetchAll() {
        return userManagement.fetchAll();
    }

    @Override
    public Page<User> fetchPage(FetchPageUserRequestDTO dto) {
        return userManagement.fetchPage(dto);
    }
}
