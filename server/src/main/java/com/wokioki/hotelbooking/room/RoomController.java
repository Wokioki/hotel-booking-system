package com.wokioki.hotelbooking.room;

import com.wokioki.hotelbooking.room.dto.RoomResponse;
import com.wokioki.hotelbooking.room.dto.RoomTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/rooms")
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/rooms/{id}")
    public RoomResponse getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @GetMapping("/room-types")
    public List<RoomTypeResponse> getAllRoomTypes() {
        return roomService.getAllRoomTypes();
    }
}