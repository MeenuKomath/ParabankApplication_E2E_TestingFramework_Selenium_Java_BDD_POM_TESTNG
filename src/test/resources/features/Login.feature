Feature: ParaBank User Authentication

  @Smoke
  Scenario Outline: Verify ParaBank login functionality using Excel data
    Given user navigates to the ParaBank login page
    When user logs in using Excel sheet "LoginData" and row <RowNumber>
    Then user should see the expected login outcome from sheet "LoginData" and row <RowNumber>

    Examples:

      | RowNumber |
      | 0         |
      | 1         |
