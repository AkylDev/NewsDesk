package kz.aqyl.newsdesk.service;

import kz.aqyl.newsdesk.dto.AdvertisementDto;
import kz.aqyl.newsdesk.entity.Advertisement;
import kz.aqyl.newsdesk.entity.User;
import kz.aqyl.newsdesk.exception.UnauthorizedException;
import kz.aqyl.newsdesk.mapper.AdvertisementMapper;
import kz.aqyl.newsdesk.repository.AdvertisementRepository;
import kz.aqyl.newsdesk.service.impl.AdvertisementServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdvertisementServiceImplTest {
  @Mock
  private AdvertisementRepository advertisementRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private AdvertisementServiceImpl advertisementService;

  @Test
  void addAdvertisement_ShouldAddNewAdvertisement_WhenValidInput() {
    User user = new User();
    user.setId(1L);
    AdvertisementDto adDto = new AdvertisementDto(1L, "Title", "Description", 100.0, 100.0, null, null);
    Advertisement adEntity = AdvertisementMapper.toEntity(adDto);
    adEntity.setUser(user);
    adEntity.setCurrentCost(adDto.minCost());

    when(userService.getCurrentSessionUser()).thenReturn(user);
    when(advertisementRepository.save(any(Advertisement.class))).thenReturn(adEntity);

    AdvertisementDto createdAd = advertisementService.addAdvertisement(adDto);

    assertEquals(adDto.title(), createdAd.title());
    assertEquals(adDto.description(), createdAd.description());
    verify(advertisementRepository).save(any(Advertisement.class));
  }

  @Test
  void updateAdvertisement_ShouldUpdateAd_WhenValidIdAndUser() {
    User user = new User();
    user.setId(1L);
    Advertisement ad = new Advertisement();
    ad.setTitle("Old Title");
    ad.setDescription("Old Description");
    ad.setMinCost(100.0);
    ad.setId(1L);
    ad.setUser(user);

    AdvertisementDto updatedAdDto = new AdvertisementDto(1L, "New title", "New description", 150.0, 150.0, null, null);

    when(advertisementRepository.findById(1L)).thenReturn(Optional.of(ad));
    when(userService.getCurrentSessionUser()).thenReturn(user);
    when(advertisementRepository.save(any(Advertisement.class))).thenReturn(ad);

    AdvertisementDto result = advertisementService.updateAdvertisement(1L, updatedAdDto);

    assertEquals(updatedAdDto.title(), result.title());
    assertEquals(updatedAdDto.description(), result.description());
    assertEquals(updatedAdDto.minCost(), result.minCost());
    verify(advertisementRepository).save(ad);
  }

  @Test
  void updateAdvertisement_ShouldThrowUnauthorizedException_WhenUserIsNotOwner() {
    User owner = new User();
    owner.setId(1L);
    User anotherUser = new User();
    anotherUser.setId(2L);

    Advertisement ad = new Advertisement();
    ad.setTitle("Title");
    ad.setDescription("Description");
    ad.setMinCost(100.0);
    ad.setId(1L);
    ad.setUser(owner);

    AdvertisementDto updatedAdDto = new AdvertisementDto(1L, "New title", "New description", 150.0, 150.0, null, null);

    when(advertisementRepository.findById(1L)).thenReturn(Optional.of(ad));
    when(userService.getCurrentSessionUser()).thenReturn(anotherUser);

    UnauthorizedException thrown = assertThrows(UnauthorizedException.class, () -> advertisementService.updateAdvertisement(1L, updatedAdDto));

    assertEquals("You are not authorized to change this account", thrown.getMessage());
    verify(advertisementRepository, never()).save(any(Advertisement.class));
  }

  @Test
  void deleteAdvertisement_ShouldDeleteAd_WhenValidIdAndUserIsOwner() {
    User user = new User();
    user.setId(1L);

    Advertisement ad = new Advertisement();
    ad.setTitle("Title");
    ad.setDescription("Description");
    ad.setId(1L);
    ad.setUser(user);

    when(advertisementRepository.findById(1L)).thenReturn(Optional.of(ad));
    when(userService.getCurrentSessionUser()).thenReturn(user);

    advertisementService.deleteAdvertisement(1L);

    verify(advertisementRepository).deleteById(1L);
  }
}
