package kz.aqyl.newsdesk.mapper;

import kz.aqyl.newsdesk.dto.UserDto;
import kz.aqyl.newsdesk.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public static UserDto toDto(User user) {
    if (user == null) {
      return null;
    }

    return new UserDto(
            user.getUsername(),
            null
    );
  }
}
