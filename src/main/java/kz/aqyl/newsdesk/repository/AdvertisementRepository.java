package kz.aqyl.newsdesk.repository;

import kz.aqyl.newsdesk.entity.Advertisement;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
  @NonNull
  Optional<Advertisement> findById(@NonNull Long id);
}
