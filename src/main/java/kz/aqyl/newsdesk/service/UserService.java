package kz.aqyl.newsdesk.service;

import kz.aqyl.newsdesk.dto.UserDto;
import kz.aqyl.newsdesk.dto.request.LoginRequest;
import kz.aqyl.newsdesk.entity.User;

public interface UserService {
  UserDto register(UserDto user);
  UserDto login(LoginRequest request);
  User getCurrentSessionUser();
}
