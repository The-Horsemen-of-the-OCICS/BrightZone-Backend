Feature: As an admin, I want to handle request messages

  @UpdateRequestFail
  Scenario Outline: Update request status fail because no matching Request object with requestId
    Given the admin select request with request id <requestId>
    And modify request status to <newStatus>
    And Request with request id <requestId> doesn't exist
    When the admin hit update request message button
    Then Update request message fail
    Examples:
      | requestId | newStatus   |
      | 100       | "fulfilled" |

  @UpdateRequestSuccess
  Scenario Outline: Update request status success
    Given the admin select request with request id <requestId>
    And modify request status to <newStatus>
    And Request with request id <requestId> exists
    When the admin hit update request message button
    Then Update request message success
    Examples:
      | requestId | newStatus   |
      | 1         | "fulfilled" |

  @DeleteByUserId
  Scenario Outline: Delete request message by userId success
    Given the admin select request with user id <userId>
    When the admin hit delete request message button
    Then Delete request message success
    Examples:
      | userId  |
      | 3000000 |

  @GetOpenRequest
  Scenario: Get all request with open status
    Given the admin select request with status open
    When the admin hit search request message button
    Then the admin get all open status
