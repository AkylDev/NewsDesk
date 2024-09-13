package kz.aqyl.newsdesk.service.impl;

import kz.aqyl.newsdesk.dto.UserDto;
import kz.aqyl.newsdesk.dto.request.LoginRequest;
import kz.aqyl.newsdesk.entity.Permissions;
import kz.aqyl.newsdesk.entity.User;
import kz.aqyl.newsdesk.mapper.UserMapper;
import kz.aqyl.newsdesk.repository.PermissionsRepository;
import kz.aqyl.newsdesk.repository.UserRepository;
import kz.aqyl.newsdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PermissionsRepository permissionsRepository;
  private final PasswordEncoder passwordEncoder;
  private final CustomUserDetailsService userDetailsService;

  @Override
  public UserDto register(UserDto user) {
    Optional<User> checkUser = userRepository.findByEmail(user.email());
    if (checkUser.isPresent()) {
      throw new IllegalArgumentException("User with this email already exists.");
    }

    User newUser = new User();
    newUser.setEmail(user.email());
    newUser.setPassword(passwordEncoder.encode(user.password()));

    Permissions defaultPermission = permissionsRepository.findByRole("ROLE_USER");
    if (defaultPermission == null) {
      defaultPermission = new Permissions();
      defaultPermission.setRole("ROLE_USER");
      defaultPermission = permissionsRepository.save(defaultPermission);
    }
    newUser.setPermissionList(Collections.singletonList(defaultPermission));

    return UserMapper.toDto(userRepository.save(newUser));
  }

  @Override
  public UserDto login(LoginRequest request) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
    if (passwordEncoder.matches(request.password(), userDetails.getPassword())) {
      UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(userDetails, null,
                      userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      return UserMapper.toDto((User) userDetails);
    } else {
      throw new UsernameNotFoundException("Invalid credentials");
    }
  }
}
