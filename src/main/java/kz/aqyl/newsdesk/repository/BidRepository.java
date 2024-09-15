package kz.aqyl.newsdesk.repository;

import kz.aqyl.newsdesk.entity.Bid;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
  List<Bid> findByAdvertisementIdOrderByBidAmountDesc(Long advertisementId);

  @NonNull
  Optional<Bid> findById(@NonNull Long id);

  List<Bid> findAllByUserId(Long id);
}

