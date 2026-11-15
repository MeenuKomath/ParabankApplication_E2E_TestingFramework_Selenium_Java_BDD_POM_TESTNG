package stepdefinitions;

import base.DriverManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.BillPayPage;
import utils.ScenarioContext;

import java.util.Map;

public class BillPaySteps {

    private final BillPayPage billPayPage = new BillPayPage(DriverManager.getDriver());

    @Given("user navigates to the Bill Pay page")
    public void navigateToBillPay() {
        billPayPage.navigateToBillPayPage();
    }

    @When("user enters payee details and submits a bill payment:")
    public void enterPayeeDetailsAndSubmit(DataTable dataTable) {
        Map<String, String> payeeData = dataTable.asMap(String.class, String.class);
        billPayPage.enterPayeeDetails(payeeData);
        String fromAccount = billPayPage.selectFromAccountByIndex(0);
        ScenarioContext.set("BILL_PAY_FROM_ACCOUNT", fromAccount);
        billPayPage.clickSendPayment();
    }

    @Then("user should see the {string} confirmation for payee {string} and amount {string}")
    public void verifyBillPayConfirmation(String expectedHeading, String expectedPayee, String expectedAmount) {
        String expectedAccount = ScenarioContext.get("BILL_PAY_FROM_ACCOUNT", String.class);

        Assert.assertTrue(billPayPage.isBillPaymentCompleteDisplayed(), "Bill Payment Complete section should be visible.");
        Assert.assertEquals(billPayPage.getResultHeading(), expectedHeading, "Bill payment heading mismatch.");
        Assert.assertEquals(billPayPage.getConfirmedPayeeName(), expectedPayee, "Confirmed payee name mismatch.");
        Assert.assertEquals(billPayPage.getConfirmedAmount(), expectedAmount, "Confirmed payment amount mismatch.");
        Assert.assertEquals(billPayPage.getConfirmedFromAccount(), expectedAccount, "Confirmed From Account mismatch.");
    }
}
