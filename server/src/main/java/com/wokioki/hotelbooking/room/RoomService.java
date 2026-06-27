package com.wokioki.hotelbooking.room;

import com.wokioki.hotelbooking.common.exception.ResourceNotFoundException;
import com.wokioki.hotelbooking.room.dto.RoomResponse;
import com.wokioki.hotelbooking.room.dto.RoomTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomMapper roomMapper;

    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toRoomResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id));

        return roomMapper.toRoomResponse(room);
    }

    @Transactional(readOnly = true)
    public List<RoomTypeResponse> getAllRoomTypes() {
        return roomTypeRepository.findAll()
                .stream()
                .map(roomMapper::toRoomTypeResponse)
                .toList();
    }
}