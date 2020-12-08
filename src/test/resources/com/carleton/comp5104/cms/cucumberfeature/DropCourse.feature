@tag
Feature: As a Student
  I want to drop a course

  @drop_success1
  Scenario Outline: Student drop a course success no DR
    Given A student with id <stu_id> check all registered courses
    And The student choose a course <class_id> to drop
    When It is before the course deadline
    And The student click drop
    Then student drop success no DR

    Examples:
      | stu_id  | class_id |
      | 3000182 | 1074     |

  @drop_success2
  Scenario Outline: Student drop a course success with DR
    Given A student with id <stu_id> check all registered courses
    And The student choose a course <class_id> to drop
    When It is after the course deadline
    And It is before the DR deadline
    And The student click drop
    Then student drop success with DR

    Examples:
      | stu_id  | class_id |
      | 3000182 | 1075     |

  @register_fail
  Scenario Outline: Student register a class of the course fail, because of preclusion courses
    Given A student with id <stu_id> check all registered courses
    And The student choose a course <class_id> to drop
    When It is after the course deadline
    And It is after the DR deadline
    And The student click drop
    Then student drop failed

    Examples:
      | stu_id  | class_id |
      | 3000182 | 1076     |
