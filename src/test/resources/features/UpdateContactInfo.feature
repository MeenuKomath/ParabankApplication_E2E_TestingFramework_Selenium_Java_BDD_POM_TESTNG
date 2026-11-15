Feature: ParaBank Update Contact Info Functionality

  Background:
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row 0

  @Smoke @UpdateContact
  Scenario Outline: Verify user can update contact information and persist changes using Excel row <RowNumber>
    Given user navigates to the Update Contact Info page
    When user updates contact information from Excel sheet "ContactData" and row <RowNumber>
    And user clicks the Update Profile button
    Then user should see the "Profile Updated" confirmation message
    When user navigates back to the Update Contact Info page
    Then the updated contact details from Excel sheet "ContactData" and row <RowNumber> should be saved

    Examples:

      | RowNumber |
      | 0         |
