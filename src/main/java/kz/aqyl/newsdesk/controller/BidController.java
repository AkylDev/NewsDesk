package kz.aqyl.newsdesk.controller;

import kz.aqyl.newsdesk.dto.BidDto;
import kz.aqyl.newsdesk.dto.request.BidRequest;
import kz.aqyl.newsdesk.service.BidService;
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
@RequestMapping("/bids")
public class BidController {
  private final BidService bidService;

  @PostMapping
  public ResponseEntity<BidDto> createBid(@RequestBody BidRequest request) {
    return new ResponseEntity<>(bidService.placeBid(request.id(), request.bidMount()), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<BidDto>> getUsersBids() {
    return new ResponseEntity<>(bidService.getUsersBids(), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BidDto> updateBid(@PathVariable(name = "id") Long id,
                                          @RequestBody BidRequest request) {
    return new ResponseEntity<>(bidService.updateBid(id, request.bidMount()), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBid(@PathVariable(name = "id") Long id) {
    bidService.deleteBid(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
