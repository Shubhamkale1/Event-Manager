package com.shubham.event_manager.mapper;

import com.shubham.event_manager.dto.VenueDTO;
import com.shubham.event_manager.entity.Venue;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {

    public VenueDTO toDTO(Venue venue) {
        if (venue == null) return null;
        VenueDTO dto = new VenueDTO();
        dto.setId(venue.getId());
        dto.setName(venue.getName());
        dto.setAddress(venue.getAddress());
        dto.setCity(venue.getCity());
        dto.setState(venue.getState());
        dto.setPincode(venue.getPincode());
        dto.setCapacity(venue.getCapacity());
        dto.setLatitude(venue.getLatitude());
        dto.setLongitude(venue.getLongitude());
        dto.setCreatedAt(venue.getCreatedAt());
        return dto;
    }

    public Venue toEntity(VenueDTO dto) {
        if (dto == null) return null;
        return Venue.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .capacity(dto.getCapacity())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

    public void updateFromDTO(VenueDTO dto, Venue venue) {
        venue.setName(dto.getName());
        venue.setAddress(dto.getAddress());
        venue.setCity(dto.getCity());
        venue.setState(dto.getState());
        venue.setPincode(dto.getPincode());
        venue.setCapacity(dto.getCapacity());
        venue.setLatitude(dto.getLatitude());
        venue.setLongitude(dto.getLongitude());
    }

}
