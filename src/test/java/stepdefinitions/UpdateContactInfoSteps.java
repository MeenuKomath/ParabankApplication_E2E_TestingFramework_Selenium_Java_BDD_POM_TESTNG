package stepdefinitions;

import base.DriverManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.UpdateContactInfoPage;

import java.util.Map;

public class UpdateContactInfoSteps {

    private final UpdateContactInfoPage updateContactInfoPage = new UpdateContactInfoPage(DriverManager.getDriver());

    @Given("user navigates to the Update Contact Info page")
    @When("user navigates back to the Update Contact Info page")
    public void navigateToUpdateContactInfo() {
        updateContactInfoPage.navigateToUpdateContactInfoPage();
    }

    @When("user updates contact information with the following details:")
    public void updateContactInformation(DataTable dataTable) {
        Map<String, String> contactData = dataTable.asMap(String.class, String.class);
        updateContactInfoPage.updateContactDetails(contactData);
    }

    @And("user clicks the Update Profile button")
    public void clickUpdateProfileButton() {
        updateContactInfoPage.clickUpdateProfileButton();
    }

    @Then("user should see the {string} confirmation message")
    public void verifyProfileUpdatedConfirmation(String expectedHeading) {
        Assert.assertTrue(updateContactInfoPage.isProfileUpdatedDisplayed(), "Profile Updated confirmation should be displayed.");
        Assert.assertEquals(updateContactInfoPage.getResultHeading(), expectedHeading, "Heading mismatch.");
        Assert.assertTrue(
                updateContactInfoPage.getResultMessage().contains("Your updated address and phone number have been added to the system."),
                "Confirmation message text mismatch."
        );
    }

    @Then("the updated address {string} and phone {string} should be saved")
    public void verifyPersistedContactDetails(String expectedAddress, String expectedPhone) {
        Assert.assertEquals(updateContactInfoPage.getFieldValue("customer.address.street"), expectedAddress, "Saved street address mismatch.");
        Assert.assertEquals(updateContactInfoPage.getFieldValue("customer.phoneNumber"), expectedPhone, "Saved phone number mismatch.");
    }
}
