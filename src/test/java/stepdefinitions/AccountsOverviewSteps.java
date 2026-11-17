package stepdefinitions;

import base.DriverManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.AccountsOverviewPage;
import utils.ExcelReader;
import utils.ScenarioContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AccountsOverviewSteps {

    private final AccountsOverviewPage accountsOverviewPage = new AccountsOverviewPage(DriverManager.getDriver());
    private static final String EXCEL_PATH = "src/test/resources/testdata/TestData.xlsx";

    @Then("the page heading should be {string}")
    public void verifyPageHeading(String expectedHeading) {
        Assert.assertEquals(
                accountsOverviewPage.getPageHeading(),
                expectedHeading,
                "Accounts Overview page heading mismatch."
        );
    }

    @And("the Accounts Overview table should be visible with account records")
    public void verifyAccountsOverviewTableIsVisible() {
        Assert.assertTrue(
                accountsOverviewPage.isAccountsTableDisplayed(),
                "Accounts Overview table or account rows should be visible."
        );
    }

    @Then("the Accounts Overview table headers should be:")
    public void verifyAccountsOverviewTableHeaders(DataTable expectedHeadersTable) {
        List<String> expectedHeaders = expectedHeadersTable.asList();
        List<String> actualHeaders = accountsOverviewPage.getTableHeaderTexts();
        Assert.assertEquals(actualHeaders, expectedHeaders,
                "Accounts Overview table headers do not match."
        );
    }

    @Then("the displayed Total balance should match the sum of individual account balances")
    public void verifyTotalBalanceCalculation() {
        double expectedSum = accountsOverviewPage.calculateSumOfIndividualBalances();
        double actualTotal = accountsOverviewPage.getDisplayedTotalBalance();
        Assert.assertEquals(actualTotal, expectedSum, 0.01,
                "Displayed Total balance does not match the sum of individual account balances."
        );
    }

    @Then("the welcome message should display the Account Holder Name from Excel sheet {string} and row {int}")
    public void verifyWelcomeMessageFromExcel(String sheetName, int rowIndex) throws IOException {
        List<Map<String, String>> testData = ExcelReader.getData(EXCEL_PATH, sheetName);
        String accountHolderName = testData.get(rowIndex).get("AccountHolderName");
        String expectedWelcomeText = "Welcome " + accountHolderName;

        Assert.assertEquals(accountsOverviewPage.getWelcomeMessageText(), expectedWelcomeText,
                "Welcome message text did not match the Account Holder Name from Excel."
        );
    }

    @Then("under Account Services subheading the following links should be displayed:")
    public void verifyAccountServicesLinks(DataTable expectedLinksTable) {
        Assert.assertTrue(
                accountsOverviewPage.isAccountServicesHeaderDisplayed(),
                "Account Services subheading should be displayed."
        );

        List<String> expectedLinks = expectedLinksTable.asList();
        List<String> actualLinks = accountsOverviewPage.getAccountServicesLinkTexts();

        Assert.assertEquals(actualLinks, expectedLinks,
                "Account Services links do not match the expected menu items."
        );
    }

    @When("user clicks the first account number link in the Accounts Overview table")
    public void clickFirstAccountNumber() {
        String clickedAccountId = accountsOverviewPage.clickFirstAccountAndGetNumber();
        ScenarioContext.set("SELECTED_ACCOUNT_ID", clickedAccountId);
    }

    @Then("user should be redirected to the Account Details page showing the matching account number")
    public void verifyAccountDetailsPage() {
        String expectedAccountId = ScenarioContext.get("SELECTED_ACCOUNT_ID", String.class);
        String actualAccountId = accountsOverviewPage.getAccountDetailsPageAccountId();

        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("activity.htm"), "URL should redirect to activity.htm");
        Assert.assertEquals(actualAccountId, expectedAccountId, "Account Details page should display the clicked account number."
        );
    }
}
