package kz.aqyl.newsdesk.repository;

import jakarta.persistence.LockModeType;
import kz.aqyl.newsdesk.entity.Advertisement;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
  @NonNull
  Optional<Advertisement> findById(@NonNull Long id);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT a FROM Advertisement a WHERE a.id = :id")
  Optional<Advertisement> findByIdWithLock(@Param("id") Long id);
}
