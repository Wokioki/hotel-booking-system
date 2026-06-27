package com.wokioki.hotelbooking.room;

import com.wokioki.hotelbooking.room.dto.RoomResponse;
import com.wokioki.hotelbooking.room.dto.RoomTypeResponse;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomResponse toRoomResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomType().getId(),
                room.getRoomType().getName(),
                room.getCapacity(),
                room.getPricePerNight(),
                room.getStatus(),
                room.getDescription()
        );
    }

    public RoomTypeResponse toRoomTypeResponse(RoomType roomType) {
        return new RoomTypeResponse(
                roomType.getId(),
                roomType.getName(),
                roomType.getDescription()
        );
    }
}