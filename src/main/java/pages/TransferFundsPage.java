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

public class TransferFundsPage {
    private static final Logger log = LoggerUtil.getLogger(TransferFundsPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Navigation & Form Locators
    private final By transferFundsLink = By.linkText("Transfer Funds");
    private final By amountField = By.id("amount");
    private final By fromAccountDropdown = By.id("fromAccountId");
    private final By toAccountDropdown = By.id("toAccountId");
    private final By transferButton = By.xpath("//input[@value='Transfer']");

    // Confirmation Locators
    private final By transferResultSection = By.id("showResult");
    private final By transferCompleteHeading = By.xpath("//div[@id='showResult']/h1");
    private final By transferredAmountResult = By.id("amountResult");
    private final By fromAccountResult = By.id("fromAccountIdResult");
    private final By toAccountResult = By.id("toAccountIdResult");

    public TransferFundsPage(WebDriver driver) {

        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public void navigateToTransferFundsPage() {

        log.info("Clicking Transfer Funds link");
        wait.until(ExpectedConditions.elementToBeClickable(transferFundsLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(amountField));
        VideoRecorderUtil.captureStepFrame();
    }

    public void enterAmount(String amount) {

        log.info("Entering transfer amount: ${}", amount);
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(amountField));
        input.clear();
        input.sendKeys(amount);
        VideoRecorderUtil.captureStepFrame();
    }

    public String selectSourceAccountByIndex(int index) {

        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(fromAccountDropdown));
        wait.until(d -> new Select(dropdown).getOptions().size() > 0);
        Select select = new Select(dropdown);
        select.selectByIndex(index);
        String chosenAccount = select.getFirstSelectedOption().getText().trim();
        log.info("Selected source (From) account: {}", chosenAccount);
        VideoRecorderUtil.captureStepFrame();
        return chosenAccount;
    }

    public void selectDestinationAccountByValue(String accountNumber) {

        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(toAccountDropdown));
        wait.until(d -> new Select(dropdown).getOptions().size() > 0);
        Select select = new Select(dropdown);
        select.selectByVisibleText(accountNumber);
        log.info("Selected destination (To) account: {}", accountNumber);
        VideoRecorderUtil.captureStepFrame();
    }

    public void clickTransferButton() {

        log.info("Clicking Transfer submit button");
        wait.until(ExpectedConditions.elementToBeClickable(transferButton)).click();
        VideoRecorderUtil.captureStepFrame();
    }

    public boolean isTransferCompleteDisplayed() {

        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(transferResultSection));
        VideoRecorderUtil.captureStepFrame();
        return result.isDisplayed();
    }

    public String getTransferCompleteHeading() {

        return wait.until(ExpectedConditions.visibilityOfElementLocated(transferCompleteHeading)).getText().trim();
    }

    public String getConfirmedAmount() {

        return wait.until(ExpectedConditions.visibilityOfElementLocated(transferredAmountResult)).getText().trim();
    }

    public String getConfirmedFromAccount() {

        return wait.until(ExpectedConditions.visibilityOfElementLocated(fromAccountResult)).getText().trim();
    }

    public String getConfirmedToAccount() {

        return wait.until(ExpectedConditions.visibilityOfElementLocated(toAccountResult)).getText().trim();
    }
}
