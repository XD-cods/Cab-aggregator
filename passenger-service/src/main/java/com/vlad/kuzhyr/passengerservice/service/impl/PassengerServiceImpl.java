package com.vlad.kuzhyr.passengerservice.service.impl;

import com.vlad.kuzhyr.passengerservice.exception.PassengerAlreadyExistsException;
import com.vlad.kuzhyr.passengerservice.exception.PassengerNotFoundException;
import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.service.PassengerService;
import com.vlad.kuzhyr.passengerservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.passengerservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.passengerservice.utility.mapper.PassengerMapper;
import com.vlad.kuzhyr.passengerservice.web.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.response.PageResponse;
import com.vlad.kuzhyr.passengerservice.web.response.PassengerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;
    private final PageResponseMapper pageResponseMapper;

    @Override
    public PassengerResponse getPassengerById(Long id) {
        Passenger existPassenger = passengerRepository.findPassengerByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new PassengerNotFoundException(
                ExceptionMessageConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(id)
            ));

        return passengerMapper.toResponse(existPassenger);
    }

    @Override
    public PageResponse<PassengerResponse> getPassengers(Integer currentPage, Integer limit) {
        PageRequest pageRequest = PageRequest.of(currentPage, limit);
        Page<Passenger> passengersPage = passengerRepository.findAll(pageRequest);

        return pageResponseMapper.toPageResponse(
            passengersPage,
            currentPage,
            passengerMapper::toResponse
        );
    }

    @Transactional
    @Override
    public PassengerResponse createPassenger(PassengerRequest passengerRequest) {
        String passengerRequestEmail = passengerRequest.email();
        String passengerRequestPhone = passengerRequest.phone();

        if (passengerRepository.existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail)) {
            throw new PassengerAlreadyExistsException(
                ExceptionMessageConstant.PASSENGER_ALREADY_EXISTS_BY_EMAIL_MESSAGE.formatted(passengerRequestEmail)
            );
        }

        if (passengerRepository.existsPassengerByPhoneAndIsEnabledTrue(passengerRequestPhone)) {
            throw new PassengerAlreadyExistsException(
                ExceptionMessageConstant.PASSENGER_ALREADY_EXISTS_BY_PHONE_MESSAGE.formatted(passengerRequestPhone)
            );
        }

        Passenger newPassenger = passengerMapper.toEntity(passengerRequest);
        Passenger savedPassenger = passengerRepository.save(newPassenger);
        return passengerMapper.toResponse(savedPassenger);
    }

    @Transactional
    @Override
    public PassengerResponse updatePassenger(Long id, PassengerRequest passengerRequest) {
        Passenger existPassenger = passengerRepository.findPassengerByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new PassengerNotFoundException(
                ExceptionMessageConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(id)
            ));

        passengerMapper.updateFromRequest(passengerRequest, existPassenger);
        passengerRepository.save(existPassenger);
        return passengerMapper.toResponse(existPassenger);
    }

    @Transactional
    @Override
    public Boolean deletePassengerById(Long id) {
        Passenger existPassenger = passengerRepository.findPassengerByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new PassengerNotFoundException(
                ExceptionMessageConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(id))
            );

        existPassenger.setIsEnabled(Boolean.FALSE);
        passengerRepository.save(existPassenger);
        return Boolean.TRUE;
    }

}