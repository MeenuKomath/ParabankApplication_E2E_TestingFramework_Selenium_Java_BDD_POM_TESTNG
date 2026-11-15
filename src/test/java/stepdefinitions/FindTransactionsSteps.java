package stepdefinitions;

import base.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.FindTransactionsPage;
import utils.ScenarioContext;

import java.util.List;

public class FindTransactionsSteps {

    private final FindTransactionsPage findTransactionsPage = new FindTransactionsPage(DriverManager.getDriver());

    @When("user navigates to the Find Transactions page")
    public void navigateToFindTransactions() {
        findTransactionsPage.navigateToFindTransactionsPage();
    }

    @And("user searches transactions by amount {string}")
    public void searchTransactionsByAmount(String amount) {
        String paidFromAccount = ScenarioContext.get("BILL_PAY_FROM_ACCOUNT", String.class);
        if (paidFromAccount != null && !paidFromAccount.isEmpty()) {
            findTransactionsPage.selectAccountByValue(paidFromAccount);
        } else {
            findTransactionsPage.selectAccountByIndex(0);
        }
        findTransactionsPage.searchByAmount(amount);
    }

    @Then("the transaction results table should display a record matching {string} and {string}")
    public void verifyTransactionResults(String expectedPayeeText, String expectedFormattedAmount) {
        Assert.assertTrue(
                findTransactionsPage.areTransactionResultsDisplayed(),
                "Transaction results table should be visible with at least one matching row."
        );

        List<String> rows = findTransactionsPage.getTransactionRowTexts();
        boolean matchFound = rows.stream()
                .anyMatch(row -> row.contains(expectedPayeeText) && row.contains(expectedFormattedAmount));

        Assert.assertTrue(
                matchFound,
                "Expected transaction containing '" + expectedPayeeText + "' and '" + expectedFormattedAmount + "' but found: " + rows
        );
    }
}
