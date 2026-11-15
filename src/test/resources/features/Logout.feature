Feature: ParaBank User Logout Functionality

  Background:
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row 0

  @Smoke @Logout
  Scenario: Verify user can log out and is redirected to the login screen
    When user clicks the Log Out link
    Then user should be logged out and see the Customer Login form
    And the Log Out link should no longer be visible
