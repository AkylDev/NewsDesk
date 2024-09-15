package kz.aqyl.newsdesk.mapper;

import kz.aqyl.newsdesk.dto.BidDto;
import kz.aqyl.newsdesk.entity.Bid;
import org.springframework.stereotype.Component;

@Component
public class BidMapper {
  public static BidDto toDto(Bid bid) {
    if (bid == null) {
      return null;
    }

    return new BidDto(
            bid.getId(),
            bid.getBidAmount(),
            UserMapper.toDto(bid.getUser()),
            AdvertisementMapper.toDto(bid.getAdvertisement())
    );
  }
}
