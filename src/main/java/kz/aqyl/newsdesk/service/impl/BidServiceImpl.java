package kz.aqyl.newsdesk.service.impl;

import kz.aqyl.newsdesk.dto.BidDto;
import kz.aqyl.newsdesk.entity.Advertisement;
import kz.aqyl.newsdesk.entity.Bid;
import kz.aqyl.newsdesk.entity.User;
import kz.aqyl.newsdesk.exception.AdvertisementInactiveException;
import kz.aqyl.newsdesk.exception.AdvertisementNotFoundException;
import kz.aqyl.newsdesk.exception.BidNotFoundException;
import kz.aqyl.newsdesk.exception.UnauthorizedException;
import kz.aqyl.newsdesk.mapper.BidMapper;
import kz.aqyl.newsdesk.repository.AdvertisementRepository;
import kz.aqyl.newsdesk.repository.BidRepository;
import kz.aqyl.newsdesk.service.BidService;
import kz.aqyl.newsdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

  private final BidRepository bidRepository;

  private final AdvertisementRepository advertisementRepository;

  private final UserService userService;

  @Override
  public BidDto placeBid(Long AdId, double bidAmount) {
    Advertisement advertisement = advertisementRepository.findById(AdId)
            .orElseThrow(() -> new AdvertisementNotFoundException("Advertisement not found"));

    if (!advertisement.isActive()) {
      throw new AdvertisementInactiveException("Cannot place bid on inactive advertisement");
    }

    if (bidAmount <= advertisement.getCurrentCost()) {
      throw new IllegalArgumentException("Bid must be higher than the current cost");
    }

    User currentUser = userService.getCurrentSessionUser();

    Bid bid = new Bid();
    bid.setBidAmount(bidAmount);
    bid.setAdvertisement(advertisement);
    bid.setUser(currentUser);

    advertisement.setCurrentCost(bidAmount);
    advertisementRepository.save(advertisement);

    return BidMapper.toDto(bidRepository.save(bid));
  }

  @Override
  public BidDto updateBid(Long id, double newBidAmount) {
    Optional<Bid> bidOptional = bidRepository.findById(id);

    if (bidOptional.isEmpty()){
      throw new BidNotFoundException("Bid not found");
    }

    Bid bid = bidOptional.get();
    if (!bid.getUser().getId().equals(userService.getCurrentSessionUser().getId())){
      throw new UnauthorizedException("You are not authorized to change this bid");
    }

    if (newBidAmount < bid.getAdvertisement().getMinCost()){
      throw new IllegalArgumentException("Your bid amount is lower than minimum cost for this advertisement");
    }

    bid.setBidAmount(newBidAmount);

    return BidMapper.toDto(bidRepository.save(bid));
  }

  @Override
  public List<BidDto> getUsersBids() {
    Long userId = userService.getCurrentSessionUser().getId();
    List<Bid> userBidList = bidRepository.findAllByUserId(userId);
    return userBidList.stream()
            .map(BidMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteBid(Long id) {
    Optional<Bid> bidOptional = bidRepository.findById(id);

    if (bidOptional.isEmpty()){
      throw new BidNotFoundException("Bid not found");
    }

    Bid bid = bidOptional.get();
    if (!bid.getUser().getId().equals(userService.getCurrentSessionUser().getId())){
      throw new UnauthorizedException("You are not authorized to delete this bid");
    }

    bidRepository.deleteById(id);
  }
}
