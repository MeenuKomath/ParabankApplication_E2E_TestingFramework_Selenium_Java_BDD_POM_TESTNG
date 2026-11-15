Feature: ParaBank Accounts Overview Page Verification

  Background:
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row 0

  @Smoke @AccountsOverview
  Scenario: Verify Accounts Overview table is visible
    Then the Accounts Overview table should be visible with account records

  @Smoke @AccountsOverview
  Scenario: Verify the Welcome UserName message is displayed
    Then the welcome message should display "Welcome John Smith"

  @Smoke @AccountsOverview
  Scenario: Verify all links under Account Services subheading are available
    Then under Account Services subheading the following links should be displayed:

      | Open New Account    |
      | Accounts Overview   |
      | Transfer Funds      |
      | Bill Pay            |
      | Find Transactions   |
      | Update Contact Info |
      | Request Loan        |
      | Log Out             |
