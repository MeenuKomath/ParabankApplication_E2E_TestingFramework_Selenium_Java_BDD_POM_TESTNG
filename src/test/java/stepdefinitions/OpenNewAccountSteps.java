package stepdefinitions;

import base.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.OpenNewAccountPage;
import utils.ScenarioContext;

import java.util.List;

public class OpenNewAccountSteps {

    private final OpenNewAccountPage openNewAccountPage = new OpenNewAccountPage(DriverManager.getDriver());

    @And("user navigates to the Open New Account page")
    public void userNavigatesToOpenNewAccountPage() {
        openNewAccountPage.navigateToOpenNewAccountPage();
    }

    @Then("the account type dropdown should contain options {string} and {string}")
    public void verifyAccountTypeDropdownOptions(String option1, String option2) {
        List<String> actualOptions = openNewAccountPage.getAvailableAccountTypes();
        Assert.assertTrue(actualOptions.contains(option1), "Dropdown should contain option: " + option1);
        Assert.assertTrue(actualOptions.contains(option2), "Dropdown should contain option: " + option2);
    }

    @When("user selects {string} from the account type dropdown")
    public void userSelectsAccountType(String accountType) {
        openNewAccountPage.selectAccountType(accountType);
    }

    @Then("{string} should be selected in the account type dropdown")
    public void verifySelectedAccountType(String expectedType) {
        Assert.assertEquals(
                openNewAccountPage.getSelectedAccountType(),
                expectedType,
                "Selected account type does not match expected value."
        );
    }

    @When("user selects an existing account from the transfer funds dropdown")
    public void userSelectsExistingAccount() {
        openNewAccountPage.selectExistingAccountByIndex(0);
    }

    @Then("a valid existing account number should be selected")
    public void verifyExistingAccountSelected() {
        String selectedAccount = openNewAccountPage.getSelectedFromAccount();
        Assert.assertFalse(selectedAccount.isEmpty(), "An existing account number should be selected.");
        Assert.assertTrue(selectedAccount.matches("\\d+"), "Selected account should be numeric, found: " + selectedAccount);
    }

    @When("user clicks the Open New Account button")
    public void userClicksOpenNewAccountButton() {
        openNewAccountPage.clickOpenNewAccountButton();
    }

    @Then("user should see the {string} heading and confirmation message")
    public void verifyAccountOpenedHeadingAndMessage(String expectedHeading) {
        Assert.assertTrue(openNewAccountPage.isAccountOpenedConfirmationDisplayed(), "Account Opened confirmation section should be displayed."
        );
        Assert.assertEquals(openNewAccountPage.getAccountOpenedHeading(), expectedHeading, "Confirmation heading mismatch."
        );
        Assert.assertTrue(
                openNewAccountPage.getAccountOpenedMessage().contains("Congratulations, your account is now open."),
                "Expected congratulations message was not displayed."
        );
    }

    @And("a valid new Account Number should be displayed")
    public void verifyNewAccountNumberDisplayed() {
        String newAccountNumber = openNewAccountPage.getNewAccountNumber();
        Assert.assertFalse(newAccountNumber.isEmpty(), "New Account Number should not be empty.");
        Assert.assertTrue(newAccountNumber.matches("\\d+"), "New Account Number should be numeric, found: " + newAccountNumber);

        // Store the generated account ID so TransferFundsSteps can retrieve it
        ScenarioContext.set("NEW_ACCOUNT_ID", newAccountNumber);
    }

}
