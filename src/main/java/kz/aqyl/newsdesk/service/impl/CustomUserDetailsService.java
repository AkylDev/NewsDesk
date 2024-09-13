package kz.aqyl.newsdesk.service.impl;

import kz.aqyl.newsdesk.entity.User;
import kz.aqyl.newsdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepository.findByEmail(username);
    if (userOptional.isEmpty()){
      throw new UsernameNotFoundException("Username not found");
    }

    return userOptional.get();
  }
}
