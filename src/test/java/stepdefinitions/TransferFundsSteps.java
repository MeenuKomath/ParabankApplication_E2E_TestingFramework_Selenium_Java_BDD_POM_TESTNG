package stepdefinitions;

import base.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.AccountsOverviewPage;
import pages.TransferFundsPage;
import utils.ScenarioContext;

public class TransferFundsSteps {

    private final TransferFundsPage transferFundsPage = new TransferFundsPage(DriverManager.getDriver());
    private final AccountsOverviewPage accountsOverviewPage = new AccountsOverviewPage(DriverManager.getDriver());

    @When("user checks initial balances on the Accounts Overview page")
    public void recordInitialBalances() {

        String newAccountId = ScenarioContext.get("NEW_ACCOUNT_ID", String.class);
        Assert.assertNotNull(newAccountId, "NEW_ACCOUNT_ID was not saved in ScenarioContext from the Open New Account step.");

        transferFundsPage.navigateToTransferFundsPage();
        String sourceAccountId = transferFundsPage.selectSourceAccountByIndex(0);
        ScenarioContext.set("SOURCE_ACCOUNT_ID", sourceAccountId);

        accountsOverviewPage.navigateToAccountsOverview();
        double initialSourceBalance = accountsOverviewPage.getAccountBalance(sourceAccountId);
        double initialNewAccountBalance = accountsOverviewPage.getAccountBalance(newAccountId);

        ScenarioContext.set("INITIAL_SOURCE_BALANCE", initialSourceBalance);
        ScenarioContext.set("INITIAL_NEW_BALANCE", initialNewAccountBalance);
    }


    @And("user navigates to the Transfer Funds page")
    public void navigateToTransferFunds() {

        transferFundsPage.navigateToTransferFundsPage();
    }

    @And("user transfers amount {string} from the primary account to the newly created account")
    public void performTransfer(String amount) {

        String sourceAccountId = ScenarioContext.get("SOURCE_ACCOUNT_ID", String.class);
        String newAccountId = ScenarioContext.get("NEW_ACCOUNT_ID", String.class);

        transferFundsPage.enterAmount(amount);
        transferFundsPage.selectSourceAccountByIndex(0);
        transferFundsPage.selectDestinationAccountByValue(newAccountId);
        transferFundsPage.clickTransferButton();
    }

    @Then("user should see the {string} confirmation with amount {string}")
    public void verifyTransferConfirmation(String expectedHeading, String expectedFormattedAmount) {

        String sourceAccountId = ScenarioContext.get("SOURCE_ACCOUNT_ID", String.class);
        String newAccountId = ScenarioContext.get("NEW_ACCOUNT_ID", String.class);

        Assert.assertTrue(transferFundsPage.isTransferCompleteDisplayed(), "Transfer Complete result section should be visible.");
        Assert.assertEquals(transferFundsPage.getTransferCompleteHeading(), expectedHeading, "Transfer heading mismatch.");
        Assert.assertEquals(transferFundsPage.getConfirmedAmount(), expectedFormattedAmount, "Confirmed transfer amount mismatch.");
        Assert.assertEquals(transferFundsPage.getConfirmedFromAccount(), sourceAccountId, "Source account ID mismatch.");
        Assert.assertEquals(transferFundsPage.getConfirmedToAccount(), newAccountId, "Destination account ID mismatch.");
    }

    @And("the updated balances on Accounts Overview should reflect the {string} transfer")
    public void verifyUpdatedAccountBalances(String transferredAmountStr) {

        double transferredAmount = Double.parseDouble(transferredAmountStr);
        String sourceAccountId = ScenarioContext.get("SOURCE_ACCOUNT_ID", String.class);
        String newAccountId = ScenarioContext.get("NEW_ACCOUNT_ID", String.class);

        double initialSourceBalance = ScenarioContext.get("INITIAL_SOURCE_BALANCE", Double.class);
        double initialNewBalance = ScenarioContext.get("INITIAL_NEW_BALANCE", Double.class);

        accountsOverviewPage.navigateToAccountsOverview();
        double updatedSourceBalance = accountsOverviewPage.getAccountBalance(sourceAccountId);
        double updatedNewBalance = accountsOverviewPage.getAccountBalance(newAccountId);

        // 1. Source account is shared across parallel threads, so verify it decreased by at least the transferred amount
        Assert.assertTrue(updatedSourceBalance <= (initialSourceBalance - transferredAmount + 0.01),
                "Source account balance should decrease by at least $" + transferredAmount
                        + " (Initial: " + initialSourceBalance + ", Updated: " + updatedSourceBalance + ")");

        // 2. Newly created account is isolated to this thread, so verify exact +$50.00 increase
        Assert.assertEquals(updatedNewBalance,
                initialNewBalance + transferredAmount, 0.01,
                "Newly created account balance should increase by exact $" + transferredAmount);
    }

}
