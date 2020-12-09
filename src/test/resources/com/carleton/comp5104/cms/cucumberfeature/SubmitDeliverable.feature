@tag
Feature: As a Student
  I want to submit the deliverable from my class

  @submit_success
  Scenario Outline: Student submit the deliverable success
    Given A student <stu_id> check all deliverable sections for the course <class_id>
    And The Student choose a section <deliverable_id> and file to submit
    When It is before the deadline
    And The student click submit
    Then The student submit success

    Examples:
      | stu_id  | class_id | deliverable_id |
      | 3000182 | 1032     | 1              |

  @submit_fail
  Scenario Outline: Student submit the deliverable failed, because of deadline
    Given A student <stu_id> check all deliverable sections for the course <class_id>
    And The Student choose a section <deliverable_id> and file to submit
    When It is after the deadline
    And The student click submit
    Then The student submit failed

    Examples:
      | stu_id  | class_id | deliverable_id |
      | 3000182 | 1032     | 1              |
