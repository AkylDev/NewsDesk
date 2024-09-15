package kz.aqyl.newsdesk.service;

import kz.aqyl.newsdesk.entity.Advertisement;
import kz.aqyl.newsdesk.entity.User;

public interface NotificationService {
  void sendOutbidNotification(User user, Advertisement advertisement);

  void sendAuctionWonNotification(User buyer, User seller, Advertisement advertisement);
}
