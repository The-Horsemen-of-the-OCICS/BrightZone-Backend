@tag
Feature: As a Professor I want to delete a deliverable for my class

  @success
  Scenario Outline: Professor delete a deliverable for class <class_id> successfully
    Given A professor with id <prof_id> is assigned to class <class_id>
    And A student with id <student_id> is enrolled to class <class_id>
    And The professor submits a deliverable to class <class_id> with deadline <dead_line>, and percentage <percent>
    And The student with id <student_id> has submitted for a deliverable at time <submit_time> and has grades <grade>
    When The professor delete this new created deliverable
    Then The deliverable is deleted
    And All submissions related to the deliverable are deleted

    Examples:
      | prof_id | class_id | dead_line | percent | student_id | submit_time | grade |
      | 2000006 | 1069 | "2021-01-01 10:10:10" | 0.3 | 3000001 | "2020-12-30 10:10:10" | 0.89 |

  @invalid_deliverable_id
  Scenario Outline: Professor delete a deliverable with <deliverable_id>, which is invalid, so it fails
    Given A professor with id <prof_id> is assigned to class <class_id>
    When The professor delete deliverable with id <deliverable_id>
    Then No entry is deleted in the Deliverable table

    Examples:
      | prof_id | class_id | deliverable_id |
      | 2000006 | 1069 | -1457754 |
