package stepdefinitions;

import base.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.RequestLoanPage;
import utils.ExcelReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RequestLoanSteps {

    private final RequestLoanPage requestLoanPage = new RequestLoanPage(DriverManager.getDriver());
    private static final String EXCEL_PATH = "src/test/resources/testdata/TestData.xlsx";

    @Given("user navigates to the Request Loan page")
    public void navigateToRequestLoan() {
        requestLoanPage.navigateToRequestLoanPage();
    }

    @When("user applies for a loan using Excel sheet {string} and row {int}")
    public void applyForLoanFromExcel(String sheetName, int rowIndex) throws IOException {

        List<Map<String, String>> testData = ExcelReader.getData(EXCEL_PATH, sheetName);
        String loanAmount = testData.get(rowIndex).get("LoanAmount");
        String downPayment = testData.get(rowIndex).get("DownPayment");

        requestLoanPage.enterLoanDetails(loanAmount, downPayment);
        requestLoanPage.selectFromAccountByIndex(0);
        requestLoanPage.clickApplyNowButton();
    }

    @Then("user should see the {string} confirmation matching Excel sheet {string} and row {int}")
    public void verifyLoanOutcomeFromExcel(String expectedHeading, String sheetName, int rowIndex) throws IOException {

        List<Map<String, String>> testData = ExcelReader.getData(EXCEL_PATH, sheetName);
        String expectedStatus = testData.get(rowIndex).get("ExpectedStatus");

        Assert.assertTrue(requestLoanPage.isLoanResultDisplayed(), "Loan Request Processed section should be visible.");
        Assert.assertEquals(requestLoanPage.getResultHeading(), expectedHeading, "Loan confirmation heading mismatch.");
        Assert.assertEquals(requestLoanPage.getLoanStatus(), expectedStatus, "Loan approval status mismatch.");

        if (expectedStatus.equalsIgnoreCase("Approved")) {
            String newLoanAccount = requestLoanPage.getNewLoanAccountNumber();
            Assert.assertFalse(newLoanAccount.isEmpty(), "New Loan Account ID should be generated for approved loans.");
            Assert.assertTrue(newLoanAccount.matches("\\d+"), "New Loan Account ID should be numeric, found: " + newLoanAccount);
        }
    }
}
