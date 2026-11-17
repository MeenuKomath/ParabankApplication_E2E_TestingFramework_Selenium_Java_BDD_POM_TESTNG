package stepdefinitions;

import base.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.LoginPage;
import utils.ExcelReader;
import utils.ConfigReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LoginSteps {

    private final LoginPage loginPage = new LoginPage(DriverManager.getDriver());
    private static final String EXCEL_PATH = "src/test/resources/testdata/TestData.xlsx";

    @Given("user navigates to the ParaBank login page")
    public void userNavigatesToParaBankLoginPage() {

        DriverManager.getDriver().get(ConfigReader.getUrl());
    }

    @When("user logs in using Excel sheet {string} and row {int}")
    public void userLogsInUsingExcel(String sheetName, int rowIndex) throws IOException {

        List<Map<String, String>> testData = ExcelReader.getData(EXCEL_PATH, sheetName);
        String username = testData.get(rowIndex).get("Username");
        String password = testData.get(rowIndex).get("Password");

        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
    }

    @Then("user should see the expected login outcome from sheet {string} and row {int}")
    public void verifyLoginOutcome(String sheetName, int rowIndex) throws IOException {

        List<Map<String, String>> testData = ExcelReader.getData(EXCEL_PATH, sheetName);
        String expectedHeading = testData.get(rowIndex).get("ExpectedResult");

        String actualHeading = loginPage.getPageHeadingText();
        Assert.assertTrue(actualHeading.contains(expectedHeading), "Expected heading to contain '" +
                expectedHeading + "' but found '" + actualHeading + "'");

        if (expectedHeading.equalsIgnoreCase("Accounts Overview")) {
            Assert.assertTrue(loginPage.isLogoutDisplayed(), "Log Out link should be visible after login.");
            Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("overview.htm"),
                    "URL should redirect to overview.htm");
        }
    }
}
