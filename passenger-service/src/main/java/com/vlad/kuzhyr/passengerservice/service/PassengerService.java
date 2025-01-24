package com.vlad.kuzhyr.passengerservice.service;

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

@Service
@RequiredArgsConstructor
public class PassengerService {

  private final PassengerRepository passengerRepository;
  private final PassengerMapper passengerMapper;

  public PassengerResponse getPassengerById(Long id) {
    Passenger existPassenger = passengerRepository.findPassengerByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new NotFoundException(String.format(PassengerServiceConstant.NOT_FOUND_MESSAGE, id)));

    return passengerMapper.toResponse(existPassenger);
  }

  @Transactional
  public PassengerResponse createPassenger(PassengerRequest passengerRequest) {
    if (passengerRepository.existsPassengersByEmailOrPhone(passengerRequest.email(), passengerRequest.phone())) {
      throw new ConflictException(
              String.format(PassengerServiceConstant.CONFLICT_BY_ID_MESSAGE, passengerRequest.email()));
    }

    Passenger newPassenger = passengerMapper.toEntity(passengerRequest);
    Passenger savedPassenger = passengerRepository.save(newPassenger);
    return passengerMapper.toResponse(savedPassenger);
  }

  @Transactional
  public PassengerResponse updatePassenger(Long id, PassengerRequest passengerRequest) {
    Passenger existPassenger = passengerRepository.findPassengerByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new NotFoundException(String.format(PassengerServiceConstant.NOT_FOUND_MESSAGE, id)));

    passengerMapper.updateFromRequest(passengerRequest, existPassenger);
    passengerRepository.save(existPassenger);
    return passengerMapper.toResponse(existPassenger);
  }

  @Transactional
  public Boolean deletePassengerById(Long id) {
    Passenger existPassenger = passengerRepository.findPassengerByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new NotFoundException(String.format(PassengerServiceConstant.NOT_FOUND_MESSAGE, id)));

    existPassenger.setIsEnabled(Boolean.FALSE);
    passengerRepository.save(existPassenger);
    return Boolean.TRUE;
  }

}
