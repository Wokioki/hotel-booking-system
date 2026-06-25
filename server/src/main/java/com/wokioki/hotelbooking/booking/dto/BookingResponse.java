package com.wokioki.hotelbooking.booking.dto;

import com.wokioki.hotelbooking.booking.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long userId,
        Long roomId,
        String roomNumber,
        LocalDate checkIn,
        LocalDate checkOut,
        BookingStatus status,
        BigDecimal totalPrice,
        LocalDateTime createdAt
) {
}