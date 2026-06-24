package com.wokioki.hotelbooking.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByRoomId(Long roomId);

    boolean existsByRoomIdAndStatusInAndCheckInLessThanAndCheckOutGreaterThan(
            Long roomId,
            List<BookingStatus> statuses,
            LocalDate checkOut,
            LocalDate checkIn
    );
}
