Feature: Ride Service End-to-End Tests

  Scenario: Create a new ride
    Given configurator server
    When I create a ride with the following details:
    """
    {
      "departure_address":"Сосновая улица, 52, Брест, Беларусь",
      "destination_address":"Шоссейная улица, 2/8, Брест, Беларусь",
      "driver_id": 1,
      "passenger_id": 1
    }
    """
    Then the response status should be 201

  Scenario: Create a new ride, not valid request
    Given configurator server
    When I create a ride with the following details:
    """
    {
      "departure_address":"",
      "destination_address":"",
      "driver_id": 0,
      "passenger_id": 1
    }
    """
    Then the response status should be 400

  Scenario: Get a ride by ID
    Given configurator server
    Given a ride exists
    When I get the ride with ID 1
    Then the response status should be 200
    And the response should contain the ride details

  Scenario: Get a ride by ID, not found by id
    Given configurator server
    Given a ride exists
    When I get the ride with ID 0
    Then the response status should be 404

  Scenario: Update a ride by ID
    Given configurator server
    Given a ride exists
    When I update the ride with the following details:
    """
    {
      "departure_address":"Address A",
      "destination_address":"Address B"
    }
    """
    Then the response status should be 200
    And the response should contain the updated ride details

  Scenario: Update a ride by ID, not valid request
    Given configurator server
    Given a ride exists
    When I update the ride with the following details:
    """
    {
      "departure_address":"",
      "destination_address":"Address B"
    }
    """
    Then the response status should be 400

  Scenario: Update a ride status by ID
    Given configurator server
    Given a ride exists
    When I update the ride status to "WAITING_FOR_DRIVER"
    Then the response status should be 200
    And the response should contain the updated ride status

  Scenario: Update a ride status by ID, transition status exception
    Given configurator server
    Given a ride exists
    When I update the ride status to "COMPLETED"
    Then the response status should be 400

  Scenario: Get all rides by driver ID
    Given configurator server
    Given a ride exists with driver ID 1
    When I request get all rides by driver ID 1, current_page 0 and limit 10
    Then the response status should be 200
    And the response should contain at least one ride

  Scenario: Get all rides by passenger ID
    Given configurator server
    Given a ride exists by passenger ID 1
    When I request get all rides by passenger ID 1, current_page 0 and limit 10
    Then the response status should be 200
    And the response should contain at least one ride

  Scenario: Get all rides
    Given configurator server
    Given at least one ride exists
    When I request get all rides, current_page 0 and limit 10
    Then the response status should be 200
    And the response should contain at least one ride
