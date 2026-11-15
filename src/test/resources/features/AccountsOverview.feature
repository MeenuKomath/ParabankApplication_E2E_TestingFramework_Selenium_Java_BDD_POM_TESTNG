Feature: ParaBank Accounts Overview Page Verification

  Background:
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row 0

  @Smoke @AccountsOverview
  Scenario: Verify Accounts Overview heading and table are visible
    Then the page heading should be "Accounts Overview"
    And the Accounts Overview table should be visible with account records

  @Smoke @AccountsOverview
  Scenario: Verify Accounts Overview table column headers
    Then the Accounts Overview table headers should be:

      | Account          |
      | Balance*         |
      | Available Amount |

  @Smoke @AccountsOverview
  Scenario: Verify Total balance equals the sum of all account balances
    Then the displayed Total balance should match the sum of individual account balances

  @Smoke @AccountsOverview
  Scenario: Verify the Welcome UserName message is displayed from Excel data
    Then the welcome message should display the Account Holder Name from Excel sheet "LoginData" and row 0

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

  @Smoke @AccountsOverview
  Scenario: Verify clicking an account number navigates to Account Activity details
    When user clicks the first account number link in the Accounts Overview table
    Then user should be redirected to the Account Details page showing the matching account number
