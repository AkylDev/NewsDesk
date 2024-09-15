package kz.aqyl.newsdesk.service.impl;

import kz.aqyl.newsdesk.dto.AdvertisementDto;
import kz.aqyl.newsdesk.entity.Advertisement;
import kz.aqyl.newsdesk.exception.UnauthorizedException;
import kz.aqyl.newsdesk.exception.UserAdvertisementNotFoundException;
import kz.aqyl.newsdesk.mapper.AdvertisementMapper;
import kz.aqyl.newsdesk.repository.AdvertisementRepository;
import kz.aqyl.newsdesk.service.AdvertisementService;
import kz.aqyl.newsdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

  private final AdvertisementRepository advertisementRepository;

  private final UserService userService;

  private static final Logger log = LoggerFactory.getLogger(AdvertisementServiceImpl.class);

  @Override
  public AdvertisementDto addAdvertisement(AdvertisementDto advertisementDto) {
    Advertisement advertisement = AdvertisementMapper.toEntity(advertisementDto);
    advertisement.setUser(userService.getCurrentSessionUser());
    advertisement.setCurrentCost(advertisementDto.minCost());
    return AdvertisementMapper.toDto(advertisementRepository.save(advertisement));
  }

  @Override
  public List<AdvertisementDto> getAdvertisements() {
    List<Advertisement> advertisementList = advertisementRepository.findAll();
    return advertisementList.stream()
            .map(AdvertisementMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  public AdvertisementDto updateAdvertisement(Long id, AdvertisementDto advertisementDto) {
    Optional<Advertisement> advertisementOptional = advertisementRepository.findById(id);

    if (advertisementOptional.isEmpty()) {
      log.warn("Advertisement with id {} not found", id);
      throw new UsernameNotFoundException("Username not found");
    }

    Advertisement advertisement = advertisementOptional.get();
    if (!advertisement.getUser().getId().equals(userService.getCurrentSessionUser().getId())){
      log.warn("You are not allowed to update this advertisement with id {}", id);
      throw new UnauthorizedException("You are not authorized to change this account");
    }

    advertisement.setTitle(advertisementDto.title());
    advertisement.setDescription(advertisementDto.description());
    advertisement.setMinCost(advertisementDto.minCost());

    if (advertisementDto.minCost() > advertisement.getCurrentCost()){
      advertisement.setCurrentCost(advertisementDto.minCost());
    }

    return AdvertisementMapper.toDto(advertisementRepository.save(advertisement));
  }

  @Override
  public void deleteAdvertisement(Long id) {
    Optional<Advertisement> advertisementOptional = advertisementRepository.findById(id);

    if (advertisementOptional.isEmpty()){
      throw new UserAdvertisementNotFoundException("Advertisement not found");
    }

    Advertisement advertisement = advertisementOptional.get();
    if (!advertisement.getUser().getId().equals(userService.getCurrentSessionUser().getId())){
      log.warn("You are not allowed to delete this advertisement with id {}", id);
      throw new UnauthorizedException("You are not authorized to delete this account");
    }

    advertisementRepository.deleteById(id);
  }
}
