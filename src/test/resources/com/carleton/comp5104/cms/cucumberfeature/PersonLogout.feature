Feature: As a user (professor or student) I want to log out

  @LogoutFail
  Scenario: User Logout fail because s/he didn't login to CMS before
    Given User did not login to Course Management System before
    When User hit logout button
    Then Logout fail

  @LogoutSuccess
  Scenario: User Logout success
    Given User login to Course Management System before
    When User hit logout button
    Then Logout success