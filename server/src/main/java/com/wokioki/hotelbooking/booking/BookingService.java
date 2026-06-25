package com.wokioki.hotelbooking.booking;

import com.wokioki.hotelbooking.booking.dto.BookingResponse;
import com.wokioki.hotelbooking.booking.dto.CreateBookingRequest;
import com.wokioki.hotelbooking.common.exception.BadRequestException;
import com.wokioki.hotelbooking.common.exception.BookingConflictException;
import com.wokioki.hotelbooking.common.exception.ResourceNotFoundException;
import com.wokioki.hotelbooking.room.Room;
import com.wokioki.hotelbooking.room.RoomRepository;
import com.wokioki.hotelbooking.room.RoomStatus;
import com.wokioki.hotelbooking.user.User;
import com.wokioki.hotelbooking.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public BookingResponse createBooking(Long userId, CreateBookingRequest request) {
        validateDateRange(request.checkIn(), request.checkOut());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", request.roomId()));

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new BadRequestException("Room is not available for booking");
        }

        boolean hasConflict = bookingRepository.existsByRoomIdAndStatusInAndCheckInLessThanAndCheckOutGreaterThan(
                room.getId(),
                List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED),
                request.checkOut(),
                request.checkIn()
        );

        if (hasConflict) {
            throw new BookingConflictException("Room is already booked for the selected date range");
        }

        BigDecimal totalPrice = calculateTotalPrice(room, request.checkIn(), request.checkOut());

        Booking booking = Booking.builder()
                .user(user)
                .room(room)
                .checkIn(request.checkIn())
                .checkOut(request.checkOut())
                .status(BookingStatus.PENDING)
                .totalPrice(totalPrice)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        return toResponse(savedBooking);
    }

    private void validateDateRange(LocalDate checkIn, LocalDate checkOut){
        if (!checkOut.isAfter(checkIn)) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }

        if (checkIn.isBefore(LocalDate.now())) {
            throw new BadRequestException("Check-in date cannot be in the past");
        }
    }


    private BigDecimal calculateTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return room.getPricePerNight().multiply(BigDecimal.valueOf(nights));
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUser().getId(),
                booking.getRoom().getId(),
                booking.getRoom().getRoomNumber(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getStatus(),
                booking.getTotalPrice(),
                booking.getCreatedAt()
        );
    }
}
