package kz.aqyl.newsdesk.dto;


public record AdvertisementDto(Long id, String title, String description, double minCost, double currentCost, String userEmail) {
}
