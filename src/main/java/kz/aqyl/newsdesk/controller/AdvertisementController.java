package kz.aqyl.newsdesk.controller;

import kz.aqyl.newsdesk.dto.AdvertisementDto;
import kz.aqyl.newsdesk.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/advertisements")
public class AdvertisementController {

  private final AdvertisementService advertisementService;

  @PostMapping
  public ResponseEntity<AdvertisementDto> addAd(@RequestBody AdvertisementDto advertisementDto) {
    return new ResponseEntity<>(advertisementService.addAdvertisement(advertisementDto), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<AdvertisementDto>> getAds() {
    return new ResponseEntity<>(advertisementService.getAdvertisements(), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  private ResponseEntity<AdvertisementDto> updateAd(@PathVariable(name = "id") Long id,
                                                    @RequestBody AdvertisementDto advertisementDto) {
    return new ResponseEntity<>(advertisementService.updateAdvertisement(id, advertisementDto), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  private ResponseEntity<Void> deleteAd (@PathVariable(name = "id") Long id) {
    advertisementService.deleteAdvertisement(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
