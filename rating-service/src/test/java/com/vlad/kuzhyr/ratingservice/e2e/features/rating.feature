Feature: Rating API

  Scenario: Create a new rating
    Given ride info exists with ride id 1
    Given a rating request body:
    """
    {
        "ride_id": 1,
        "rating": 5.0,
        "comment": "Great service!",
        "rated_by": "PASSENGER"
    }
    """
    When I send a request to create a new rating
    Then I should get a rating response with status 201

  Scenario: Create a new rating, rating request not valid (invalid rating)
    Given ride info exists with ride id 1
    Given a rating request body:
    """
    {
        "ride_id": 1,
        "rating": 6.0,
        "comment": "Great service!",
        "rated_by": "PASSENGER"
    }
    """
    When I send a request to create a new rating
    Then I should get a rating response with status 400

  Scenario: Create a new rating, rating request not valid (missing rideId)
    Given a rating request body:
    """
    {
        "rating": 5.0,
        "comment": "Great service!",
        "rated_by": "PASSENGER"
    }
    """
    When I send a request to create a new rating
    Then I should get a rating response with status 400

  Scenario: Get rating by ID
    Given ride info exists with ride id 1
    Given a rating exists with id 1
    When I send a request to get the rating by ID
    Then I should get the rating details in the response
    And I should get a rating response with status 200

  Scenario: Get rating by ID, rating not found
    Given a rating by id: 0
    When I send a request to get the rating by ID
    Then I should get a rating response with status 404

  Scenario: Update rating
    Given ride info exists with ride id 1
    Given a rating exists with id 1
    Given a rating request body:
    """
    {
        "rating": 4.0,
        "comment": "Updated comment"
    }
    """
    When I send a request to update the rating
    Then I should get the rating details in the response
    And I should get a rating response with status 200

  Scenario: Get average rating by passenger ID
    Given ride info exists with ride id 1
    Given a rating exists for passenger 1 with id 1
    When I send a request to get the average rating by passenger ID: 1
    Then I should get the average rating in the response
    And I should get a rating response with status 200

  Scenario: Get average rating by driver ID
    Given ride info exists with ride id 1
    Given a rating exists for driver 1 with id 1 and rating 4.5
    When I send a request to get the average rating by driver ID: 1
    Then I should get the average rating in the response
    And I should get a rating response with status 200

  Scenario: Get all ratings with page 0 and limit 10
    Given ride info exists with ride id 1
    Given a rating exists with id 1
    When I send a request to get all ratings with page 0 and limit 10
    Then I should get a rating response with status 200
