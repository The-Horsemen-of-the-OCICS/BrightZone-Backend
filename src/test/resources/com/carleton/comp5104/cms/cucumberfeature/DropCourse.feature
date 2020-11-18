@tag
Feature: As a Student
  I want to register a class of a course

  @register_successfully
  Scenario Outline: Student register a class of the course successfully
    Given A student with id <stu_id> check all opened courses
    And The student choose a class <class_id> of a course
    And The class has remaining space
    And The student finished all pre-requisite courses
    And The student did not take preclusion courses
    And The student click register
    Then student register successfully

    Examples:
      | stu_id | class_id  |
      | 1 | 1069 |

  @register_fail1
  Scenario Outline: Student register a class of the course fail, because of no remaining space
    Given A student with id <stu_id> check all opened courses
    And The student choose a class <class_id> of a course
    And The class has no remaining space
    And The student click register
    Then student register fail

    Examples:
      | stu_id | class_id  |
      | 1 | 1069 |

  @register_fail2
  Scenario Outline: Student register a class of the course fail, because of pre-requisite courses
    Given A student with id <stu_id> check all opened courses
    And The student choose a class <class_id> of a course to register
    And The student has not finished all pre-requisite courses
    And The student click register
    Then student register fail

    Examples:
      | stu_id | class_id  |
      | 1 | 1069 |

  @register_fail3
  Scenario Outline: Student register a class of the course fail, because of preclusion courses
    Given A student with id <stu_id> check all opened courses
    And The student choose a class <class_id> of a course to register
    And The student took one of the preclusion courses
    And The student click register
    Then student register fail

    Examples:
      | stu_id | class_id  |
      | 1 | 1069 |
