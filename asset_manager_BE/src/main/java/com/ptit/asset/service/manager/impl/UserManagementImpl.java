package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Department;
import com.ptit.asset.domain.Role;
import com.ptit.asset.domain.User;
import com.ptit.asset.domain.enumeration.RoleName;
import com.ptit.asset.dto.request.*;
import com.ptit.asset.dto.response.AuthResponseDTO;
import com.ptit.asset.repository.DepartmentRepository;
import com.ptit.asset.repository.RoleRepository;
import com.ptit.asset.repository.UserRepository;
import com.ptit.asset.security.UserPrinciple;
import com.ptit.asset.security.jwt.JwtProvider;
import com.ptit.asset.service.manager.UserManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserManagementImpl implements UserManagement {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CentralMapper centralMapper;



    // auth space
    @Override
    public Try<AuthResponseDTO> login(LoginRequestDTO dto) {
        // add check user is active before login
        val user = userRepository.findByUsername(dto.getUsername());
        if (user.isDefined()){
            if (!user.get().getActive()){
                return Try.failure(new Exception("User has been DE-ACTIVE account"));
            }
        }
        // add check user is active before login
        return Try.of(() -> {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateJwtToken(authentication);
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

            List<String> roles = userPrinciple.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

            return AuthResponseDTO.builder()
                .id(userPrinciple.getId())
                .fullName(userPrinciple.getFullName())
                .phone(userPrinciple.getPhone())
                .email(userPrinciple.getEmail())
                .username(userPrinciple.getUsername())
                .token(token)
                .roles(roles).build();

        }).orElse(() -> Try.failure(new Exception("Failure when authenticate login")));
    }

    @Override
    public Try<AuthResponseDTO> register(RegisterRequestDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isDefined()){
            return Try.failure(new Exception("Username already used!"));
        }
        if (userRepository.findByEmail(dto.getEmail()).isDefined()){
            return Try.failure(new Exception("Email already used!"));
        }

        // fix space
        Department department = null;
        if (dto.getEmbedded().getDepartmentId() != null){
            department = departmentRepository.findById(dto.getEmbedded().getDepartmentId()).get();
        }
//        val department = departmentRepository.findById(dto.getEmbedded().getDepartmentId());
//        if (!department.isPresent()){
//            return Try.failure(new Exception("Department not found"));
//        }
        // fix space

        Set<String> roleInDTO = dto.getRoles();
        Set<Role> roles = new HashSet<>();
        // roleInDTO need check before this process
        roleInDTO.forEach(
            role -> {
                switch (role){
                    case "ROLE_ADMIN":
                        Role role_ADMIN = roleRepository.findByRoleName(RoleName.ROLE_ADMIN).get();
                        if (role_ADMIN != null) roles.add(role_ADMIN);
                        break;
                    /*****/
                    case "ROLE_ACCOUNTANT":
                        Role role_ACCOUNTANT = roleRepository.findByRoleName(RoleName.ROLE_ACCOUNTANT).get();
                        if (role_ACCOUNTANT != null) roles.add(role_ACCOUNTANT);
                        break;
                    /*****/
                    case "ROLE_EMPLOYEE":
                        Role role_EMPLOYEE = roleRepository.findByRoleName(RoleName.ROLE_EMPLOYEE).get();
                        if (role_EMPLOYEE != null) roles.add(role_EMPLOYEE);
                        break;
                    /*****/
                    case "ROLE_INSPECTOR":
                        Role role_INSPECTOR = roleRepository.findByRoleName(RoleName.ROLE_INSPECTOR).get();
                        if (role_INSPECTOR != null) roles.add(role_INSPECTOR);
                        break;
                    /*****/
                }
            }
        );

        if (roles.isEmpty()){
            return Try.failure(new Exception("User register need assign role"));
        }
        User user = new User(
            dto.getFullName(),
                dto.getPhone(),
                dto.getEmail(),
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getActive(),
                department,
                roles
        );

        return Try.of(() -> {
            User userSaved = userRepository.save(user);
//            List<String> rolesDTO = userSaved.getRoles().stream()
//                    .map(element -> element.getRoleName().name()).collect(Collectors.toList());
            return AuthResponseDTO.builder()
            .fullName(userSaved.getFullName())
            .phone(userSaved.getPhone())
            .email(userSaved.getEmail())
            .username(userSaved.getUsername())
            .token("get access token via login")
            .roles(
                userSaved.getRoles().stream()
                .map(element -> element.getRoleName().name()).collect(Collectors.toList())
            ).build();
        });
    }

    @Override
    public Try<Boolean> resetPassword(ResetPasswordRequestDTO dto) {
        val user = userRepository.findByUsername(dto.getUsername());
        if (!user.isDefined()){
            return Try.failure(new Exception("Username not exist"));
        }
        if (user.isDefined()){
            if (!user.get().getActive()){
                return Try.failure(new Exception("User has been DE-ACTIVE account"));
            }
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getCurrentPassword())
        );

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        if (userPrinciple == null){
            return Try.failure(new Exception("User not authenticated!"));
        }

        if (dto.getNewPassword().equalsIgnoreCase(dto.getCurrentPassword())){
            return Try.failure(new Exception("New password is duplicated with current password"));
        }

        if (!dto.getNewPasswordConfirm().equalsIgnoreCase(dto.getNewPassword())){
            return Try.failure(new Exception("Password confirm not matched"));
        }

        // setup new password and save
        user.get().setPassword(passwordEncoder.encode(dto.getNewPasswordConfirm()));

        return Try.of(() -> {
            userRepository.save(user.get());
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when reset password")));
    }

    // auth space
    @Override
    public Try<User> getOne(Long id) {
        return Try.of(() -> userRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when find user by id: "+id)));
    }

    @Override
    public Try<User> update(UserUpdateRequestDTO dto) {
        val userResult = userRepository.findById(dto.getId());
        if (!userResult.isPresent()){
            return Try.failure(new Exception("Failure when find user to update by id: "+dto.getId()));
        }

        val userUpdate = centralMapper.toUserUpdate(userResult.get(),dto);

        // relationship space
        if (dto.getEmbedded() != null){
            val departmentId = dto.getEmbedded().getDepartmentId();
            if (departmentId != null && !departmentId.equals(userUpdate.getDepartment().getId())){
                val department = departmentRepository.findById(departmentId);
                department.ifPresent(userUpdate::setDepartment);
            }
        }
        // relationship space

        userUpdate.getRoles().clear();
        Set<String> roleInDTO = dto.getRoles();
        Set<Role> roles = new HashSet<>();
        // roleInDTO need check before this process
        roleInDTO.forEach(
                role -> {
                    switch (role){
                        case "ROLE_ADMIN":
                            Role role_ADMIN = roleRepository.findByRoleName(RoleName.ROLE_ADMIN).get();
                            if (role_ADMIN != null) roles.add(role_ADMIN);
                            break;
                        /*****/
                        case "ROLE_ACCOUNTANT":
                            Role role_ACCOUNTANT = roleRepository.findByRoleName(RoleName.ROLE_ACCOUNTANT).get();
                            if (role_ACCOUNTANT != null) roles.add(role_ACCOUNTANT);
                            break;
                        /*****/
                        case "ROLE_EMPLOYEE":
                            Role role_EMPLOYEE = roleRepository.findByRoleName(RoleName.ROLE_EMPLOYEE).get();
                            if (role_EMPLOYEE != null) roles.add(role_EMPLOYEE);
                            break;
                        /*****/
                        case "ROLE_INSPECTOR":
                            Role role_INSPECTOR = roleRepository.findByRoleName(RoleName.ROLE_INSPECTOR).get();
                            if (role_INSPECTOR != null) roles.add(role_INSPECTOR);
                            break;
                        /*****/
                    }
                }
        );

        if (roles.isEmpty()){
            return Try.failure(new Exception("User register need assign role"));
        }

        userUpdate.setRoles(roles);
        userUpdate.setPassword(passwordEncoder.encode(dto.getPassword()));

        return Try.of(() -> userRepository.save(userUpdate))
            .orElse(() -> Try.failure(new Exception("Failure when update user")));
    }


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @Override
    public List<User> fetchAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> fetchPage(FetchPageUserRequestDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage(),dto.getSize());
        return userRepository.findAll(pageable);
    }

}//end class
