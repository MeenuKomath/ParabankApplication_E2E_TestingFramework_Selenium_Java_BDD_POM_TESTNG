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

    // Confirmation & Error Locators
    private final By billPayResultSection = By.id("billpayResult");
    private final By billPayErrorSection = By.id("billpayError");
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

        log.info("Entering Payee details from Excel map: {}", details);
        clearAndType(payeeNameField, details.getOrDefault("PayeeName", ""));
        clearAndType(addressField, details.getOrDefault("Address", ""));
        clearAndType(cityField, details.getOrDefault("City", ""));
        clearAndType(stateField, details.getOrDefault("State", ""));
        clearAndType(zipCodeField, details.getOrDefault("ZipCode", ""));
        clearAndType(phoneField, details.getOrDefault("Phone", ""));
        clearAndType(accountField, details.getOrDefault("Account", ""));
        clearAndType(verifyAccountField, details.getOrDefault("Account", ""));
        clearAndType(amountField, details.getOrDefault("Amount", ""));
        VideoRecorderUtil.captureStepFrame();
    }

    private void clearAndType(By locator, String value) {

        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
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

        log.info("Waiting for Bill Payment confirmation section");
        wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(billPayResultSection),
                ExpectedConditions.visibilityOfElementLocated(billPayErrorSection)));
        VideoRecorderUtil.captureStepFrame();

        if (!driver.findElements(billPayErrorSection).isEmpty() && driver.findElement(billPayErrorSection).isDisplayed()) {
            log.error("ParaBank returned a Bill Pay server error: {}", driver.findElement(billPayErrorSection).getText());
            return false;
        }
        return driver.findElement(billPayResultSection).isDisplayed();
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
