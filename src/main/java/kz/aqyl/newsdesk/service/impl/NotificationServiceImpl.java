package kz.aqyl.newsdesk.service.impl;

import kz.aqyl.newsdesk.entity.Advertisement;
import kz.aqyl.newsdesk.entity.User;
import kz.aqyl.newsdesk.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

  private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

  @Override
  public void sendOutbidNotification(User user, Advertisement advertisement) {
    log.info("User {} was outbid on advertisement {} with title '{}'.",
            user.getUsername(), advertisement.getId(), advertisement.getTitle());
  }

  @Override
  public void sendAuctionWonNotification(User buyer, User seller, Advertisement advertisement) {

  }
}
