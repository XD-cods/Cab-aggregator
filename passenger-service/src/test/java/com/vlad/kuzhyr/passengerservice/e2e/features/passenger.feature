Feature: Passenger API

  Scenario: Create a new passenger
    Given a passenger request body:
    """
    {
        "first_name": "Vlad",
        "last_name": "Kuzhyr",
        "phone": "+375339980129",
        "email": "vkuzir7@gmail.com"
    }
    """
    When I send a request to create a new passenger
    Then I should get a passenger response with status 201

  Scenario: Create a new passenger, passenger already exists by email
    Given a passenger request body:
    """
    {
        "first_name": "Vlad",
        "last_name": "Kuzhyr",
        "phone": "+375339980129",
        "email": "vkuzir7@gmail.com"
    }
    """
    When I send a request to create a new passenger
    Then I should get a passenger response with status 409

  Scenario: Create a new passenger, passenger request not valid
    Given a passenger request body:
    """
    {
        "first_name": "",
        "last_name": "Kuzhyr",
        "phone": "+375339980129",
        "email": "vkuzir7@gmail.com"
    }
    """
    When I send a request to create a new passenger
    Then I should get a passenger response with status 400

  Scenario: Get passenger by ID
    Given a passenger by id: 1
    When I send a request to get the passenger by ID
    Then I should get the passenger details in the response
    And I should get a passenger response with status 200

  Scenario: Get passenger by ID, passenger not found
    Given a passenger by id: 0
    When I send a request to get the passenger by ID
    Then I should get a passenger response with status 404

  Scenario: Update passenger details
    Given a passenger by id: 1
    Given a passenger request body:
    """
    {
        "first_name": "UpdatedFirstName",
        "last_name": "UpdatedLastName",
        "phone": "+375339980129",
        "email": "vkuzir7@gmail.com"
    }
    """
    When I send a request to update the passenger details
    Then the passenger details should be updated
    And I should get a passenger response with status 200

  Scenario: Delete passenger
    Given a passenger by id: 1
    When I send a request to delete the passenger
    Then I should get a passenger response with status 200

  Scenario: Delete passenger, passenger not found
    Given a passenger by id: 0
    When I send a request to delete the passenger
    Then I should get a passenger response with status 404