@tag
Feature: As a Professor
  I want to submit the grades for student's submission from my class

  @tag1
  Scenario Outline: Professor submit the grade to a submission successfully
    Given A professor with id <prof_id> is assigned to class <class_id>
    And The professor submits a deliverable to class <class_id> with deadline <dead_line>, description <desc> and percentage <percent>
    And The student with id <student_id> made a submission to that deliverable with file name <file_name>, description <submission_desc> at time <submit_time>
    When The professor submit the grade of <grade> to that submission
    Then The grade column of that submission is modified to <grade> in the Submission table

    Examples:
      | prof_id | class_id | student_id | dead_line | desc | percent | file_name | submission_desc | submit_time | grade |
      | 2000006 | 1069 | 3000299 | "2021-01-01 10:10:10" | "Assignment 1" | 0.3 | "myA1" | "This is my A1" | "2020-01-01 10:10:10" | 0.78 |
      | 2000007 | 1070 | 3000300 | "2021-08-23 10:00:00" | "Assignment 2" | 0.1 | "myA2" | "This is my A2" | "2020-01-01 10:10:10" | 0.87 |
      | 2000008 | 1071 | 3000301 | "2021-08-23 10:00:00" | "Midterm 1" | 0.25 | "myMidterm1" | "This is my A2" | "2020-01-01 10:10:10" | 0.95 |

  @tag1
  Scenario Outline: Professor tries to submit the grade to a invalid submission, and fails to do so
    Given A professor with id <prof_id> is assigned to class <class_id>
    When The professor submit the grade of <grade> to submission with id <submission_id>
    Then No changes were made to the Submission table as the submission id is invalid

    Examples:
      | prof_id | class_id | submission_id | grade |
      | 2000006 | 1069 | -6565 | 0.78 |
      | 2000007 | 1070 | 99999999 | 0.87 |
