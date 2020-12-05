@tag
Feature: As a Professor
  I want to submit the final grades for students enrolled in my class

  @success
  Scenario Outline: Professor submit the final grade of a student successfully (No missing submissions)
    Given A professor with id <prof_id> is assigned to class <class_id>
    And A student with id <student_id> is enrolled to class <class_id>
    And There are no deliverables in class <class_id>
    And The professor submits a deliverable to class <class_id> with deadline <dead_line_1>, and percentage <percent_1>
    And The professor submits a deliverable to class <class_id> with deadline <dead_line_2>, and percentage <percent_2>
    And The professor submits a deliverable to class <class_id> with deadline <dead_line_3>, and percentage <percent_3>
    And The student with id <student_id> has submitted for a deliverable at time <submit_time_1> and has grades <grade_1>
    And The student with id <student_id> has submitted for a deliverable at time <submit_time_2> and has grades <grade_2>
    And The student with id <student_id> has submitted for a deliverable at time <submit_time_3> and has grades <grade_3>
    When The professor submit the final grade of student <student_id> for class <class_id>
    Then The final_grade column of enrollment of student <student_id> and class <class_id> is modified to <final_grade>

    Examples:
      | prof_id | class_id | dead_line_1 | percent_1 | dead_line_2 | percent_2 | dead_line_3 | percent_3 | student_id | submit_time_1 | grade_1 | submit_time_2 | grade_2 |submit_time_3 | grade_3 | final_grade |
      | 2000006 | 1069 | "2021-01-01 10:10:10" | 0.34 | "2021-08-23 10:00:00" | 0.26 | "2021-09-12 10:00:00" | 0.4 | 3000001 | "2020-12-30 10:10:10" | 0.74 | "2021-08-20 10:00:00" | 0.88 | "2021-09-10 10:00:00" | 0.62 | 0.7284 |
      | 2000006 | 1069 | "2021-01-01 10:10:10" | 0.34 | "2021-08-23 10:00:00" | 0.26 | "2021-09-12 10:00:00" | 0.4 | 3000001 | "2020-12-30 10:10:10" | 0.74 | "2021-08-30 10:00:00" | 0.88 | "2021-11-11 10:00:00" | 0.62 | 0.2516 |

  @success_with_missing_submissions
  Scenario Outline: Professor submit the final grade of a student successfully (Missing submissions)
    Given A professor with id <prof_id> is assigned to class <class_id>
    And A student with id <student_id> is enrolled to class <class_id>
    And There are no deliverables in class <class_id>
    And The professor submits a deliverable to class <class_id> with deadline <dead_line_1>, and percentage <percent_1>
    And The professor submits a deliverable to class <class_id> with deadline <dead_line_2>, and percentage <percent_2>
    And The student with id <student_id> has submitted for a deliverable at time <submit_time_1> and has grades <grade_1>
    When The professor submit the final grade of student <student_id> for class <class_id>
    Then The final_grade column of enrollment of student <student_id> and class <class_id> is modified to <final_grade>

    Examples:
      | prof_id | class_id | dead_line_1 | percent_1 | dead_line_2 | percent_2 | student_id | submit_time_1 | grade_1 | final_grade |
      | 2000006 | 1069 | "2021-01-01 10:10:10" | 0.74 | "2021-08-23 10:00:00" | 0.26 | 3000001 | "2020-12-30 10:10:10" | 0.74 | 0.5476 |

  @invalid_student_id
  Scenario Outline: Professor submit the final grade of an invalid student, no changes made.
    Given A professor with id <prof_id> is assigned to class <class_id>
    When The professor submit the final grade of student <student_id> for class <class_id>
    Then Then nothing changes in the database for student <student_id> and class <class_id>
    Examples:
      | prof_id | class_id | student_id |
      | 2000006 | 1069 | -1454545 |
      | 2000006 | 1069 | 3000003 |

  @invalid_class
  Scenario Outline: Professor submit the final grade of an invalid class, no changes made.
    Given A professor with id <prof_id> is assigned to class <class_id>
    When The professor submit the final grade of student <student_id> for class <class_id>
    Then Then nothing changes in the database for student <student_id> and class <class_id>
    Examples:
      | prof_id | class_id | student_id |
      | 2000006 | -46761 | 3000001 |
