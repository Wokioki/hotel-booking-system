package com.wokioki.hotelbooking.room.dto;

import com.wokioki.hotelbooking.room.RoomStatus;

import java.math.BigDecimal;

public record RoomResponse(
        Long id,
        String roomNumber,
        Long roomTypeId,
        String roomTypeName,
        Integer capacity,
        BigDecimal pricePerNight,
        RoomStatus status,
        String description
) {
}