package stepdefinitions;

import base.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.BillPayPage;
import utils.ExcelReader;
import utils.ScenarioContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BillPaySteps {

    private final BillPayPage billPayPage = new BillPayPage(DriverManager.getDriver());
    private static final String EXCEL_PATH = "src/test/resources/testdata/TestData.xlsx";

    @Given("user navigates to the Bill Pay page")
    public void navigateToBillPay() {

        billPayPage.navigateToBillPayPage();
    }

    @When("user enters payee details and submits a bill payment from Excel sheet {string} and row {int}")
    public void enterPayeeDetailsAndSubmitFromExcel(String sheetName, int rowIndex) throws IOException {

        List<Map<String, String>> testData = ExcelReader.getData(EXCEL_PATH, sheetName);
        Map<String, String> payeeData = testData.get(rowIndex);

        billPayPage.enterPayeeDetails(payeeData);
        String fromAccount = billPayPage.selectFromAccountByIndex(0);
        ScenarioContext.set("BILL_PAY_FROM_ACCOUNT", fromAccount);
        billPayPage.clickSendPayment();
    }

    @Then("user should see the {string} bill payment confirmation matching Excel sheet {string} and row {int}")
    public void verifyBillPayConfirmationFromExcel(String expectedHeading, String sheetName, int rowIndex) throws IOException {

        List<Map<String, String>> testData = ExcelReader.getData(EXCEL_PATH, sheetName);
        Map<String, String> expectedData = testData.get(rowIndex);

        String expectedPayee = expectedData.get("PayeeName");
        double numericAmount = Double.parseDouble(expectedData.get("Amount").replace("$", "").trim());
        String expectedFormattedAmount = String.format("$%.2f", numericAmount);
        String expectedAccount = ScenarioContext.get("BILL_PAY_FROM_ACCOUNT", String.class);

        Assert.assertTrue(billPayPage.isBillPaymentCompleteDisplayed(), "Bill Payment Complete section should be visible.");
        Assert.assertEquals(billPayPage.getResultHeading(), expectedHeading, "Bill payment heading mismatch.");
        Assert.assertEquals(billPayPage.getConfirmedPayeeName(), expectedPayee, "Confirmed payee name mismatch.");
        Assert.assertEquals(billPayPage.getConfirmedAmount(), expectedFormattedAmount, "Confirmed payment amount mismatch.");
        Assert.assertEquals(billPayPage.getConfirmedFromAccount(), expectedAccount, "Confirmed From Account mismatch.");
    }
}
