package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.VenueDTO;
import com.shubham.event_manager.entity.Venue;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.mapper.VenueMapper;
import com.shubham.event_manager.repository.VenueRepository;
import com.shubham.event_manager.service.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;
    private final GeocodingService geocodingService;

    @Override
    public List<VenueDTO> getAllVenues() {
        return venueRepository.findAll()
                .stream()
                .map(venueMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VenueDTO getVenueById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Venue not found with id: " + id));
        return venueMapper.toDTO(venue);
    }

    @Override
    @Transactional
    public VenueDTO createVenue(VenueDTO venueDTO) {
        if (venueRepository.existsByNameAndCity(
                venueDTO.getName(), venueDTO.getCity())) {
            throw new IllegalArgumentException(
                    "Venue already exists: " + venueDTO.getName()
                            + " in " + venueDTO.getCity());
        }

        Venue venue = venueMapper.toEntity(venueDTO);

        // If user provided coordinates manually, use them
        // Otherwise try to auto-geocode
        if (venueDTO.getLatitude() != null
                && venueDTO.getLongitude() != null) {
            venue.setLatitude(venueDTO.getLatitude());
            venue.setLongitude(venueDTO.getLongitude());
            log.info("Using manually provided coordinates: " +
                            "{}, {}", venueDTO.getLatitude(),
                    venueDTO.getLongitude());
        } else {
            // Auto-geocode the address
            geocodingService.getCoordinates(
                            venueDTO.getAddress(),
                            venueDTO.getCity(),
                            venueDTO.getState())
                    .ifPresent(coords -> {
                        venue.setLatitude(coords[0]);
                        venue.setLongitude(coords[1]);
                    });
        }

        Venue saved = venueRepository.save(venue);
        log.info("Venue created: {} in {} [lat={}, lon={}]",
                saved.getName(), saved.getCity(),
                saved.getLatitude(), saved.getLongitude());
        return venueMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public VenueDTO updateVenue(Long id, VenueDTO venueDTO) {
        Venue existing = venueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Venue not found with id: " + id));
        venueMapper.updateFromDTO(venueDTO, existing);
        return venueMapper.toDTO(venueRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteVenue(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Venue not found with id: " + id));
        venueRepository.delete(venue);
        log.info("Venue deleted: {}", id);
    }

    @Override
    public List<VenueDTO> getVenuesByCity(String city) {
        return venueRepository.findByCityIgnoreCase(city)
                .stream()
                .map(venueMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VenueDTO> getVenuesByMinCapacity(
            Integer minCapacity) {
        return venueRepository
                .findByCapacityGreaterThanEqual(minCapacity)
                .stream()
                .map(venueMapper::toDTO)
                .collect(Collectors.toList());
    }
}
