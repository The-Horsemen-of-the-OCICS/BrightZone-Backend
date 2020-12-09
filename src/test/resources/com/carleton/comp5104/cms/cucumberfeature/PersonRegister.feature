Feature: As a user (professor or student) I want to request account creation in Course Management System

  @NotInUniversity
  Scenario Outline: Register fail because user is not a member in this university
    Given User input userId <userId> in the register form
    And User is not a member in this university
    When User hit register button
    Then Register fail
    Examples:
      | userId |
      | 123    |
      | 456    |

  @AccountNotAuthorized
  Scenario Outline: Register fail because user already has an account but hasn't been authorized yet
    Given User input userId <userId> in the register form
    And User has an account but not authorized yet
    When User hit register button
    Then Register fail
    Examples:
      | userId  |
      | 3000190 |
      | 3000191 |

  @AccountAlreadyAuthorized
  Scenario Outline: Register fail because user already has an account and has already been authorized
    Given User input userId <userId> in the register form
    And User has an account and has already been authorized
    When User hit register button
    Then Register fail
    Examples:
      | userId  |
      | 1000000 |
      | 1000001 |

  @RegisterSuccess
  Scenario Outline: Register success
    Given User input userId <userId> in the register form
    And User is a member in this university
    And User does not have an account in Course Management System
    When User hit register button
    Then Register success
    Examples:
      | userId  |
      | 3000490 |
      | 3000491 |

