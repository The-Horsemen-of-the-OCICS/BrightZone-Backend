@tag
Feature: Admin add new to-do at the index page

  @adminAddTodoItems
  Scenario Outline: admin add a new todo item at the index page
    Given the new admin test user was added to account table
    And the admin press "Add" button to write a new todo
    When the admin input the new todo <content>
    And the admin choose the priority <level> of this new Todo
    And the admin choose the start time <startTime> of the new Todo
    And the admin choose the end time <endTime> of the new Todo
    Then the admin press "submit" button to save the todo
    And the system show the new added todo

    Examples:
      | content           | level   | startTime           | endTime             |
      | "A new test todo" | "Prior" | "2021-2-5 20:30:30" | "2021-3-5 20:30:30" |

  @adminEditTodoItems
  Scenario Outline: admin modify a todo item at the index page
    Given the new admin test user was added to account table
    And the todo items has been add to table
    And the admin press "edit" button to edit the number <todoNum> todo
    When the admin input the new todo <content>
    And the admin choose the priority <level> of this new Todo
    And the admin choose the start time <startTime> of the new Todo
    And the admin choose the end time <endTime> of the new Todo
    Then the admin press "submit" button to save the edit
    And the system show the new edit todo

    Examples:
      | todoNum | content           | level    | startTime           | endTime             |
      | 0       | "A new test todo" | "Normal" | "2021-2-5 20:30:30" | "2021-3-5 20:30:30" |

  @adminFinishTodoItem
  Scenario Outline: admin finish a todo item at the index page
    Given the new admin test user was added to account table
    And the todo items has been add to table
    When the admin press the checkbox at the todo item <row>
    Then the system change the state of the todo item
    And the system show the todo has been finished

    Examples:
      | row |
      | 0   |