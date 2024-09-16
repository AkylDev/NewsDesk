package kz.aqyl.newsdesk.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "advertisements")
public class Advertisement {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String description;

  private double minCost;

  private double currentCost;

  private boolean isActive;

  private LocalDateTime auctionEndTime;

  private String imageUrl;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
