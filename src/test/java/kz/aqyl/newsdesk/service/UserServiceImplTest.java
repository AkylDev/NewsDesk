package kz.aqyl.newsdesk.service;

import kz.aqyl.newsdesk.dto.UserDto;
import kz.aqyl.newsdesk.dto.request.LoginRequest;
import kz.aqyl.newsdesk.entity.Permissions;
import kz.aqyl.newsdesk.entity.User;
import kz.aqyl.newsdesk.repository.PermissionsRepository;
import kz.aqyl.newsdesk.repository.UserRepository;
import kz.aqyl.newsdesk.service.impl.CustomUserDetailsService;
import kz.aqyl.newsdesk.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PermissionsRepository permissionsRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private CustomUserDetailsService userDetailsService;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  void register_ShouldRegisterUser_WhenValidInput() {
    UserDto userDto = new UserDto("test@example.com", "password123");
    User newUser = new User();
    newUser.setEmail(userDto.email());
    newUser.setPassword("encodedPassword");

    Permissions roleUser = new Permissions();
    roleUser.setRole("ROLE_USER");

    when(userRepository.findByEmail(userDto.email())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(userDto.password())).thenReturn("encodedPassword");
    when(permissionsRepository.findByRole("ROLE_USER")).thenReturn(roleUser);
    when(userRepository.save(any(User.class))).thenReturn(newUser);

    UserDto registeredUser = userService.register(userDto);

    assertEquals(userDto.email(), registeredUser.email());
    verify(userRepository).save(any(User.class));
    verify(passwordEncoder).encode(userDto.password());
  }

  @Test
  void register_ShouldThrowException_WhenEmailAlreadyExists() {
    UserDto userDto = new UserDto("existing@example.com", "password123");
    User existingUser = new User();
    existingUser.setEmail(userDto.email());

    when(userRepository.findByEmail(userDto.email())).thenReturn(Optional.of(existingUser));

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.register(userDto));

    assertEquals("User with this email already exists.", thrown.getMessage());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void login_ShouldLoginUser_WhenCredentialsAreCorrect() {
    LoginRequest request = new LoginRequest("test@example.com", "password123");
    User user = new User();
    user.setEmail(request.email());
    user.setPassword("encodedPassword");


    when(userDetailsService.loadUserByUsername(request.email())).thenReturn(user);
    when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);

    UserDto loggedInUser = userService.login(request);

    assertEquals(request.email(), loggedInUser.email());
    verify(userDetailsService).loadUserByUsername(request.email());
    verify(passwordEncoder).matches(request.password(), user.getPassword());
  }

  @Test
  void login_ShouldThrowException_WhenCredentialsAreInvalid() {
    LoginRequest request = new LoginRequest("test@example.com", "wrongpassword");
    UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            "test@example.com",
            "encodedPassword",
            Collections.emptyList()
    );

    when(userDetailsService.loadUserByUsername(request.email())).thenReturn(userDetails);
    when(passwordEncoder.matches(request.password(), userDetails.getPassword())).thenReturn(false);

    UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () -> userService.login(request));

    assertEquals("Invalid credentials", thrown.getMessage());
    verify(userDetailsService).loadUserByUsername(request.email());
    verify(passwordEncoder).matches(request.password(), userDetails.getPassword());
  }
}
