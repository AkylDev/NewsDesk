package kz.aqyl.newsdesk.repository;

import kz.aqyl.newsdesk.entity.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionsRepository extends JpaRepository<Permissions, Long> {
  Permissions findByRole(String role);
}