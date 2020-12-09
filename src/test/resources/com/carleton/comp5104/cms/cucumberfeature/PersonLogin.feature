Feature: As a user (professor or student) I want to login to Course Management System

  @DoNotHaveAnAccount
  Scenario Outline: Login fail because user doesn't have an account in CMS
    Given User input userId <userId> and password <password> in the login form
    And User does not have an account in Course Management System
    When User hit login button
    Then Login fail
    Examples:
      | userId | password |
      | 123    | "123456" |
      | 456    | "123456" |

  @NotAuthorized
  Scenario Outline: Login fail because userId is not authorized yet
    Given User input userId <userId> and password <password> in the login form
    And User has an account but not authorized yet
    When User hit login button
    Then Login fail
    Examples:
      | userId  | password |
      | 3000193 | "123456" |
      | 3000194 | "123456" |

  @WrongPassword
  Scenario Outline: Login fail because password is wrong
    Given User input userId <userId> and password <password> in the login form
    And User has an account and has already been authorized
    And Password <password> is not correct
    When User hit login button
    Then Login fail
    Examples:
      | userId  | password    |
      | 1000000 | "123456789" |
      | 1000001 | "123456789" |

  @LoginSuccess
  Scenario Outline: Login success
    Given User input userId <userId> and password <password> in the login form
    And User has an account and has already been authorized
    And Password <password> is correct
    When User hit login button
    Then Login success
    Examples:
      | userId  | password |
      | 1000000 | "123456" |
      | 1000001 | "123456" |