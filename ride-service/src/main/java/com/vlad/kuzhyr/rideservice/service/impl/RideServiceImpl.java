package com.vlad.kuzhyr.rideservice.service.impl;

import com.vlad.kuzhyr.rideservice.exception.RideNotFoundException;
import com.vlad.kuzhyr.rideservice.exception.RidesNotFoundByDriverIdException;
import com.vlad.kuzhyr.rideservice.exception.RidesNotFoundByPassengerIdException;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import com.vlad.kuzhyr.rideservice.persistence.repository.RideRepository;
import com.vlad.kuzhyr.rideservice.service.RideService;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.rideservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.rideservice.utility.mapper.RideMapper;
import com.vlad.kuzhyr.rideservice.web.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.request.UpdateRideStatusRequest;
import com.vlad.kuzhyr.rideservice.web.response.PageResponse;
import com.vlad.kuzhyr.rideservice.web.response.RideResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

  private final RideRepository rideRepository;
  private final RideMapper rideMapper;
  private final PageResponseMapper pageResponseMapper;

  @Override
  public RideResponse getRideById(Long id) {
    Ride existingRide = getExistingRideById(id);
    return rideMapper.toResponse(existingRide);
  }

  @Override
  public PageResponse<RideResponse> getAllRidesByDriverId(Long driverId, Integer offset, Integer limit) {
    if (!rideRepository.existsRidesByDriverId(driverId)) {
      throw new RidesNotFoundByDriverIdException(
              ExceptionMessageConstant.RIDES_NOT_FOUND_BY_DRIVER_ID_MESSAGE.formatted(driverId)
      );
    }

    Page<Ride> existingRidesByDriverId = rideRepository
            .findByDriverId(driverId, PageRequest.of(offset, limit));

    return pageResponseMapper.toPageResponse(
            existingRidesByDriverId,
            offset,
            rideMapper::toResponse
    );
  }

  @Override
  public PageResponse<RideResponse> getAllRidesByPassengerId(
          Long passengerId,
          Integer offset,
          Integer limit
  ) {
    if (!rideRepository.existsRidesByPassengerId(passengerId)) {
      throw new RidesNotFoundByPassengerIdException(
              ExceptionMessageConstant.RIDES_NOT_FOUND_BY_PASSENGER_ID_MESSAGE.formatted(passengerId)
      );
    }

    Page<Ride> existingRidesByPassengerId = rideRepository
            .findByPassengerId(passengerId, PageRequest.of(offset, limit));

    return pageResponseMapper.toPageResponse(
            existingRidesByPassengerId,
            offset,
            rideMapper::toResponse
    );
  }

  @Override
  public PageResponse<RideResponse> getAllRides(Integer offset, Integer limit) {
    Page<Ride> existingRidesId = rideRepository
            .findAll(PageRequest.of(offset, limit));

    return pageResponseMapper.toPageResponse(
            existingRidesId,
            offset,
            rideMapper::toResponse
    );
  }

  @Override
  @Transactional
  public RideResponse updateRide(Long id, RideRequest rideRequest) {
    Ride existingRide = getExistingRideById(id);

    rideMapper.updateFromRequest(rideRequest, existingRide);
    Ride savedRide = rideRepository.save(existingRide);
    return rideMapper.toResponse(savedRide);
  }

  @Override
  @Transactional
  public RideResponse updateRideStatus(Long id, UpdateRideStatusRequest rideRequest) {
    Ride existingRide = getExistingRideById(id);

    RideStatus rideStatus = rideRequest.rideStatus();
    switch (rideStatus) {
      case ACCEPTED -> existingRide.setStartTime(LocalDateTime.now());
      case COMPLETED -> existingRide.setFinishTime(LocalDateTime.now());
    }

    existingRide.setRideStatus(rideStatus);
    Ride savedRide = rideRepository.save(existingRide);
    return rideMapper.toResponse(savedRide);
  }

  @Override
  @Transactional
  public RideResponse createRide(RideRequest rideRequest) {
    Ride ride = rideMapper.toEntity(rideRequest);
    Ride savedRide = rideRepository.save(ride);
    return rideMapper.toResponse(savedRide);
  }

  private Ride getExistingRideById(Long id) {
    return rideRepository.findById(id)
            .orElseThrow(() -> new RideNotFoundException(
                    ExceptionMessageConstant.RIDE_NOT_FOUND_MESSAGE.formatted(id)
            ));
  }

}
