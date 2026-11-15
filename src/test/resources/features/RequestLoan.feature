Feature: ParaBank Request Loan Functionality

  Background:
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row 0

  @Smoke @RequestLoan
  Scenario Outline: Verify loan application outcome for <ScenarioType>
    Given user navigates to the Request Loan page
    When user applies for a loan of "<LoanAmount>" with down payment "<DownPayment>"
    Then user should see the "Loan Request Processed" confirmation with status "<ExpectedStatus>"

    Examples:

      | ScenarioType      | LoanAmount | DownPayment | ExpectedStatus |
      | Approved Loan     | 100.00     | 20.00       | Approved       |
      | Insufficient Down | 1000000.00 | 10.00       | Denied         |
