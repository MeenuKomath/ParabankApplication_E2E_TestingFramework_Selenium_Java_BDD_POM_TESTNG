package stepdefinitions;

import base.DriverManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import pages.AccountsOverviewPage;

import java.util.List;

public class AccountsOverviewSteps {

    private final AccountsOverviewPage accountsOverviewPage = new AccountsOverviewPage(DriverManager.getDriver());

    @Then("the Accounts Overview table should be visible with account records")
    public void verifyAccountsOverviewTableIsVisible() {
        Assert.assertTrue(
                accountsOverviewPage.isAccountsTableDisplayed(),
                "Accounts Overview table or account rows should be visible."
        );
    }

    @Then("the welcome message should display {string}")
    public void verifyWelcomeMessage(String expectedWelcomeText) {
        String actualWelcomeText = accountsOverviewPage.getWelcomeMessageText();
        Assert.assertEquals(
                actualWelcomeText,
                expectedWelcomeText,
                "Welcome message text did not match expected user name."
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

        Assert.assertEquals(
                actualLinks,
                expectedLinks,
                "Account Services links do not match the expected menu items."
        );
    }

}
