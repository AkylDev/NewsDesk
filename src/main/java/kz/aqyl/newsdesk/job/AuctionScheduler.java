package kz.aqyl.newsdesk.job;

import jakarta.transaction.Transactional;
import kz.aqyl.newsdesk.entity.Advertisement;
import kz.aqyl.newsdesk.entity.Bid;
import kz.aqyl.newsdesk.repository.AdvertisementRepository;
import kz.aqyl.newsdesk.repository.BidRepository;
import kz.aqyl.newsdesk.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@EnableScheduling
@Service
@RequiredArgsConstructor
public class AuctionScheduler {

  private final AdvertisementRepository advertisementRepository;
  private final BidRepository bidRepository;
  private final NotificationService notificationService;

  @Scheduled(fixedRate = 60000)
  public void checkAuctions() {
    List<Advertisement> activeAdvertisements = advertisementRepository.findAllActiveAdvertisements();

    LocalDateTime now = LocalDateTime.now();
    for (Advertisement ad : activeAdvertisements) {
      if (ad.getAuctionEndTime() != null && ad.getAuctionEndTime().isBefore(now)) {
        endAuction(ad);
      }
    }
  }

  @Transactional
  public void endAuction(Advertisement advertisement) {
    Optional<Bid> bidOptional = bidRepository.findTopByAdvertisement_IdOrderByBidAmountDesc(advertisement.getId());

    if (bidOptional.isPresent()) {
      Bid winningBid = bidOptional.get();
      notificationService.sendAuctionWonNotification(winningBid.getUser(), advertisement.getUser(), advertisement);
    }

    advertisement.setActive(false);
    advertisementRepository.save(advertisement);
  }
}

