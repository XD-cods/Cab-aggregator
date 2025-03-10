Feature: Car API

  Scenario: Create a new car
    Given a configured car service
    Given a car request body:
    """
    {
        "color": "Black",
        "car_brand": "Mercedes",
        "car_number": "9XG81OK"
    }
    """

    When I send a request to create a new car
    Then I should get a car response with status 201

  Scenario: Create a new car, car already exists by car number
    Given a configured car service
    Given a car request body:
    """
    {
        "color": "Black",
        "car_brand": "Mercedes",
        "car_number": "9XG81OK"
    }
    """
    When I send a request to create a new car
    Then I should get a car response with status 409

  Scenario: Create a new car, car request not a valid
    Given a configured car service
    Given a car request body:
    """
    {
        "color": "Black",
        "car_brand": "",
        "car_number": "9YC81OK"
    }
    """
    When I send a request to create a new car
    Then I should get a car response with status 400

  Scenario: Get car by ID
    Given a configured car service
    Given a car by id: 1
    When I send a request to get the car by ID
    Then I should get the car details in the response
    And I should get a car response with status 200

  Scenario: Get car by ID, car not found
    Given a configured car service
    Given a car by id: 0
    When I send a request to get the car by ID
    Then  I should get a car response with status 404

  Scenario: Update car details
    Given a configured car service
    Given a car by id: 1
    Given a car request body:
    """
    {
        "color": "Red",
        "car_brand": "Tesla",
        "car_number": "9XG81OK"
    }
    """
    When I send a request to update the car details
    Then the car details should be updated
    And I should get a car response with status 200

  Scenario: Delete car
    Given a configured car service
    Given a car by id: 1
    When I send a request to delete the car
    Then I should get a car response with status 200

  Scenario: Delete car, car not found
    Given a configured car service
    Given a car by id: 0
    When I send a request to delete the car
    Then I should get a car response with status 404