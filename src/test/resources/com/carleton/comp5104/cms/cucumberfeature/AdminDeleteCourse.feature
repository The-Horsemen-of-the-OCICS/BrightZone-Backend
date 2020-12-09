@tag
Feature: As an Administrator
  I want to delete a course from the course table

  @deleteACourse
  Scenario Outline: Admin delete a course successfully.
    Given A test course has been added to the course table
    And some prerequisite course has been added to the course
    And some preclusion course has been added to the course
    When the admin search the course with the <courseSubject0> and <courseNumber0>
    And the system output no such course
    And the admin search the course with the <courseSubject1> and <courseNumber1>
    And the system output the course Info
    Then the admin press the "delete" button
    And the system delete the prerequisite and preclusion
    And the system delete the course and classes under this course

    Examples:
      | courseSubject0 | courseNumber0 | courseSubject1 | courseNumber1 |
      | "NEX"          | "508"         | "NE"           | "500"         |