@tag
Feature: As a Professor I want to create and submit a new deliverable for my class

  @success
  Scenario Outline: Professor submit a deliverable for class <class_id> successfully
    Given A professor with id <prof_id> is assigned to class <class_id>
    And There are no deliverables in class <class_id>
    When The professor submits a deliverable to class <class_id> with deadline <dead_line>, description <desc> and percentage <percent>
    Then The corresponding new entry is created in the Deliverable table

    Examples:
      | prof_id | class_id | dead_line | desc | percent |
      | 2000006 | 1069 | "2021-01-01 10:10:10" | "Assignment 1" | 0.3 |
      | 2000007 | 1070 | "2021-08-23 10:00:00" | "Assignment 2" | 0.1 |
      | 2000008 | 1071 | "2021-08-23 10:00:00" | "Midterm 1" | 0.25 |

  @invalid_deadline
  Scenario Outline: Professor submit a deliverable for class <class_id> but with invalid dead line, so it fails
    Given A professor with id <prof_id> is assigned to class <class_id>
    And There are no deliverables in class <class_id>
    When The professor submits a deliverable to class <class_id> with deadline <dead_line>, description <desc> and percentage <percent>
    Then No entry is created in the Deliverable table

    Examples:
      | prof_id | class_id | dead_line | desc | percent |
      | 2000006 | 1069 | "1999-01-01 10:10:10" | "Assignment 10" | 0.3 |

  @course_does_not_exist
  Scenario Outline: Professor submit a deliverable for class <class_id>, which is invalid, so it fails
    Given A professor with id <prof_id> is assigned to class <class_id>
    And There are no deliverables in class <class_id>
    When The professor submits a deliverable to class <class_id> with deadline <dead_line>, description <desc> and percentage <percent>
    Then No entry is created in the Deliverable table

    Examples:
      | prof_id | class_id | dead_line | desc | percent |
      | 2000006 | 9999999 | "2021-08-23 10:00:00" | "Assignment 1" | 0.3 |

  @deliverable_already_exists
  Scenario Outline: Professor submit a deliverable for class <class_id>, which already exists, so it fails
    Given A professor with id <prof_id> is assigned to class <class_id>
    And There are no deliverables in class <class_id>
    When The professor submits a deliverable to class <class_id> with deadline <dead_line_1>, description <desc> and percentage <percent_1>
    Then The corresponding new entry is created in the Deliverable table
    When The professor submits a deliverable to class <class_id> with deadline <dead_line_2>, description <desc> and percentage <percent_2>
    Then No entry is created in the Deliverable table

    Examples:
      | prof_id | class_id | desc | dead_line_1| percent_1 | dead_line_2| percent_2 |
      | 2000006 | 1069 |"Assignment 1" | "2021-08-23 10:00:00" | 0.15 | "2021-09-24 10:00:00" | 0.34 |

