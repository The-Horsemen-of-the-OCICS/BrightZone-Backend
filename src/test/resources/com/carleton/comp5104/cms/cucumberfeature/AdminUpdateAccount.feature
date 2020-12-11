@tag
Feature: Admin try to update an account.

  @AdminUpdateAccount
  Scenario Outline: admin success update the user`s account info after several invalid input.
    Given the new test user was added to account table
    When the admin search the account with the <user_name_wrong>
    And the system output no such user
    And the admin search the account with the <user_name_correct>
    And the system output the userInfo
    And the admin press the "modify" button
    And the admin set the account to <newUserType>
    And the admin set the account to a new <newUserName>
    And the System check if the input edit is valid
    And the System output the "userName" should not be "none"
    And the admin set the account to a new <newUserName1>
    And the System check if the input edit is valid
    And the System output the "userName" should not be "all blanks"
    And the admin set the account to a new <newUserName2>
    And the System check if the input edit is valid
    And the System output the "userName" is valid
    Then the admin press the "submit" button
    And the system update the account with the new info
    And the system shows the updated account info

    Examples:
      | user_name_wrong | user_name_correct | newUserType | newUserName | newUserName1 | newUserName2 |
      | "testAccount1"  | "Tom Hanks "      | "professor" | ""          | "   "        | "Jack Ryan"  |
