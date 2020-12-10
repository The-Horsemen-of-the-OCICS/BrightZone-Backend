@tag
Feature: Update an added course

  @updateCourse
  Scenario Outline: admin want to update a course
    Given A test course has been added to the course table
    And some prerequisite course has been added to the course
    And some preclusion course has been added to the course
    When the admin search the course with the <courseSubject0> and <courseNumber0>
    And the system output no such course
    And the admin search the course with the <courseSubject1> and <courseNumber1>
    And the system output the course Info
    And the admin press "modify" button
    And the admin input the new <courseSubject2> and <courseNumber2>
    And the system check if the course number has conflicts
    And the system output the course number is "repeat"
    And the admin input the new <courseSubject3> and <courseNumber3>
    And the system check if the course number has conflicts
    And the system output the course number is "valid"
    And the admin input the new <courseName>
    And the system check if the course name has conflicts
    And the system output the course name is "repeat"
    And the admin input the new <courseName1>
    And the system check if the course name has conflicts
    And the system output the course name is "valid"
    And the admin assign a <credit> to the course
    And the admin input the <description> of the course
    Then the admin save the change
    And the system update the course info

    Examples:
      | courseSubject0 | courseNumber0 | courseSubject1 | courseNumber1 | courseSubject2 | courseNumber2 | courseSubject3 | courseNumber3 | courseName        | courseName1  | credit | description             |
      | "NEX"          | "508"         | "NE"           | "500"         | "NE"           | "499"         | "NE"           | "601"         | "Pathophysiology" | "testUpdate" | 3      | "this is a test course" |
