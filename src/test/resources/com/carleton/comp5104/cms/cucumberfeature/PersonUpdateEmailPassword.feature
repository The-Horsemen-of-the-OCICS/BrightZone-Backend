Feature: As a user (professor or student) I want to update my email and/or password in CMS

  @UpdateEmailFail
  Scenario Outline: Update email fail because new email has been occupied by other users
    Given User with userId <userId> input new email <newEmail> in email update form
    And New email has already been occupied by other user
    When User hit update email button
    Then Update fail
    Examples:
      | userId  | newEmail                |
      | 1000001 | "niladevine@uottawa.ca" |

  @UpdateEmailSuccess
  Scenario Outline: Update email success
    Given User with userId <userId> input new email <newEmail> in email update form
    And New email has not been occupied by other user
    When User hit update email button
    Then Email update success
    Examples:
      | userId  | newEmail                 |
      | 1000001 | "niladevinee@uottawa.ca" |

  @UpdatePasswordSuccess
  Scenario Outline: Update password success
    Given User with userId <userId> input new password <password> in password update form
    Then User hit update password button
    Then Password update success
    Examples:
      | userId  | password   |
      | 1000000 | "12345678" |