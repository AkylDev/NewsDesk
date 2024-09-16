package kz.aqyl.newsdesk.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.aqyl.newsdesk.dto.UserDto;
import kz.aqyl.newsdesk.dto.request.LoginRequest;
import kz.aqyl.newsdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "API for user authentication and registration")
public class AuthController {

  private final UserService userService;

  @Operation(summary = "User registration", description = "Register a new user with email and password")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "User successfully registered"),
          @ApiResponse(responseCode = "400", description = "Invalid input data")
  })
  @PostMapping("/register")
  public ResponseEntity<UserDto> register(@RequestBody @Parameter(description = "User data for registration") UserDto user) {
    return new ResponseEntity<>(userService.register(user), HttpStatus.CREATED);
  }

  @Operation(summary = "User login", description = "Login a user with email and password")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "User successfully logged in"),
          @ApiResponse(responseCode = "401", description = "Invalid credentials")
  })
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Parameter(description = "Login request with email and password") LoginRequest request) {
    try {
      return ResponseEntity.ok(userService.login(request));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
  }
}
