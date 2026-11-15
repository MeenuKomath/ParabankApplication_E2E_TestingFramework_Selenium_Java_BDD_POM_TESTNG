Feature: ParaBank Open New Account Functionality

  Background:
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row 0
    And user navigates to the Open New Account page

  @Smoke @OpenAccount
  Scenario: Verify Account Type dropdown contains CHECKING and SAVINGS options
    Then the account type dropdown should contain options "CHECKING" and "SAVINGS"

  @Smoke @OpenAccount
  Scenario Outline: Verify user can open a new <AccountType> account and view generated account number
    When user selects "<AccountType>" from the account type dropdown
    Then "<AccountType>" should be selected in the account type dropdown
    When user selects an existing account from the transfer funds dropdown
    Then a valid existing account number should be selected
    When user clicks the Open New Account button
    Then user should see the "Account Opened!" heading and confirmation message
    And a valid new Account Number should be displayed

    Examples:

      | AccountType |
      | CHECKING    |
      | SAVINGS     |
