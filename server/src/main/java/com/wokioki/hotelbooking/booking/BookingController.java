package com.wokioki.hotelbooking.booking;

import com.wokioki.hotelbooking.booking.dto.BookingResponse;
import com.wokioki.hotelbooking.booking.dto.CreateBookingRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(
            @RequestParam Long userId,
            @Valid @RequestBody CreateBookingRequest request
    ) {
        return bookingService.createBooking(userId, request);
    }
}