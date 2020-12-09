Feature: As a user (professor or student) I want to send a request to Admin

  @CreateSuccess
  Scenario Outline: Request message create success
    Given User with userId <userId> choose request type <requestType>
    And Input request message <requestMessage> in the message box
    When User hit send message button
    Then Request message will be sent to admin
    Examples:
      | userId  | requestType     | requestMessage                                                                                          |
      | 2000000 | "create_course" | "Hello, I'm professor Shu Percival, please help me create course COMP 5104 Object-Oriented Programming" |
      | 3000000 | "enroll"        | "Hello, my name is Jillian Villagomez, please help me enroll course COMP5104"                           |

