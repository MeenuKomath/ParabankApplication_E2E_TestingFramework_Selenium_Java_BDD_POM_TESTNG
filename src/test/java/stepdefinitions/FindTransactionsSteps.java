package stepdefinitions;

import base.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.FindTransactionsPage;
import utils.ExcelReader;
import utils.ScenarioContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FindTransactionsSteps {

    private final FindTransactionsPage findTransactionsPage = new FindTransactionsPage(DriverManager.getDriver());
    private static final String EXCEL_PATH = "src/test/resources/testdata/TestData.xlsx";

    @When("user navigates to the Find Transactions page")
    public void navigateToFindTransactions() {

        findTransactionsPage.navigateToFindTransactionsPage();
    }

    @And("user searches transactions by amount from Excel sheet {string} and row {int}")
    public void searchTransactionsByAmountFromExcel(String sheetName, int rowIndex) throws IOException {

        List<Map<String, String>> testData = ExcelReader.getData(EXCEL_PATH, sheetName);
        String rawAmount = testData.get(rowIndex).get("Amount").replace("$", "").trim();

        String searchAmount = rawAmount.endsWith(".00") ? rawAmount.replace(".00", "") : rawAmount;

        String paidFromAccount = ScenarioContext.get("BILL_PAY_FROM_ACCOUNT", String.class);
        if (paidFromAccount != null && !paidFromAccount.isEmpty()) {

            findTransactionsPage.selectAccountByValue(paidFromAccount);
        } else {
            findTransactionsPage.selectAccountByIndex(0);
        }
        findTransactionsPage.searchByAmount(searchAmount);
    }

    @Then("the transaction results table should display a matching record from Excel sheet {string} and row {int}")
    public void verifyTransactionResultsFromExcel(String sheetName, int rowIndex) throws IOException {

        List<Map<String, String>> testData = ExcelReader.getData(EXCEL_PATH, sheetName);
        Map<String, String> expectedData = testData.get(rowIndex);

        String expectedPayeeText = expectedData.get("PayeeName");
        double numericAmount = Double.parseDouble(expectedData.get("Amount").replace("$", "").trim());
        String expectedFormattedAmount = String.format("$%.2f", numericAmount);

        Assert.assertTrue(findTransactionsPage.areTransactionResultsDisplayed(),
                "Transaction results table should be visible with at least one matching row.");

        List<String> rows = findTransactionsPage.getTransactionRowTexts();
        boolean matchFound = rows.stream().anyMatch(row -> row.contains(expectedPayeeText) && row.contains(expectedFormattedAmount));

        Assert.assertTrue(matchFound, "Expected transaction containing '" + expectedPayeeText +
                "' and '" + expectedFormattedAmount + "' but found: " + rows);
    }
}
