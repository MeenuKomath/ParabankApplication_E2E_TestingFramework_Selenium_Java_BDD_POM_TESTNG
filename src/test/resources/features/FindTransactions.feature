Feature: ParaBank Find Transactions Functionality

  Background:
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row 0

  @Smoke @FindTransactions
  Scenario Outline: Pay a bill and locate the matching transaction using Excel sheet "BillPayData" and row <RowNumber>
    Given user navigates to the Bill Pay page
    When user enters payee details and submits a bill payment from Excel sheet "BillPayData" and row <RowNumber>
    Then user should see the "Bill Payment Complete" bill payment confirmation matching Excel sheet "BillPayData" and row <RowNumber>
    When user navigates to the Find Transactions page
    And user searches transactions by amount from Excel sheet "BillPayData" and row <RowNumber>
    Then the transaction results table should display a matching record from Excel sheet "BillPayData" and row <RowNumber>

    Examples:

      | RowNumber |
      | 1         |
