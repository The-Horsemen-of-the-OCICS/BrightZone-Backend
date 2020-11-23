@tag
Feature: As a Professor
  I want to create a new deliverable for my class

  @tag1
  Scenario Outline: Professor submit a deliverable for class <class_id> successfully
    Given A professor with id <prof_id> is assigned to class <class_id>
    When The professor submits a deliverable to class <class_id> with deadline <dead_line>, description <desc> and percentage <percent>
    Then The corresponding new entry is created in the Deliverable table

    Examples:
      | prof_id | class_id | dead_line | desc | percent |
      | 2000006 | 1069 | "2021-01-01 10:10:10" | "Assignment 1" | 0.3 |
      | 2000007 | 1070 | "2021-08-23 10:00:00" | "Assignment 2" | 0.1 |
      | 2000008 | 1071 | "2021-08-23 10:00:00" | "Midterm 1" | 0.25 |

  @tag2
  Scenario Outline: Professor submit a deliverable for class <class_id> but with invalid dead line, so it fails
    Given A professor with id <prof_id> is assigned to class <class_id>
    When The professor submits a deliverable to class <class_id> with deadline <dead_line>, description <desc> and percentage <percent>
    Then No entry is created in the Deliverable table

    Examples:
      | prof_id | class_id | dead_line | desc | percent |
      | 2000006 | 1069 | "1999-01-01 10:10:10" | "Assignment 1" | 0.3 |

  @tag3
  Scenario Outline: Professor submit a deliverable for class <class_id>, which is invalid, so it fails
    Given A professor with id <prof_id> is assigned to class <class_id>
    When The professor submits a deliverable to class <class_id> with deadline <dead_line>, description <desc> and percentage <percent>
    Then No entry is created in the Deliverable table

    Examples:
      | prof_id | class_id | dead_line | desc | percent |
      | 2000006 | 9999999 | "2021-08-23 10:00:00" | "Assignment 1" | 0.3 |


