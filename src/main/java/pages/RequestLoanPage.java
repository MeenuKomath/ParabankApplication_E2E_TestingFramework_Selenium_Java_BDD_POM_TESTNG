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

public class RequestLoanPage {
    private static final Logger log = LoggerUtil.getLogger(RequestLoanPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Navigation & Form Locators
    private final By requestLoanLink = By.linkText("Request Loan");
    private final By loanAmountField = By.id("amount");
    private final By downPaymentField = By.id("downPayment");
    private final By fromAccountDropdown = By.id("fromAccountId");
    private final By applyNowButton = By.xpath("//input[@value='Apply Now']");

    // Confirmation Locators
    private final By requestLoanResultSection = By.id("requestLoanResult");
    private final By resultHeading = By.xpath("//div[@id='requestLoanResult']/h1");
    private final By loanStatusText = By.id("loanStatus");
    private final By newAccountIdLink = By.id("newAccountId");

    public RequestLoanPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToRequestLoanPage() {
        log.info("Navigating to Request Loan page");
        wait.until(ExpectedConditions.elementToBeClickable(requestLoanLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(loanAmountField));
        VideoRecorderUtil.captureStepFrame();
    }

    public void enterLoanDetails(String loanAmount, String downPayment) {
        log.info("Entering Loan Amount: ${}, Down Payment: ${}", loanAmount, downPayment);
        WebElement amountInput = wait.until(ExpectedConditions.visibilityOfElementLocated(loanAmountField));
        amountInput.clear();
        amountInput.sendKeys(loanAmount);

        WebElement downPaymentInput = driver.findElement(downPaymentField);
        downPaymentInput.clear();
        downPaymentInput.sendKeys(downPayment);
        VideoRecorderUtil.captureStepFrame();
    }

    public void selectFromAccountByIndex(int index) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(fromAccountDropdown));
        wait.until(d -> new Select(dropdown).getOptions().size() > 0);
        Select select = new Select(dropdown);
        select.selectByIndex(index);
        log.info("Selected From Account for Loan: {}", select.getFirstSelectedOption().getText().trim());
        VideoRecorderUtil.captureStepFrame();
    }

    public void clickApplyNowButton() {
        log.info("Clicking Apply Now button");
        wait.until(ExpectedConditions.elementToBeClickable(applyNowButton)).click();
        VideoRecorderUtil.captureStepFrame();
    }

    public boolean isLoanResultDisplayed() {
        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(requestLoanResultSection));
        VideoRecorderUtil.captureStepFrame();
        return result.isDisplayed();
    }

    public String getResultHeading() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(resultHeading)).getText().trim();
    }

    public String getLoanStatus() {
        String status = wait.until(ExpectedConditions.visibilityOfElementLocated(loanStatusText)).getText().trim();
        log.info("Loan Application Status: {}", status);
        VideoRecorderUtil.captureStepFrame();
        return status;
    }

    public String getNewLoanAccountNumber() {
        String accountId = wait.until(ExpectedConditions.visibilityOfElementLocated(newAccountIdLink)).getText().trim();
        log.info("Generated Loan Account Number: {}", accountId);
        VideoRecorderUtil.captureStepFrame();
        return accountId;
    }
}
