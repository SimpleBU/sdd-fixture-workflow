package ru.testbed.workflow.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.testbed.workflow.dto.DeliveryDto;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDto> getDelivery(@PathVariable("deliveryId") String deliveryId) {
        return ResponseEntity.ok(new DeliveryDto());
    }

    @GetMapping("/export")
    public ResponseEntity<List<DeliveryDto>> exportDeliveries(@RequestParam(name = "date", required = false) LocalDate date) {
        return ResponseEntity.ok(List.of());
    }

}
