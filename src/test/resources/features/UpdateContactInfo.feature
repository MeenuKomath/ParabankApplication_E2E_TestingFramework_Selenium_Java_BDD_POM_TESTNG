Feature: ParaBank Update Contact Info Functionality

  Background:
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row 0

  @Smoke @UpdateContact
  Scenario: Verify user can update contact information and persist changes
    Given user navigates to the Update Contact Info page
    When user updates contact information with the following details:

      | FirstName | John             |
      | LastName  | Smith            |
      | Address   | 742 Evergreen St |
      | City      | Beverly Hills    |
      | State     | CA               |
      | ZipCode   | 90210            |
      | Phone     | 310-555-0192     |
    And user clicks the Update Profile button
    Then user should see the "Profile Updated" confirmation message
    When user navigates back to the Update Contact Info page
    Then the updated address "742 Evergreen St" and phone "310-555-0192" should be saved
