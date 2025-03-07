package com.ptit.asset.service.endservice;

import com.ptit.asset.domain.User;
import com.ptit.asset.dto.request.*;
import com.ptit.asset.dto.response.AuthResponseDTO;
import io.vavr.control.Try;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    // auth space
    Try<AuthResponseDTO> login(LoginRequestDTO dto);

    Try<AuthResponseDTO> register(RegisterRequestDTO dto);

    Try<Boolean> resetPassword(ResetPasswordRequestDTO dto);
    // auth space

    Try<User> getOne(Long id);

    Try<User> update(UserUpdateRequestDTO dto);

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    List<User> fetchAll();

    Page<User> fetchPage(FetchPageUserRequestDTO dto);

}
