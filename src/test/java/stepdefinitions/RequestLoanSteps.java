package stepdefinitions;

import base.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.RequestLoanPage;

public class RequestLoanSteps {

    private final RequestLoanPage requestLoanPage = new RequestLoanPage(DriverManager.getDriver());

    @Given("user navigates to the Request Loan page")
    public void navigateToRequestLoan() {
        requestLoanPage.navigateToRequestLoanPage();
    }

    @When("user applies for a loan of {string} with down payment {string}")
    public void applyForLoan(String loanAmount, String downPayment) {
        requestLoanPage.enterLoanDetails(loanAmount, downPayment);
        requestLoanPage.selectFromAccountByIndex(0);
        requestLoanPage.clickApplyNowButton();
    }

    @Then("user should see the {string} confirmation with status {string}")
    public void verifyLoanOutcome(String expectedHeading, String expectedStatus) {
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
