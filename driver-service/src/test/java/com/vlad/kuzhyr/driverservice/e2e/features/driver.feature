Feature: Driver API

  Scenario: Create a new driver
    Given a driver request body:
    """
    {
        "first_name": "John",
        "last_name": "Doe",
        "email": "jo.doe@test.com",
        "gender": "MALE",
        "phone": "+375333234567",
        "car_ids": []
    }
    """
    When I send a request to create a new driver
    Then I should get a driver response with status 201

  Scenario: Create a new driver, driver already exists by email
    Given a driver request body:
    """
    {
        "first_name": "John",
        "last_name": "Doe",
        "email": "jo.doe@test.com",
        "gender": "MALE",
        "phone": "+375333334567",
        "car_ids": []
    }
    """
    When I send a request to create a new driver
    Then I should get a driver response with status 409

  Scenario: Create a new driver, driver already exists by phone
    Given a driver request body:
    """
    {
        "first_name": "John",
        "last_name": "Doe",
        "email": "jo.does@test.com",
        "gender": "MALE",
        "phone": "+375333234567",
        "car_ids": []
    }
    """
    When I send a request to create a new driver
    Then I should get a driver response with status 409

  Scenario: Create a new driver, driver request not a valid
    Given a driver request body:
    """
    {
        "first_name": "",
        "last_name": "Doe",
        "email": "jo.does@test.com",
        "gender": "MALE",
        "phone": "+375333234567",
        "car_ids": []
    }
    """
    When I send a request to create a new driver
    Then I should get a driver response with status 400

  Scenario: Get driver by ID
    Given a driver by id: 1
    When I send a request to get the driver by ID
    Then I should get the driver details in the response
    And I should get a driver response with status 200

  Scenario: Get driver by ID, driver not found
    Given a driver by id: 0
    When I send a request to get the driver by ID
    Then  I should get a driver response with status 404

  Scenario: Update driver details
    Given a driver by id: 1
    Given a driver request body:
    """
    {
        "first_name": "Updated first name",
        "last_name": "Updated last name",
        "email": "updated@test.com",
        "gender": "MALE",
        "phone": "+375339985178",
        "car_ids": []
    }
    """
    When I send a request to update the driver details
    Then the driver details should be updated
    And I should get a driver response with status 200

  Scenario: Delete driver
    Given a driver by id: 1
    When I send a request to delete the driver
    Then I should get a driver response with status 200

  Scenario: Delete driver, driver not found
    Given a driver by id: 0
    When I send a request to delete the driver
    Then I should get a driver response with status 404