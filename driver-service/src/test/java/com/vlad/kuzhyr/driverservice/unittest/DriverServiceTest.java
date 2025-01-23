package com.vlad.kuzhyr.driverservice.unittest;

import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.service.DriverService;
import com.vlad.kuzhyr.driverservice.utility.mapper.DriverMapper;
import com.vlad.kuzhyr.driverservice.web.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {

  @Mock
  private DriverRepository driverRepository;

  @Spy
  private DriverMapper driverMapper;

  @InjectMocks
  private DriverService driverService;

  Driver testDriver;

  DriverResponse testDriverResponse;

  DriverRequest testDriverRequest;

  public DriverServiceTest() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    driverMapper = new DriverMapper(modelMapper);
  }

  @BeforeEach
  void setUp() {
    testDriver = Driver.builder()
            .id(1L)
            .email("email@email.com")
            .phone("123456789")
            .firstName("firstName")
            .lastName("lastName")
            .gender("Male")
            .carId(1L)
            .build();

    testDriverResponse = DriverResponse.builder()
            .id(1L)
            .email("email@email.com")
            .phone("123456789")
            .firstName("firstName")
            .lastName("lastName")
            .carId(1L)
            .build();

    testDriverRequest = DriverRequest.builder()
            .email("test@email.com")
            .phone("223456789")
            .firstName("testFirstName")
            .lastName("testLastName")
            .carId(2L)
            .build();
  }

  @Test
  public void getDriverById_shouldReturnDriverResponseTest() {
    Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.of(testDriver));

    DriverResponse driverResponse = driverService.getDriverById(1L);

    Assertions.assertEquals(testDriver.getId(), driverResponse.getId());
    Assertions.assertEquals(testDriver.getEmail(), driverResponse.getEmail());
    Assertions.assertEquals(testDriver.getCarId(), driverResponse.getCarId());
    Assertions.assertEquals(testDriver.getGender(), driverResponse.getGender());
    Assertions.assertEquals(testDriver.getFirstName(), driverResponse.getFirstName());
    Assertions.assertEquals(testDriver.getLastName(), driverResponse.getLastName());
  }

  public void getDriverById_shouldThrowNotFoundExceptionTest() {
    Mockito.when(driverRepository.findById(1L)).thenReturn(Optional.empty());

  }
}
