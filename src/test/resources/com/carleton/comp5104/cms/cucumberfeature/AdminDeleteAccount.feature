@tag
Feature: As a Admin, I want to delete some account from the cms system.

  @adminDeleteStudentAccount
  Scenario Outline: admin search and delete an account
    Given the new test user was added to account table
    When the admin search the account with the <user_name_wrong>
    And the system output no such user
    And the admin search the account with the <user_name_correct>
    And the system output the userInfo
    And the admin press the "delete" button
    Then the system delete the account

    Examples:
      | user_name_wrong | user_name_correct |
      | "testAccount1"  | "Tom Hanks "      |

  @adminDeleteProfessorAccount
  Scenario Outline: admin search and delete a professor account
    Given the new professor test user was added to account table
    When the admin search the account with the <user_name_wrong>
    And the system output no such user
    And the admin search the account with the <user_name_correct>
    And the system output the userInfo
    And the admin press the "delete" button
    Then the system delete the account

    Examples:
      | user_name_wrong | user_name_correct |
      | "testAccount1"  | "Tom Hanks "      |
