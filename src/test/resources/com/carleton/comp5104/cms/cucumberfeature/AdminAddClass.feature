@tag
Feature: Admin add a new class

  @SectionNumValidCheck
  Scenario Outline: admin success add a new class
    Given A test course has been added to the course table
    And the admin search the new added course
    When The admin press "addClass" button to add class
    And the new class is under the new added course
    And the admin assign a <professor> to the class
    And the admin input the class section <sectionNum0>
    And the system check if the section Num is valid
    And the admin input the class section <sectionNum1>
    And the system check if the section Num is valid
    And the admin input the class enrolled <enrolledNum0>
    And the admin input the class enrollCapacity <enrolledCapacity0>
    And the system check if the enrolledNum and enrolledCapacity is valid
    And the admin choose the enrolled Deadline <enrollDeadline>
    And the admin choose the Drop no penalty Deadline <DnpDL>
    And the admin choose the Drop no fail Deadline <DnfDL>
    And the system check if the three is valid
    And the admin input the class description <description>
    Then The admin press "submit" button to submit class
    And the system add the new class



    Examples:
      | professor | sectionNum0 | sectionNum1 | enrolledNum0 | enrolledCapacity0 | enrollDeadline      | DnpDL               | DnfDL               | description |
      | 2000000   | 0           | 1           | 0            | 120               | "2021-2-5 20:30:30" | "2021-3-5 20:30:30" | "2021-4-5 20:30:30" | "test"      |
      | 2000000   | 1           | 2           | 0            | 120               | "2021-2-5 20:30:30" | "2021-3-5 20:30:30" | "2021-4-5 20:30:30" | "test"      |


  @EnrolledAndCapacityValidCheck
  Scenario Outline: admin success add a new class after several try.
    Given A test course has been added to the course table
    And the admin search the new added course
    When The admin press "addClass" button to add class
    And the new class is under the new added course
    And the admin assign a <professor> to the class
    And the admin input the class section <sectionNum0>
    And the system check if the section Num is valid
    And the admin input the class enrolled <enrolledNum0>
    And the admin input the class enrollCapacity <enrolledCapacity0>
    And the system check if the enrolledNum and enrolledCapacity is valid
    And the admin input the class enrolled <enrolledNum1>
    And the admin input the class enrollCapacity <enrolledCapacity1>
    And the system check if the enrolledNum and enrolledCapacity is valid
    And the admin choose the enrolled Deadline <enrollDeadline>
    And the admin choose the Drop no penalty Deadline <DnpDL>
    And the admin choose the Drop no fail Deadline <DnfDL>
    And the system check if the three is valid
    And the admin input the class description <description>
    Then The admin press "submit" button to submit class
    And the system add the new class

    Examples:
      | professor | sectionNum0 | enrolledNum0 | enrolledCapacity0 | enrolledNum1 | enrolledCapacity1 | enrollDeadline      | DnpDL               | DnfDL               | description |
      | 2000000   | 1           | 300          | 120               | 10           | 120               | "2021-2-5 20:30:30" | "2021-3-5 20:30:30" | "2021-4-5 20:30:30" | "test"      |

  @ThreeDateValidCheck
  Scenario Outline: admin success add a new class after several try.
    Given A test course has been added to the course table
    And the admin search the new added course
    When The admin press "addClass" button to add class
    And the new class is under the new added course
    And the admin assign a <professor> to the class
    And the admin input the class section <sectionNum0>
    And the system check if the section Num is valid
    And the admin input the class enrolled <enrolledNum0>
    And the admin input the class enrollCapacity <enrolledCapacity0>
    And the system check if the enrolledNum and enrolledCapacity is valid
    And the admin choose the enrolled Deadline <enrollDeadline>
    And the admin choose the Drop no penalty Deadline <DnpDL>
    And the admin choose the Drop no fail Deadline <DnfDL>
    And the system check if the three is valid
    And the admin choose the enrolled Deadline <enrollDeadline1>
    And the admin choose the Drop no penalty Deadline <DnpDL1>
    And the admin choose the Drop no fail Deadline <DnfDL1>
    And the system check if the three is valid
    And the admin choose the enrolled Deadline <enrollDeadline2>
    And the admin choose the Drop no penalty Deadline <DnpDL2>
    And the admin choose the Drop no fail Deadline <DnfDL2>
    And the system check if the three is valid
    And the admin choose the enrolled Deadline <enrollDeadline3>
    And the admin choose the Drop no penalty Deadline <DnpDL3>
    And the admin choose the Drop no fail Deadline <DnfDL3>
    And the system check if the three is valid
    And the admin input the class description <description>
    Then The admin press "submit" button to submit class
    And the system add the new class

    Examples:
      | professor | sectionNum0 | enrolledNum0 | enrolledCapacity0 | enrollDeadline      | DnpDL               | DnfDL               | enrollDeadline1     | DnpDL1              | DnfDL1              | enrollDeadline2     | DnpDL2              | DnfDL2              | enrollDeadline3     | DnpDL3              | DnfDL3              | description |
      | 2000000   | 1           | 300          | 120               | "2019-2-5 20:30:30" | "2021-3-5 20:30:30" | "2021-4-5 20:30:30" | "2021-2-5 20:30:30" | "2021-1-5 20:30:30" | "2021-4-5 20:30:30" | "2021-2-5 20:30:30" | "2021-3-5 20:30:30" | "2021-3-1 20:30:30" | "2021-2-5 20:30:30" | "2021-3-5 20:30:30" | "2021-4-5 20:30:30" | "test"      |

  @AddNewSchedule
  Scenario Outline: Add new schedule to the class
    Given A test course has been added to the course table
    And the admin search the new added course
    And the test class has been added to the class table
    When the admin press the add schedule button
    And the admin choose the <weekday> of the schedule
    And the admin choose the start Time <startTime>
    And the admin choose the end time <endTime>
    And the admin choose the <roomSize> the schedule needs
    And the system check if there is any room available
    And the system output the available room
    And the admin select a room for this schedule
    Then the admin save the new schedule

    Examples:
      | weekday | startTime | endTime | roomSize |
      | "Mon"   | "10:00"   | "12:00" | "200"    |