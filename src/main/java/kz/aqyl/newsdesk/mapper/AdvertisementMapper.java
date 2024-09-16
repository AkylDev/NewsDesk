package kz.aqyl.newsdesk.mapper;

import kz.aqyl.newsdesk.dto.AdvertisementDto;
import kz.aqyl.newsdesk.entity.Advertisement;
import org.springframework.stereotype.Component;

@Component
public class AdvertisementMapper {
  public static AdvertisementDto toDto(Advertisement advertisement) {
    if (advertisement == null) {
      return null;
    }

    return new AdvertisementDto(
            advertisement.getId(),
            advertisement.getTitle(),
            advertisement.getDescription(),
            advertisement.getMinCost(),
            advertisement.getCurrentCost(),
            advertisement.getUser().getUsername(),
            advertisement.getImageUrl()
    );
  }

  public static Advertisement toEntity(AdvertisementDto advertisementDto) {
    Advertisement advertisement = new Advertisement();
    advertisement.setId(advertisement.getId());
    advertisement.setTitle(advertisementDto.title());
    advertisement.setDescription(advertisementDto.description());
    advertisement.setMinCost(advertisementDto.minCost());
    advertisement.setCurrentCost(advertisementDto.currentCost());
    advertisement.setImageUrl(advertisementDto.imageUrl());
    advertisement.setActive(true);
    return advertisement;
  }

}
