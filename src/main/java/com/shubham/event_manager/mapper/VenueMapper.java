package com.shubham.event_manager.mapper;

import com.shubham.event_manager.dto.VenueDTO;
import com.shubham.event_manager.entity.Venue;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {

    public VenueDTO toDTO(Venue venue) {
        VenueDTO dto = new VenueDTO();
        dto.setId(venue.getId());
        dto.setName(venue.getName());
        dto.setAddress(venue.getAddress());
        dto.setCity(venue.getCity());
        dto.setState(venue.getState());
        dto.setPincode(venue.getPincode());
        dto.setCapacity(venue.getCapacity());
        dto.setCreatedAt(venue.getCreatedAt());

        // Generate Google Maps link if coordinates exist
        // User clicks this and sees the venue on a map
        if (venue.getLatitude() != null
                && venue.getLongitude() != null) {
            dto.setMapUrl(
                    "https://www.google.com/maps?q="
                            + venue.getLatitude()
                            + ","
                            + venue.getLongitude()
            );
        } else {
            dto.setMapUrl(
                    "https://www.google.com/maps/search/"
                            + venue.getName().replace(" ", "+")
                            + "+"
                            + venue.getCity().replace(" ", "+")
            );
        }

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
