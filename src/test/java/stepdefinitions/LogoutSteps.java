package stepdefinitions;

import base.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.LogoutPage;

public class LogoutSteps {

    private final LogoutPage logoutPage = new LogoutPage(DriverManager.getDriver());

    @When("user clicks the Log Out link")
    public void userClicksLogoutLink() {

        logoutPage.clickLogoutLink();
    }

    @Then("user should be logged out and see the Customer Login form")
    public void verifyRedirectedToLoginForm() {

        Assert.assertTrue(logoutPage.isLoginPanelDisplayed(),
                "Customer Login panel and input fields should be displayed after logging out.");
        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("index.htm"),
                "URL should redirect back to index.htm after logout.");
    }

    @And("the Log Out link should no longer be visible")
    public void verifyLogoutLinkNotVisible() {

        Assert.assertTrue(logoutPage.isLogoutLinkHidden(), "Log Out link should not be present after ending the session.");
    }
}
