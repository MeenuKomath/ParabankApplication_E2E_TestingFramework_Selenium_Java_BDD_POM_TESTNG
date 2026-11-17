package stepdefinitions;

import base.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.UpdateContactInfoPage;
import utils.ExcelReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UpdateContactInfoSteps {

    private final UpdateContactInfoPage updateContactInfoPage = new UpdateContactInfoPage(DriverManager.getDriver());
    private static final String EXCEL_PATH = "src/test/resources/testdata/TestData.xlsx";

    @Given("user navigates to the Update Contact Info page")
    @When("user navigates back to the Update Contact Info page")
    public void navigateToUpdateContactInfo() {

        updateContactInfoPage.navigateToUpdateContactInfoPage();
    }

    @When("user updates contact information from Excel sheet {string} and row {int}")
    public void updateContactInformationFromExcel(String sheetName, int rowIndex) throws IOException {

        List<Map<String, String>> testData = ExcelReader.getData(EXCEL_PATH, sheetName);
        Map<String, String> contactData = testData.get(rowIndex);
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
        Assert.assertTrue(updateContactInfoPage.getResultMessage().contains("Your updated address and phone number have been added to the system."),
                "Confirmation message text mismatch.");
    }

    @Then("the updated contact details from Excel sheet {string} and row {int} should be saved")
    public void verifyPersistedContactDetailsFromExcel(String sheetName, int rowIndex) throws IOException {

        List<Map<String, String>> testData = ExcelReader.getData(EXCEL_PATH, sheetName);
        Map<String, String> expectedData = testData.get(rowIndex);

        Assert.assertEquals(updateContactInfoPage.getFieldValue("customer.address.street"),
                expectedData.get("Address"), "Saved street address mismatch.");
        Assert.assertEquals(updateContactInfoPage.getFieldValue("customer.address.city"),
                expectedData.get("City"), "Saved city mismatch.");
        Assert.assertEquals(updateContactInfoPage.getFieldValue("customer.address.state"),
                expectedData.get("State"), "Saved state mismatch.");
        Assert.assertEquals(updateContactInfoPage.getFieldValue("customer.address.zipCode"),
                expectedData.get("ZipCode"), "Saved zip code mismatch.");
        Assert.assertEquals(updateContactInfoPage.getFieldValue("customer.phoneNumber"),
                expectedData.get("Phone"), "Saved phone number mismatch.");
    }
}
