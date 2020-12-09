@tag
Feature: The admin want to add a new course to the system.

  @addCourse
  Scenario Outline: success add a new course
    Given the admin press the add course button
    When the admin input the new <courseSubject> and <courseNumber>
    And the system check if the course number has conflicts
    And the system output the course number is "repeated"
    And the admin input the new <courseSubject> and <courseNumber1>
    And the system check if the course number has conflicts
    And the system output the course number is "valid"
    And the admin input the new <courseName>
    And the system check if the course name has conflicts
    And the system output the course name is "repeat"
    And the admin input the new <courseName1>
    And the system check if the course name has conflicts
    And the system output the course name is "valid"
    And the admin assign a <credit> to the new course
    And the admin input the <description> of the course
    Then the admin press the submit button
    And the system add the new course

    Examples:
      | courseSubject | courseNumber | courseNumber1 | courseName        | courseName1 | credit | description             |
      | "OPTOM"       | "101"        | "99"          | "Pathophysiology" | "test1"     | 3      | "this is a test course" |

