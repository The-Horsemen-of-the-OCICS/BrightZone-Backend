@tag
Feature: As a Professor I want to download files(e.g. code files, pdf files, etc) that a student submitted for a deliverable

  @success
  Scenario Outline: Professor uploads a file successfully.
    Given A professor with id <prof_id> is assigned to class <class_id>
    And There are no deliverables in class <class_id>
    And The professor submits a deliverable to class <class_id> with deadline <dead_line>, description <desc> and percentage <percent>
    And The student with id <student_id> submitted file <file_name>, description <submission_desc> to that deliverable
    When the professor download the file submitted by the student <student_id> for that deliverable of class <class_id>
    Then The file is downloaded

    Examples:
      | prof_id | class_id | student_id | dead_line | desc | percent | file_name | submission_desc |
      | 2000006 | 1069 | 3000001 | "2022-01-01 10:10:10" | "Assignment 1" | 0.3 | "myA1.txt" | "This is my A1" |
