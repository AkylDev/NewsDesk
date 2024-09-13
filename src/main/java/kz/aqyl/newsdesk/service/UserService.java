package kz.aqyl.newsdesk.service;

import kz.aqyl.newsdesk.dto.UserDto;
import kz.aqyl.newsdesk.dto.request.LoginRequest;

public interface UserService {
  UserDto register(UserDto user);
  UserDto login(LoginRequest request);
}
