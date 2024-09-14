package kz.aqyl.newsdesk.service;

import kz.aqyl.newsdesk.dto.AdvertisementDto;

import java.util.List;

public interface AdvertisementService {

  AdvertisementDto addAdvertisement(AdvertisementDto advertisementDto);

  List<AdvertisementDto> getAdvertisement();

  AdvertisementDto updateAdvertisement(Long id, AdvertisementDto advertisementDto);

  void deleteAdvertisement(Long id);
}
