package pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LoggerUtil;
import utils.VideoRecorderUtil;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class OpenNewAccountPage {
    private static final Logger log = LoggerUtil.getLogger(OpenNewAccountPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Navigation & Dropdown Locators
    private final By openNewAccountLink = By.linkText("Open New Account");
    private final By accountTypeDropdown = By.id("type");
    private final By fromAccountDropdown = By.id("fromAccountId");
    private final By openNewAccountButton = By.xpath("//input[@value='Open New Account']");

    // Confirmation Section Locators
    private final By openAccountResultSection = By.id("openAccountResult");
    private final By accountOpenedHeading = By.xpath("//div[@id='openAccountResult']/h1");
    private final By accountOpenedMessage = By.xpath("//div[@id='openAccountResult']/p[1]");
    private final By newAccountIdLink = By.id("newAccountId");

    public OpenNewAccountPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToOpenNewAccountPage() {
        log.info("Clicking Open New Account link");
        wait.until(ExpectedConditions.elementToBeClickable(openNewAccountLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountTypeDropdown));
        VideoRecorderUtil.captureStepFrame();
    }

    public List<String> getAvailableAccountTypes() {
        Select typeSelect = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(accountTypeDropdown)));
        return typeSelect.getOptions()
                .stream()
                .map(option -> option.getText().trim())
                .collect(Collectors.toList());
    }

    public void selectAccountType(String accountType) {
        log.info("Selecting account type: {}", accountType);
        Select typeSelect = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(accountTypeDropdown)));
        typeSelect.selectByVisibleText(accountType.toUpperCase());
        VideoRecorderUtil.captureStepFrame();
    }

    public String getSelectedAccountType() {
        Select typeSelect = new Select(driver.findElement(accountTypeDropdown));
        return typeSelect.getFirstSelectedOption().getText().trim();
    }

    public void selectExistingAccountByIndex(int index) {
        WebElement dropdownElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fromAccountDropdown));
        // Wait until AJAX populates at least one existing account option
        wait.until(d -> new Select(dropdownElement).getOptions().size() > 0);

        Select fromAccountSelect = new Select(dropdownElement);
        fromAccountSelect.selectByIndex(index);
        String selectedAccount = fromAccountSelect.getFirstSelectedOption().getText().trim();
        log.info("Selected existing funding account: {}", selectedAccount);
        VideoRecorderUtil.captureStepFrame();
    }

    public String getSelectedFromAccount() {
        Select fromAccountSelect = new Select(driver.findElement(fromAccountDropdown));
        return fromAccountSelect.getFirstSelectedOption().getText().trim();
    }

    public void clickOpenNewAccountButton() {
        log.info("Clicking Open New Account submit button");
        wait.until(ExpectedConditions.elementToBeClickable(openNewAccountButton)).click();
        VideoRecorderUtil.captureStepFrame();
    }

    public boolean isAccountOpenedConfirmationDisplayed() {
        log.info("Verifying Account Opened confirmation section");
        WebElement resultSection = wait.until(ExpectedConditions.visibilityOfElementLocated(openAccountResultSection));
        VideoRecorderUtil.captureStepFrame();
        return resultSection.isDisplayed();
    }

    public String getAccountOpenedHeading() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(accountOpenedHeading)).getText().trim();
    }

    public String getAccountOpenedMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(accountOpenedMessage)).getText().trim();
    }

    public String getNewAccountNumber() {
        String newAccountId = wait.until(ExpectedConditions.visibilityOfElementLocated(newAccountIdLink)).getText().trim();
        log.info("Generated New Account Number: {}", newAccountId);
        VideoRecorderUtil.captureStepFrame();
        return newAccountId;
    }
}
