package com.wokioki.hotelbooking.config;

import com.wokioki.hotelbooking.room.Room;
import com.wokioki.hotelbooking.room.RoomRepository;
import com.wokioki.hotelbooking.room.RoomStatus;
import com.wokioki.hotelbooking.room.RoomType;
import com.wokioki.hotelbooking.room.RoomTypeRepository;
import com.wokioki.hotelbooking.user.Role;
import com.wokioki.hotelbooking.user.User;
import com.wokioki.hotelbooking.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedRoomTypesAndRooms();
    }

    private void seedUsers() {
        createUserIfNotExists(
                "admin@hotel.com",
                "admin123",
                "Admin",
                "User",
                Role.ADMIN
        );

        createUserIfNotExists(
                "manager@hotel.com",
                "manager123",
                "Manager",
                "User",
                Role.MANAGER
        );

        createUserIfNotExists(
                "customer@hotel.com",
                "customer123",
                "Customer",
                "User",
                Role.CUSTOMER
        );
    }

    private void createUserIfNotExists(
            String email,
            String rawPassword,
            String firstName,
            String lastName,
            Role role
    ) {
        if (userRepository.existsByEmail(email)) {
            return;
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .build();

        userRepository.save(user);
    }

    private void seedRoomTypesAndRooms() {
        RoomType standard = createRoomTypeIfNotExists(
                "Standard",
                "Comfortable room for one or two guests."
        );

        RoomType deluxe = createRoomTypeIfNotExists(
                "Deluxe",
                "Larger room with additional comfort and better view."
        );

        RoomType suite = createRoomTypeIfNotExists(
                "Suite",
                "Premium room with separate living area."
        );

        createRoomIfNotExists("101", standard, 2, new BigDecimal("80.00"), RoomStatus.AVAILABLE);
        createRoomIfNotExists("102", standard, 2, new BigDecimal("85.00"), RoomStatus.AVAILABLE);
        createRoomIfNotExists("201", deluxe, 3, new BigDecimal("130.00"), RoomStatus.AVAILABLE);
        createRoomIfNotExists("202", deluxe, 3, new BigDecimal("140.00"), RoomStatus.MAINTENANCE);
        createRoomIfNotExists("301", suite, 4, new BigDecimal("220.00"), RoomStatus.AVAILABLE);
    }

    private RoomType createRoomTypeIfNotExists(String name, String description) {
        return roomTypeRepository.findByName(name)
                .orElseGet(() -> roomTypeRepository.save(
                        RoomType.builder()
                                .name(name)
                                .description(description)
                                .build()
                ));
    }

    private void createRoomIfNotExists(
            String roomNumber,
            RoomType roomType,
            int capacity,
            BigDecimal pricePerNight,
            RoomStatus status
    ) {
        if (roomRepository.existsByRoomNumber(roomNumber)) {
            return;
        }

        Room room = Room.builder()
                .roomNumber(roomNumber)
                .roomType(roomType)
                .capacity(capacity)
                .pricePerNight(pricePerNight)
                .status(status)
                .description(roomType.getName() + " room number " + roomNumber)
                .build();

        roomRepository.save(room);
    }
}