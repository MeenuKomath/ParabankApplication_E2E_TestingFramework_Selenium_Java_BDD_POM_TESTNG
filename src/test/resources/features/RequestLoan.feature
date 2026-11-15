Feature: ParaBank Request Loan Functionality

  Background:
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row 0

  @Smoke @RequestLoan
  Scenario Outline: Verify loan application outcome using Excel data row <RowNumber>
    Given user navigates to the Request Loan page
    When user applies for a loan using Excel sheet "LoanData" and row <RowNumber>
    Then user should see the "Loan Request Processed" confirmation matching Excel sheet "LoanData" and row <RowNumber>

    Examples:

      | RowNumber |
      | 0         |
      | 1         |
