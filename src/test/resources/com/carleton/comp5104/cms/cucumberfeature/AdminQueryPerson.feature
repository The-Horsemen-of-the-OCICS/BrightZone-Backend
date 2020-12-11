Feature: admin query person

  @findSuccess
  Scenario Outline: admin find person by personId
    Given the admin input personId <personId> in the search box
    When Person with personId <personId> exist
    Then the admin will find this person
    Examples:
      | personId |
      | 1000000  |