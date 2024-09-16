package kz.aqyl.newsdesk.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Bid Controller", description = "API для работы со ставками на объявления")
public class BidController {

  private final BidService bidService;

  @Operation(summary = "Создать новую ставку", description = "Добавить ставку для объявления")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Ставка успешно создана"),
          @ApiResponse(responseCode = "400", description = "Некорректные данные для создания ставки"),
          @ApiResponse(responseCode = "404", description = "Объявление не найдено")
  })
  @PostMapping
  public ResponseEntity<BidDto> createBid(@RequestBody @Parameter(description = "Данные для создания новой ставки") BidRequest request) {
    return new ResponseEntity<>(bidService.placeBid(request.id(), request.bidMount()), HttpStatus.CREATED);
  }

  @Operation(summary = "Получить ставки пользователя", description = "Получить все ставки, сделанные текущим пользователем")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Список ставок получен")
  })
  @GetMapping
  public ResponseEntity<List<BidDto>> getUsersBids() {
    return new ResponseEntity<>(bidService.getUsersBids(), HttpStatus.OK);
  }

  @Operation(summary = "Обновить ставку", description = "Обновить существующую ставку по её ID")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Ставка успешно обновлена"),
          @ApiResponse(responseCode = "404", description = "Ставка не найдена"),
          @ApiResponse(responseCode = "403", description = "Пользователь не авторизован для изменения этой ставки")
  })
  @PutMapping("/{id}")
  public ResponseEntity<BidDto> updateBid(@PathVariable(name = "id") @Parameter(description = "ID ставки") Long id,
                                          @RequestBody @Parameter(description = "Новая сумма ставки") BidRequest request) {
    return new ResponseEntity<>(bidService.updateBid(id, request.bidMount()), HttpStatus.OK);
  }

  @Operation(summary = "Удалить ставку", description = "Удалить существующую ставку по её ID")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "Ставка успешно удалена"),
          @ApiResponse(responseCode = "404", description = "Ставка не найдена"),
          @ApiResponse(responseCode = "403", description = "Пользователь не авторизован для удаления этой ставки")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBid(@PathVariable(name = "id") @Parameter(description = "ID ставки") Long id) {
    bidService.deleteBid(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
