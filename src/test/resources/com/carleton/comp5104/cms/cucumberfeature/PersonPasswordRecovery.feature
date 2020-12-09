Feature: As a user (professor or student) I want to recover my password

  @DoNotHaveAnAccount
  Scenario Outline: Password recovery fail because user doesn't have an account in CMS
    Given User input email <email> verification code <verificationCode> and new password <newPassword> in password recovery form
    And User does not have an account in Course Management System
    When User hit recovery button
    Then Password recover fail
    Examples:
      | email              | verificationCode | newPassword |
      | "mock1@uottawa.ca" | "123456"         | "1234567"   |
      | "mock3@uottawa.ca" | "123456"         | "1234567"   |

  @NotAuthorized
  Scenario Outline: Password recovery fail because email is not authorized yet
    Given User input email <email> verification code <verificationCode> and new password <newPassword> in password recovery form
    And User has an account but not authorized yet
    When User hit recovery button
    Then Password recover fail
    Examples:
      | email                      | verificationCode | newPassword |
      | "calebnewberg@uottawa.ca"  | "888"            | "1234567"   |
      | "edwinaphillip@uottawa.ca" | "888"            | "1234567"   |

  @WrongVerificationCode
  Scenario Outline: Password recovery fail because verification code is wrong
    Given User input email <email> verification code <verificationCode> and new password <newPassword> in password recovery form
    And User has an account and has already been authorized
    When User hit recovery button
    Then Password recover fail
    Examples:
      | email                   | verificationCode | newPassword |
      | "niladevine@uottawa.ca" | "123456"         | "1234567"   |

  @PasswordRecoverySuccess
  Scenario Outline: Password recovery success
    Given User input email <email> verification code <verificationCode> and new password <newPassword> in password recovery form
    And User has an account and email has been authorized
    When User hit recovery button
    Then Password recover success
    Examples:
      | email                   | verificationCode | newPassword |
      | "niladevine@uottawa.ca" | "234567"         | "1234567"   |
