package com.dailycodework.parcelservice.web;

import com.dailycodework.parcelservice.dto.UpdateParcelStatusRequest;
import com.dailycodework.parcelservice.entities.Parcel;
import com.dailycodework.parcelservice.entities.ParcelStatusHistory;
import com.dailycodework.parcelservice.entities.Status;
import com.dailycodework.parcelservice.service.ParcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parcels")
@RequiredArgsConstructor
public class ParcelRestController {

    private final ParcelService parcelService;

    @PostMapping
    public ResponseEntity<Parcel> createParcel(@RequestBody Parcel parcel) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(parcelService.createParcel(parcel));
    }

    @GetMapping
    public ResponseEntity<List<Parcel>> getAllParcels() {
        return ResponseEntity.ok(parcelService.getAllParcels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parcel> getParcelById(@PathVariable Long id) {
        return ResponseEntity.ok(parcelService.getParcelById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Parcel>> getParcelsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(parcelService.getParcelsByOrderId(orderId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Parcel>> getParcelsByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(parcelService.getParcelsByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parcel> updateParcel(
            @PathVariable Long id,
            @RequestBody Parcel parcel
    ) {
        return ResponseEntity.ok(parcelService.updateParcel(id, parcel));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Parcel> updateParcelStatus(
            @PathVariable Long id,
            @RequestBody UpdateParcelStatusRequest request
    ) {
        return ResponseEntity.ok(
                parcelService.updateParcelStatus(
                        id,
                        request.getStatus(),
                        request.getChangedBy(),
                        request.getComment()
                )
        );
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ParcelStatusHistory>> getParcelHistory(@PathVariable Long id) {
        return ResponseEntity.ok(parcelService.getParcelHistory(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParcel(@PathVariable Long id) {
        parcelService.deleteParcel(id);
        return ResponseEntity.noContent().build();
    }
}