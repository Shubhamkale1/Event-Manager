package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.VenueDTO;
import java.util.List;

public interface VenueService {
    List<VenueDTO> getAllVenues();
    VenueDTO getVenueById(Long id);
    VenueDTO createVenue(VenueDTO venueDTO);
    VenueDTO updateVenue(Long id, VenueDTO venueDTO);
    void deleteVenue(Long id);
    List<VenueDTO> getVenuesByCity(String city);
    List<VenueDTO> getVenuesByMinCapacity(Integer minCapacity);
}
