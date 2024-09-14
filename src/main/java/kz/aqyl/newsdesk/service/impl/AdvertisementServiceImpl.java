package kz.aqyl.newsdesk.service.impl;

import kz.aqyl.newsdesk.dto.AdvertisementDto;
import kz.aqyl.newsdesk.entity.Advertisement;
import kz.aqyl.newsdesk.mapper.AdvertisementMapper;
import kz.aqyl.newsdesk.repository.AdvertisementRepository;
import kz.aqyl.newsdesk.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

  private final AdvertisementRepository advertisementRepository;

  @Override
  public AdvertisementDto addAdvertisement(AdvertisementDto advertisementDto) {
    Advertisement advertisement = AdvertisementMapper.toEntity(advertisementDto);
    return AdvertisementMapper.toDto(advertisementRepository.save(advertisement));
  }

  @Override
  public List<AdvertisementDto> getAdvertisement() {
    List<Advertisement> advertisementList = advertisementRepository.findAll();
    return advertisementList.stream()
            .map(AdvertisementMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  public AdvertisementDto updateAdvertisement(Long id, AdvertisementDto advertisementDto) {
    Optional<Advertisement> advertisementOptional = advertisementRepository.findById(id);

    if (advertisementOptional.isEmpty()) {
      throw new UsernameNotFoundException("Username not found");
    }

    Advertisement advertisement = advertisementOptional.get();
    advertisement.setTitle(advertisementDto.title());
    advertisement.setDescription(advertisementDto.description());
    advertisement.setCost(advertisementDto.cost());

    return AdvertisementMapper.toDto(advertisementRepository.save(advertisement));
  }

  @Override
  public void deleteAdvertisement(Long id) {
    advertisementRepository.deleteById(id);
  }
}
