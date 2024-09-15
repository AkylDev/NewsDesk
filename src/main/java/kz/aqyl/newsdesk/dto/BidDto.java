package kz.aqyl.newsdesk.dto;

public record BidDto(Long id, double bidMount, UserDto userDto, AdvertisementDto advertisementDto) {
}
