@tag
Feature: Admin want to update a class

  @AdminCancelAClass
  Scenario: the admin want to cancel a class when it already started
    Given A test course has been added to the course table
    And the admin search the new added course
    And the test class has been added to the class table
    When the admin press "modify" class button
    And the admin change the class status to "cancel"
    And the admin submit the change
    Then the system delete all the submission related
    And the system delete all the deliverable related
    And the system update the class status to "cancel"
