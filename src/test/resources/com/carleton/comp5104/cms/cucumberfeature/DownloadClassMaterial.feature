@tag
Feature: As a Professor I want to download course material(e.g. slides, assignment files, etc) from my class

  @success
  Scenario Outline: Professor uploads a file successfully.
    Given A professor with id <prof_id> is assigned to class <class_id>
    And There are no class materials in class <class_id>
    When The professor upload a file with filename <file_name> under directory <dir> to class <class_id>
    Then The file <file_name> for class <class_id> is uploaded to server
    When The professor download the file with filename <file_name> under directory <dir> from class <class_id>
    Then The file is downloaded

    Examples:
      | prof_id | class_id | file_name | dir |
      | 2000006 | 1069 | "A1.txt" | "Assignments" |
