Feature: ParaBank Bill Payment Functionality

  Background:
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row 0

  @Smoke @BillPay
  Scenario: Verify user can complete a bill payment and view confirmation
    Given user navigates to the Bill Pay page
    When user enters payee details and submits a bill payment:

      | PayeeName | City Power & Light |
      | Address   | 450 Main Street    |
      | City      | Springfield        |
      | State     | IL                 |
      | ZipCode   | 62701              |
      | Phone     | 555-0199           |
      | Account   | 987654             |
      | Amount    | 75.50              |
    Then user should see the "Bill Payment Complete" confirmation for payee "City Power & Light" and amount "$75.50"
