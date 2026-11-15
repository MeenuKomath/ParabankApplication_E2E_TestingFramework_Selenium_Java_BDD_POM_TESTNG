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
import java.util.Map;

public class BillPayPage {
    private static final Logger log = LoggerUtil.getLogger(BillPayPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Navigation & Form Locators
    private final By billPayLink = By.linkText("Bill Pay");
    private final By payeeNameField = By.name("payee.name");
    private final By addressField = By.name("payee.address.street");
    private final By cityField = By.name("payee.address.city");
    private final By stateField = By.name("payee.address.state");
    private final By zipCodeField = By.name("payee.address.zipCode");
    private final By phoneField = By.name("payee.phoneNumber");
    private final By accountField = By.name("payee.accountNumber");
    private final By verifyAccountField = By.name("verifyAccount");
    private final By amountField = By.name("amount");
    private final By fromAccountDropdown = By.name("fromAccountId");
    private final By sendPaymentButton = By.xpath("//input[@value='Send Payment']");

    // Confirmation Locators
    private final By billPayResultSection = By.id("billpayResult");
    private final By resultHeading = By.xpath("//div[@id='billpayResult']/h1");
    private final By payeeNameResult = By.id("payeeName");
    private final By amountResult = By.id("amount");
    private final By fromAccountResult = By.id("fromAccountId");

    public BillPayPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToBillPayPage() {
        log.info("Navigating to Bill Pay page");
        wait.until(ExpectedConditions.elementToBeClickable(billPayLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(payeeNameField));
        VideoRecorderUtil.captureStepFrame();
    }

    public void enterPayeeDetails(Map<String, String> details) {
        log.info("Entering Payee details for: {}", details.get("PayeeName"));
        driver.findElement(payeeNameField).sendKeys(details.get("PayeeName"));
        driver.findElement(addressField).sendKeys(details.get("Address"));
        driver.findElement(cityField).sendKeys(details.get("City"));
        driver.findElement(stateField).sendKeys(details.get("State"));
        driver.findElement(zipCodeField).sendKeys(details.get("ZipCode"));
        driver.findElement(phoneField).sendKeys(details.get("Phone"));
        driver.findElement(accountField).sendKeys(details.get("Account"));
        driver.findElement(verifyAccountField).sendKeys(details.get("Account"));
        driver.findElement(amountField).sendKeys(details.get("Amount"));
        VideoRecorderUtil.captureStepFrame();
    }

    public String selectFromAccountByIndex(int index) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(fromAccountDropdown));
        wait.until(d -> new Select(dropdown).getOptions().size() > 0);
        Select select = new Select(dropdown);
        select.selectByIndex(index);
        String selectedAccount = select.getFirstSelectedOption().getText().trim();
        log.info("Selected From Account for Bill Pay: {}", selectedAccount);
        VideoRecorderUtil.captureStepFrame();
        return selectedAccount;
    }

    public void clickSendPayment() {
        log.info("Clicking Send Payment button");
        wait.until(ExpectedConditions.elementToBeClickable(sendPaymentButton)).click();
        VideoRecorderUtil.captureStepFrame();
    }

    public boolean isBillPaymentCompleteDisplayed() {
        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(billPayResultSection));
        VideoRecorderUtil.captureStepFrame();
        return result.isDisplayed();
    }

    public String getResultHeading() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(resultHeading)).getText().trim();
    }

    public String getConfirmedPayeeName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(payeeNameResult)).getText().trim();
    }

    public String getConfirmedAmount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(amountResult)).getText().trim();
    }

    public String getConfirmedFromAccount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(fromAccountResult)).getText().trim();
    }
}
