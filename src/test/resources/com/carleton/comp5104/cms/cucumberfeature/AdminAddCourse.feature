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
    And the admin assign a <credit> to the course
    And the admin input the <description> of the course
    Then the admin press the submit button
    And the system add the new course

    Examples:
      | courseSubject | courseNumber | courseNumber1 | courseName        | courseName1 | credit | description             |
      | "OPTOM"       | "101"        | "99"          | "Pathophysiology" | "test1"     | 3      | "this is a test course" |


  @addCourseWithPrerequisite
  Scenario Outline: success add a new course with prerequisite
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
    And the admin assign a <credit> to the course
    And the admin input the <description> of the course
    And the admin try to add related course
    And the admin input the Prerequisite course subject <prerequisiteCourseSubject>
    And the system search the course table to check it
    And the system output no such course subject
    And the admin input the Prerequisite course subject <prerequisiteCourseSubject1>
    And the system search the course table to check it
    And the system show the courses under the subject
    And the admin choose a course under the subject <prerequisiteCourseSubject1>
    And the system output Prerequisite course info
    Then the admin press the submit button
    And the system add the new course with prerequisite info

    Examples:
      | courseSubject | courseNumber | courseNumber1 | courseName        | courseName1 | credit | description             | prerequisiteCourseSubject | prerequisiteCourseSubject1 |
      | "OPTOM"       | "101"        | "99"          | "Pathophysiology" | "test1"     | 3      | "this is a test course" | "ASFG"                    | "OPTOM"                    |


  @addCourseWithPreclusion
  Scenario Outline: success add a new course with preclusion
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
    And the admin assign a <credit> to the course
    And the admin input the <description> of the course
    And the admin try to add related course
    And the admin input the Preclusion course subject <preclusionCourseSubject>
    And the system search the course table to check it
    And the system output no such course subject
    And the admin input the Preclusion course subject <preclusionCourseSubject1>
    And the system search the course table to check it
    And the system show the courses under the subject
    And the admin choose a course under the subject <preclusionCourseSubject1>
    And the system output Preclusion course info
    Then the admin press the submit button
    And the system add the new course with preclusion info

    Examples:
      | courseSubject | courseNumber | courseNumber1 | courseName        | courseName1 | credit | description             | preclusionCourseSubject | preclusionCourseSubject1 |
      | "OPTOM"       | "101"        | "99"          | "Pathophysiology" | "test1"     | 3      | "this is a test course" | "NXSK"                  | "OPTOM"                  |

