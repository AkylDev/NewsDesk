package kz.aqyl.newsdesk.service;

import kz.aqyl.newsdesk.dto.BidDto;

import java.util.List;

public interface BidService {
  BidDto placeBid(Long advertisementId, double bidAmount);
  BidDto updateBid(Long id, double newBidAmount);

  List<BidDto> getUsersBids();

  void deleteBid(Long id);
}
