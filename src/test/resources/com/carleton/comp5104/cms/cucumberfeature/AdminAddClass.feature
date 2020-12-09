@tag
Feature: Admin add a new class

  Scenario Outline: admin success add a new class after several try.
    Given A test course has been added to the course table
    And the system received the new added course id
    When The admin press "addClass" button to add class
    And the new class is under the new added course
    And the admin assign a <professor> to the class
    And the admin input the class section <sectionNum>
    And the admin input the class enrolled <enrolledNum>
    And the admin input the class enrollCapacity <enrolledCapacity>
    And the admin choose the enrolled Deadline <enrollDeadline>
    And the admin choose the Drop no penalty Deadline <DnpDL>
    And the admin choose the Drop no fail Deadline <DnfDL>
    And the admin input the class description <description>
    Then The admin press "submit" button to submit class
    And the system add the new class



    Examples:
      | professor | sectionNum | enrolledNum | enrolledCapacity | enrollDeadline      | DnpDL               | DnfDL               | description |
      | 2000000   | 1          | 0           | 120              | "2021-2-5 20:30:30" | "2021-3-5 20:30:30" | "2021-4-5 20:30:30" | "test"      |