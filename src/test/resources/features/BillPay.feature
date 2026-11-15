Feature: ParaBank Bill Payment Functionality

  Background:
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row 0

  @Smoke @BillPay
  Scenario Outline: Verify user can complete a bill payment and view confirmation using Excel row <RowNumber>
    Given user navigates to the Bill Pay page
    When user enters payee details and submits a bill payment from Excel sheet "BillPayData" and row <RowNumber>
    Then user should see the "Bill Payment Complete" bill payment confirmation matching Excel sheet "BillPayData" and row <RowNumber>

    Examples:

      | RowNumber |
      | 0         |
      | 1         |
