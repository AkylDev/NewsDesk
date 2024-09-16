package kz.aqyl.newsdesk.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Advertisement Controller", description = "API для работы с объявлениями")
public class AdvertisementController {

  private final AdvertisementService advertisementService;

  @Operation(summary = "Добавить объявление", description = "Создать новое объявление с указанием всех необходимых полей")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Объявление успешно создано"),
          @ApiResponse(responseCode = "400", description = "Некорректные данные для создания объявления")
  })
  @PostMapping
  public ResponseEntity<AdvertisementDto> addAd(@RequestBody @Parameter(description = "Данные нового объявления") AdvertisementDto advertisementDto) {
    return new ResponseEntity<>(advertisementService.addAdvertisement(advertisementDto), HttpStatus.CREATED);
  }

  @Operation(summary = "Получить все объявления", description = "Получить список всех объявлений")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Список объявлений получен")
  })
  @GetMapping
  public ResponseEntity<List<AdvertisementDto>> getAds() {
    return new ResponseEntity<>(advertisementService.getAdvertisements(), HttpStatus.OK);
  }

  @Operation(summary = "Обновить объявление", description = "Обновить существующее объявление по его ID")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Объявление успешно обновлено"),
          @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
          @ApiResponse(responseCode = "403", description = "Пользователь не авторизован для изменения этого объявления")
  })
  @PutMapping("/{id}")
  public ResponseEntity<AdvertisementDto> updateAd(@PathVariable(name = "id") @Parameter(description = "ID объявления") Long id,
                                                   @RequestBody @Parameter(description = "Обновленные данные объявления") AdvertisementDto advertisementDto) {
    return new ResponseEntity<>(advertisementService.updateAdvertisement(id, advertisementDto), HttpStatus.OK);
  }

  @Operation(summary = "Удалить объявление", description = "Удалить существующее объявление по его ID")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "Объявление успешно удалено"),
          @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
          @ApiResponse(responseCode = "403", description = "Пользователь не авторизован для удаления этого объявления")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAd(@PathVariable(name = "id") @Parameter(description = "ID объявления") Long id) {
    advertisementService.deleteAdvertisement(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
