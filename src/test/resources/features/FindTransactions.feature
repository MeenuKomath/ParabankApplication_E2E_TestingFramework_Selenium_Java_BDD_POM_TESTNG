Feature: ParaBank Find Transactions Functionality

  Background:
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row 0

  @Smoke @FindTransactions
  Scenario: Pay a bill and locate the matching transaction by amount
    Given user navigates to the Bill Pay page
    When user enters payee details and submits a bill payment:

      | PayeeName | Metro Water Utility |
      | Address   | 100 River Road      |
      | City      | Austin              |
      | State     | TX                  |
      | ZipCode   | 73301               |
      | Phone     | 555-0144            |
      | Account   | 445566              |
      | Amount    | 35.00               |
    Then user should see the "Bill Payment Complete" confirmation for payee "Metro Water Utility" and amount "$35.00"
    When user navigates to the Find Transactions page
    And user searches transactions by amount "35"
    Then the transaction results table should display a record matching "Metro Water Utility" and "$35.00"
