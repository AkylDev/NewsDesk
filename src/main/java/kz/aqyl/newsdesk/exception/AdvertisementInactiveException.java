package kz.aqyl.newsdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdvertisementInactiveException extends RuntimeException {
  public AdvertisementInactiveException(String message) {
    super(message);
  }

}