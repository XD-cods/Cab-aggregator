package com.vlad.kuzhyr.passengerservice.service.impl;

import com.vlad.kuzhyr.passengerservice.exception.PassengerNotFoundException;
import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.service.PassengerService;
import com.vlad.kuzhyr.passengerservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.passengerservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.passengerservice.utility.mapper.PassengerMapper;
import com.vlad.kuzhyr.passengerservice.utility.validator.PassengerValidator;
import com.vlad.kuzhyr.passengerservice.web.dto.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PageResponse;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PassengerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;
    private final PageResponseMapper pageResponseMapper;
    private final PassengerValidator passengerValidator;

    @Override
    public PassengerResponse getPassengerById(Long id) {
        log.debug("getPassengerById: Entering method. Passenger id: {}", id);

        Passenger existingPassenger = getExistingPassengerById(id);

        log.info("getPassengerById: Passenger found. Passenger id: {}", id);
        return passengerMapper.toResponse(existingPassenger);
    }

    @Override
    public PageResponse<PassengerResponse> getPassengers(Integer currentPage, Integer limit) {
        log.debug("getPassengers: Entering method. Current page: {}, limit: {}", currentPage, limit);

        PageRequest pageRequest = PageRequest.of(currentPage, limit);
        Page<Passenger> passengersPage = passengerRepository.findAll(pageRequest);

        PageResponse<PassengerResponse> pageResponse = pageResponseMapper.toPageResponse(
            passengersPage,
            currentPage,
            passengerMapper::toResponse
        );

        log.info("getPassengers: Page of passengers retrieved. {}", pageResponse);
        return pageResponse;
    }

    @Transactional
    @Override
    public PassengerResponse createPassenger(PassengerRequest passengerRequest) {
        log.debug("createPassenger: Entering method. {}", passengerRequest);

        String passengerRequestEmail = passengerRequest.email();
        String passengerRequestPhone = passengerRequest.phone();

        passengerValidator.validatePassengerEmailAndPhone(passengerRequestEmail, passengerRequestPhone);

        Passenger newPassenger = passengerMapper.toEntity(passengerRequest);
        Passenger savedPassenger = passengerRepository.save(newPassenger);

        log.info("createPassenger: Passenger created successfully. Passenger id: {}",
            savedPassenger.getId());
        return passengerMapper.toResponse(savedPassenger);
    }

    @Transactional
    @Override
    public PassengerResponse updatePassenger(Long id, PassengerRequest passengerRequest) {
        log.debug("updatePassenger: Entering method. Passenger id: {}, {}", id, passengerRequest);

        Passenger existingPassenger = getExistingPassengerById(id);

        passengerMapper.updateFromRequest(passengerRequest, existingPassenger);
        Passenger savedPassenger = passengerRepository.save(existingPassenger);

        log.info("updatePassenger: Passenger updated successfully. Passenger id: {}", id);
        return passengerMapper.toResponse(savedPassenger);
    }

    @Transactional
    @Override
    public Boolean deletePassengerById(Long id) {
        log.debug("deletePassengerById: Entering method. Passenger id: {}", id);

        Passenger existingPassenger = getExistingPassengerById(id);

        existingPassenger.setIsEnabled(Boolean.FALSE);
        passengerRepository.save(existingPassenger);

        log.info("deletePassengerById: Passenger deleted successfully. Passenger id: {}", id);
        return Boolean.TRUE;
    }

    private Passenger getExistingPassengerById(Long id) {
        log.debug("getExistingPassengerById: Attempting to find passenger. Passenger id: {}", id);

        return passengerRepository.findPassengerByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> {
                log.error("getExistingPassengerById: Passenger not found. Passenger id: {}", id);
                return new PassengerNotFoundException(
                    ExceptionMessageConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(id)
                );
            });
    }
}
