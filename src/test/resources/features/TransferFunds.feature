Feature: ParaBank Transfer Funds End-to-End Functionality

  Background:
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row 0

  @Smoke @TransferFunds
  Scenario: Open a new account, transfer funds to it, and verify updated balances
    Given user navigates to the Open New Account page
    When user selects "SAVINGS" from the account type dropdown
    And user selects an existing account from the transfer funds dropdown
    And user clicks the Open New Account button
    Then user should see the "Account Opened!" heading and confirmation message
    And a valid new Account Number should be displayed
    When user checks initial balances on the Accounts Overview page
    And user navigates to the Transfer Funds page
    And user transfers amount "50.00" from the primary account to the newly created account
    Then user should see the "Transfer Complete!" confirmation with amount "$50.00"
    And the updated balances on Accounts Overview should reflect the "50.00" transfer
