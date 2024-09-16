package kz.aqyl.newsdesk.service;

import kz.aqyl.newsdesk.dto.BidDto;
import kz.aqyl.newsdesk.entity.Advertisement;
import kz.aqyl.newsdesk.entity.Bid;
import kz.aqyl.newsdesk.entity.User;
import kz.aqyl.newsdesk.exception.UnauthorizedException;
import kz.aqyl.newsdesk.repository.AdvertisementRepository;
import kz.aqyl.newsdesk.repository.BidRepository;
import kz.aqyl.newsdesk.service.impl.BidServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BidServiceImplTest {

  @Mock
  private BidRepository bidRepository;

  @Mock
  private AdvertisementRepository advertisementRepository;

  @Mock
  private UserService userService;

  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private BidServiceImpl bidService;

  @Test
  void placeBid_ShouldPlaceNewBid_WhenValidInput() {
    User user = new User();
    user.setId(1L);
    Advertisement ad = new Advertisement();
    ad.setId(1L);
    ad.setCurrentCost(100.0);
    ad.setMinCost(50.0);
    ad.setActive(true);

    when(advertisementRepository.findByIdWithLock(1L)).thenReturn(Optional.of(ad));
    when(userService.getCurrentSessionUser()).thenReturn(user);

    Bid savedBid = new Bid();
    savedBid.setBidAmount(150.0);

    when(bidRepository.save(any(Bid.class))).thenReturn(savedBid);

    BidDto result = bidService.placeBid(1L, 150.0);

    Assertions.assertEquals(150.0, result.bidMount());
    verify(bidRepository).save(any(Bid.class));
    verify(advertisementRepository).save(ad);
  }

  @Test
  void placeBid_ShouldThrowException_WhenBidIsLowerThanCurrentCost() {
    Advertisement ad = new Advertisement();
    ad.setId(1L);
    ad.setCurrentCost(100.0);
    ad.setMinCost(50.0);
    ad.setActive(true);

    when(advertisementRepository.findByIdWithLock(1L)).thenReturn(Optional.of(ad));

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> bidService.placeBid(1L, 90.0));

    assertEquals("Bid must be higher than the current cost", thrown.getMessage());
    verify(bidRepository, never()).save(any(Bid.class));
  }

  @Test
  void placeBid_ShouldSendNotificationToPreviousBidder_WhenBidIsHigher() {
    User user = new User();
    user.setId(1L);
    Advertisement ad = new Advertisement();
    ad.setId(1L);
    ad.setCurrentCost(100.0);
    ad.setMinCost(50.0);
    ad.setActive(true);

    Bid previousBid = new Bid();
    previousBid.setBidAmount(100.0);
    previousBid.setUser(new User());

    when(advertisementRepository.findByIdWithLock(1L)).thenReturn(Optional.of(ad));
    when(bidRepository.findTopByAdvertisement_IdOrderByBidAmountDesc(1L)).thenReturn(Optional.of(previousBid));
    when(userService.getCurrentSessionUser()).thenReturn(user);
    when(bidRepository.save(any(Bid.class))).thenReturn(new Bid());

    bidService.placeBid(1L, 150.0);

    verify(notificationService).sendOutbidNotification(previousBid.getUser(), ad);
    verify(bidRepository).save(any(Bid.class));
  }

  @Test
  void updateBid_ShouldUpdateBid_WhenValidInput() {
    User user = new User();
    user.setId(1L);
    Bid bid = new Bid();
    bid.setId(1L);
    bid.setBidAmount(100.0);
    bid.setUser(user);
    Advertisement ad = new Advertisement();
    ad.setMinCost(50.0);
    bid.setAdvertisement(ad);

    when(bidRepository.findById(1L)).thenReturn(Optional.of(bid));
    when(userService.getCurrentSessionUser()).thenReturn(user);

    Bid existingBid = new Bid();
    existingBid.setId(1L);
    existingBid.setBidAmount(120.0);

    when(bidRepository.save(any(Bid.class))).thenReturn(existingBid);

    BidDto result = bidService.updateBid(1L, 120.0);

    assertEquals(120.0, result.bidMount());
    verify(bidRepository).save(bid);
  }

  @Test
  void updateBid_ShouldThrowUnauthorizedException_WhenUserIsNotOwner() {
    User owner = new User();
    owner.setId(1L);
    User anotherUser = new User();
    anotherUser.setId(2L);

    Bid bid = new Bid();
    bid.setId(1L);
    bid.setUser(owner);

    when(bidRepository.findById(1L)).thenReturn(Optional.of(bid));
    when(userService.getCurrentSessionUser()).thenReturn(anotherUser);

    UnauthorizedException thrown = assertThrows(UnauthorizedException.class, () -> bidService.updateBid(1L, 150.0));

    assertEquals("You are not authorized to change this bid", thrown.getMessage());
    verify(bidRepository, never()).save(any(Bid.class));
  }

  @Test
  void deleteBid_ShouldDeleteBid_WhenUserIsOwner() {
    User user = new User();
    user.setId(1L);
    Bid bid = new Bid();
    bid.setId(1L);
    bid.setUser(user);

    when(bidRepository.findById(1L)).thenReturn(Optional.of(bid));
    when(userService.getCurrentSessionUser()).thenReturn(user);

    bidService.deleteBid(1L);

    verify(bidRepository).deleteById(1L);
  }

  @Test
  void deleteBid_ShouldThrowUnauthorizedException_WhenUserIsNotOwner() {
    User owner = new User();
    owner.setId(1L);
    User anotherUser = new User();
    anotherUser.setId(2L);

    Bid bid = new Bid();
    bid.setId(1L);
    bid.setUser(owner);

    when(bidRepository.findById(1L)).thenReturn(Optional.of(bid));
    when(userService.getCurrentSessionUser()).thenReturn(anotherUser);

    UnauthorizedException thrown = assertThrows(UnauthorizedException.class, () -> bidService.deleteBid(1L));

    assertEquals("You are not authorized to delete this bid", thrown.getMessage());
    verify(bidRepository, never()).deleteById(anyLong());
  }
}
