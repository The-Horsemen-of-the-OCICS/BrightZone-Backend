@tag
Feature: ONE test case to address Use case dependencies
  Scenario: Use case dependencies
    Given Admin creates a term and its deadlines
    Then S1 requests creation Admin creates C2
    And P1 and P2 simultaneously request creation
    And Admin creates C1 Admin creates C3
    And S2 and S3 simultaneously request creation
    And Admin assigns C1 to P1 assigns C3 to P2 assigns C2 to P1
    And S2 logins in, then S3, then S1, then P1, then P2
    And S2 and S3 simultaneously register in C1
    And S1 registers in C2 S1 registers in C3 S2 registers in C3
    And P1 creates deliverable Project for C1 P2 creates deliverable Essay for C3
    And S1 drops C2
    And S2 and S3 simultaneously submit Project in C1
    And S1 submits Essay in C3, S2 submits Essay in C3
    And P1 submits marks for Project in C1
    And P2 submit marks for Essay in C3
    And Simultaneously P1 and P2 submit final grades respectively for C1 and C3
    And S1, S2, S3, P1 and P2 log out

