Feature: book ticket
  Scenario: Correct non-zero number of books found by author
    Given Empty repository
    Given I want to book ticket for user 1 for event 1 with place 1 and category STANDARD
    When I book ticket
    Then Validate booked ticket fields
    Then Validate used correct jms topic
