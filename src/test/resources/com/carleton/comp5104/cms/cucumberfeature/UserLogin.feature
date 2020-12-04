Feature: As a user
  I want to login to Course Management System

  @LoginFail1
  Scenario Outline: Login fail because email or password is empty
    Given User input email <email> and password <password> in the login form
    And Email <email> or password <password> is empty
    When User hit login button
    Then Login fail
    Examples:
      | email                   | password |
      | "niladevine@uottawa.ca" | ""       |
      | ""                      | "123456" |

  @LoginFail2
  Scenario Outline: Login fail because email is not in table Account
    Given User input email <email> and password <password> in the login form
    And Email <email> is not in table Account
    When User hit login button
    Then Login fail
    Examples:
      | email                     | password |
      | "mock_person1@uottawa.ca" | "123456" |
      | "mock_person2@uottawa.ca" | "123456" |

  @LoginFail3
  Scenario Outline: Login fail because email is not authorized yet
    Given User input email <email> and password <password> in the login form
    And Email <email> is not authorized yet
    When User hit login button
    Then Login fail
    Examples:
      | email                        | password |
      | "carsontropea@uottawa.ca"    | "123456" |
      | "jessikaovermyer@uottawa.ca" | "123456" |

  @LoginFail4
  Scenario Outline: Login fail because password is not correct
    Given User input email <email> and password <password> in the login form
    And Email <email> is already authorized
    And Password <password> is not correct
    When User hit login button
    Then Login fail
    Examples:
      | email                      | password    |
      | "hermaharrow@uottawa.ca"   | "123456789" |
      | "dorrislockamy@uottawa.ca" | "123456789" |

  @LoginSuccess
  Scenario Outline: Login success
    Given User input email <email> and password <password> in the login form
    And Email <email> is already authorized
    And Password <password> is correct
    When User hit login button
    Then Login success
    Examples:
      | email                      | password |
      | "niladevine@uottawa.ca"    | "123456" |
      | "evelinebizier@uottawa.ca" | "123456" |