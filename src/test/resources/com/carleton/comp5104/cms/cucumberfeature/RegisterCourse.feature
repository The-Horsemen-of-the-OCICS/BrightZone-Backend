@tag
Feature: As a Student
  I want to register a course

  @register_success
  Scenario Outline: Student register a course success
    Given A student with id <stu_id> check all opened courses
    And The student choose a class <class_id> of a course
    When The class has remaining space
    And The student finished all pre-requisite courses
    And The student did not take preclusion courses
    And The student click register
    Then student register success

    Examples:
      | stu_id  | class_id |
      | 3000182 | 1069     |

  @register_fail1
  Scenario Outline: Student register a course fail, because of no remaining space
    Given A student with id <stu_id> check all opened courses
    And The student choose a class <class_id> of a course
    When The class has no remaining space
    And The student click register
    Then student register failed

    Examples:
      | stu_id  | class_id |
      | 3000182 | 1070     |

  @register_fail2
  Scenario Outline: Student register a course fail, because of pre-requisite courses
    Given A student with id <stu_id> check all opened courses
    And The student choose a class <class_id> of a course
    When The class has remaining space
    And The student has not finished all pre-requisite courses
    And The student click register
    Then student register failed

    Examples:
      | stu_id  | class_id |
      | 3000182 | 1071     |

  @register_fail3
  Scenario Outline: Student register a fail, because of preclusion courses
    Given A student with id <stu_id> check all opened courses
    And The student choose a class <class_id> of a course
    When The class has remaining space
    And The student finished all pre-requisite courses
    And The student took one of the preclusion courses
    And The student click register
    Then student register failed

    Examples:
      | stu_id  | class_id |
      | 3000182 | 1072     |


  @register_concurrency104
  Scenario Outline: Student register a course success concurrently
    When Student A with id <stu_id> register a class <class_id> of a course with limit of <limit>
    And Student B with id <stu_idB> and Student C with id <stu_idC> register the class of the course simultaneously
    Then the enrolled student of the course is equal to <limit>
    And B <stu_idB> and C <stu_idC> only one can register success

    Examples:
      | stu_id  | class_id | stu_idB | stu_idC | limit |
      | 3000182 | 1079     | 3000193 | 3000194 | 2     |

  @register_concurrency108
  Scenario Outline: Student register and drop a course success concurrently
    When Student A with id <stu_id> register a class <class_id> of a course with limit of <limit>
    And Student A with id <stu_id> drop the class of the course, Student B with id <stu_idB> and Student C with id <stu_idC> register the class of the course simultaneously
    Then the enrolled student of the course is not bigger than <limit>
    And At least one of B <stu_idB> and C <stu_idC> register success
    Examples:
      | stu_id  | class_id | stu_idB | stu_idC | limit |
      | 3000182 | 1078     | 3000193 | 3000194 | 2     |
