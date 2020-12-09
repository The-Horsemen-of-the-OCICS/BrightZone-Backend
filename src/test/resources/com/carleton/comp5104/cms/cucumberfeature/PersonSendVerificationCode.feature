Feature: As a user (professor or student) I want to ask CMS server to send verification code to my mail box

  @DoNotHaveAnAccount
  Scenario Outline: Verification code send fail because user doesn't have an account in CMS
    Given User input email <email> in verification code sending form
    And User does not have an account in Course Management System
    When User hit send verification code button
    Then Verification code send fail
    Examples:
      | email                |
      | "mock123@uottawa.ca" |

  @NotAuthorized
  Scenario Outline: Verification code send fail because email has not been authorized by admin yet
    Given User input email <email> in verification code sending form
    And User has an account but not authorized yet
    When User hit send verification code button
    Then Verification code send fail
    Examples:
      | email                      |
      | "edwinaphillip@uottawa.ca" |

  @VerificationCodeSendSuccess
  Scenario Outline: Verification code send success
    Given User input email <email> in verification code sending form
    And User has an account and has already been authorized
    When User hit send verification code button
    Then Verification code send success
    Examples:
      | email                   |
      | "niladevine@uottawa.ca" |