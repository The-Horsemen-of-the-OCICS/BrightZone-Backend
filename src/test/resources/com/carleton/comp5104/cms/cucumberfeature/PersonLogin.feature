Feature: As a user (student or professor)
  I want to login to Course Management System

  @NotInUniversity
  Scenario Outline: Login fail because user is not a member in the university
    Given User input userId <userId> and password <password> in the login form
    And User hit login button
    When User is not a member in the university
    Then Login fail
    Examples:
      | userId | password |
      | 123    | "123456" |
      | 456    | "123456" |

  @NotAuthorized
  Scenario Outline: Login fail because userId is not authorized yet
    Given User input userId <userId> and password <password> in the login form
    And User hit login button
    When UserId <userId> is not authorized yet
    Then Login fail
    Examples:
      | userId  | password |
      | 3000192 | "123456" |
      | 3000193 | "123456" |

  @WrongPassword
  Scenario Outline: Login fail because password is wrong
    Given User input userId <userId> and password <password> in the login form
    And User hit login button
    When Password <password> is not correct
    Then Login fail
    Examples:
      | userId  | password    |
      | 1000000 | "123456789" |
      | 1000001 | "123456789" |

  @LoginSuccess
  Scenario Outline: Login success
    Given User input userId <userId> and password <password> in the login form
    And User hit login button
    When Password <password> is correct
    Then Login success
    Examples:
      | userId  | password |
      | 1000000 | "123456" |
      | 1000001 | "123456" |