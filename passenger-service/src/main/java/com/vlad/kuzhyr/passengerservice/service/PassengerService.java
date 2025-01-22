package com.vlad.kuzhyr.passengerservice.service;

import com.vlad.kuzhyr.passengerservice.exception.BadRequestException;
import com.vlad.kuzhyr.passengerservice.exception.ConflictException;
import com.vlad.kuzhyr.passengerservice.exception.NotFoundException;
import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.utility.constant.PassengerServiceConstant;
import com.vlad.kuzhyr.passengerservice.utility.mapper.PassengerMapper;
import com.vlad.kuzhyr.passengerservice.web.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.response.PassengerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PassengerService {
  private final PassengerRepository passengerRepository;
  private final PassengerMapper passengerMapper;


  public PassengerResponse getPassengerById(Long id) {
    Passenger existPassenger = passengerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(PassengerServiceConstant.NOT_FOUND_MESSAGE, id)));

    if (!existPassenger.getIsEnabled()) {
      throw new BadRequestException(String.format(PassengerServiceConstant.BAD_REQUEST_MESSAGE, id));
    }

    return passengerMapper.mapPassengerToPassengerResponse(existPassenger);
  }

  @Transactional
  public PassengerResponse createPassenger(PassengerRequest passengerRequest) {
    Optional<Passenger> existPassenger = passengerRepository.findPassengerByEmail(((passengerRequest.getEmail())));
    if (existPassenger.isPresent() && existPassenger.get().getIsEnabled()) {
      throw new ConflictException(String.format(PassengerServiceConstant.CONFLICT_BY_ID_MESSAGE, passengerRequest.getEmail()));
    }

    Passenger newPassenger = Passenger.builder()
            .email(passengerRequest.getEmail())
            .phone(passengerRequest.getPhone())
            .firstName(passengerRequest.getFirstName())
            .lastName(passengerRequest.getLastName())
            .build();

    return passengerMapper.mapPassengerToPassengerResponse(passengerRepository.save(newPassenger));
  }

  @Transactional
  public PassengerResponse updatePassenger(Long id, PassengerRequest passengerRequest) {
    Passenger existPassenger = passengerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(PassengerServiceConstant.NOT_FOUND_MESSAGE, id)));

    if (!existPassenger.getIsEnabled()) {
      throw new BadRequestException(String.format(PassengerServiceConstant.BAD_REQUEST_MESSAGE, id));
    }

    passengerMapper.updateExistPassengerFromPassengerRequest(existPassenger, passengerRequest);
    passengerRepository.save(existPassenger);
    return passengerMapper.mapPassengerToPassengerResponse(existPassenger);
  }

  @Transactional
  public Boolean deletePassengerById(Long id) {
    Passenger existPassenger = passengerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(PassengerServiceConstant.NOT_FOUND_MESSAGE, id)));

    if (!existPassenger.getIsEnabled()) {
      throw new BadRequestException(String.format(PassengerServiceConstant.BAD_REQUEST_MESSAGE, id));
    }

    existPassenger.setIsEnabled(Boolean.FALSE);
    passengerRepository.save(existPassenger);
    return Boolean.TRUE;
  }
}
