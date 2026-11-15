package pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LoggerUtil;
import utils.VideoRecorderUtil;

import java.time.Duration;
import java.util.Map;

public class UpdateContactInfoPage {
    private static final Logger log = LoggerUtil.getLogger(UpdateContactInfoPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Navigation & Form Locators
    private final By updateContactLink = By.linkText("Update Contact Info");
    private final By firstNameField = By.id("customer.firstName");
    private final By lastNameField = By.id("customer.lastName");
    private final By addressField = By.id("customer.address.street");
    private final By cityField = By.id("customer.address.city");
    private final By stateField = By.id("customer.address.state");
    private final By zipCodeField = By.id("customer.address.zipCode");
    private final By phoneField = By.id("customer.phoneNumber");
    private final By updateProfileButton = By.xpath("//input[@value='Update Profile']");

    // Confirmation Locators
    private final By updateProfileResultSection = By.id("updateProfileResult");
    private final By resultHeading = By.xpath("//div[@id='updateProfileResult']/h1");
    private final By resultMessage = By.xpath("//div[@id='updateProfileResult']/p");

    public UpdateContactInfoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToUpdateContactInfoPage() {
        log.info("Navigating to Update Contact Info page");
        wait.until(ExpectedConditions.elementToBeClickable(updateContactLink)).click();
        // Wait until AJAX populates the existing firstName value so it does not overwrite our edits
        wait.until(d -> {
            String val = d.findElement(firstNameField).getAttribute("value");
            return val != null && !val.trim().isEmpty();
        });
        VideoRecorderUtil.captureStepFrame();
    }

    public void updateContactDetails(Map<String, String> details) {
        log.info("Updating contact profile details");
        clearAndType(firstNameField, details.get("FirstName"));
        clearAndType(lastNameField, details.get("LastName"));
        clearAndType(addressField, details.get("Address"));
        clearAndType(cityField, details.get("City"));
        clearAndType(stateField, details.get("State"));
        clearAndType(zipCodeField, details.get("ZipCode"));
        clearAndType(phoneField, details.get("Phone"));
        VideoRecorderUtil.captureStepFrame();
    }

    private void clearAndType(By locator, String value) {
        if (value == null) return;
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }

    public void clickUpdateProfileButton() {
        log.info("Clicking Update Profile button");
        wait.until(ExpectedConditions.elementToBeClickable(updateProfileButton)).click();
        VideoRecorderUtil.captureStepFrame();
    }

    public boolean isProfileUpdatedDisplayed() {
        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(updateProfileResultSection));
        VideoRecorderUtil.captureStepFrame();
        return result.isDisplayed();
    }

    public String getResultHeading() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(resultHeading)).getText().trim();
    }

    public String getResultMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(resultMessage)).getText().trim();
    }

    public String getFieldValue(String fieldId) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(fieldId))).getAttribute("value").trim();
    }
}
