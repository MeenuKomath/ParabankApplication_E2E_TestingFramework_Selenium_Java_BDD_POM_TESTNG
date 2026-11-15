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

public class FindTransactionsPage {
    private static final Logger log = LoggerUtil.getLogger(FindTransactionsPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Navigation & Search Form Locators
    private final By findTransactionsLink = By.linkText("Find Transactions");
    private final By accountIdDropdown = By.id("accountId");
    private final By amountInput = By.id("amount");
    private final By findByAmountButton = By.id("findByAmount");
    private final By dateInput = By.id("transactionDate");
    private final By findByDateButton = By.id("findByDate");

    // Results Table Locators
    private final By resultContainer = By.id("resultContainer");
    private final By transactionRows = By.xpath("//table[@id='transactionTable']/tbody/tr");

    public FindTransactionsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToFindTransactionsPage() {
        log.info("Navigating to Find Transactions page");
        wait.until(ExpectedConditions.elementToBeClickable(findTransactionsLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountIdDropdown));
        VideoRecorderUtil.captureStepFrame();
    }

    public String selectAccountByIndex(int index) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(accountIdDropdown));
        wait.until(d -> new Select(dropdown).getOptions().size() > 0);
        Select select = new Select(dropdown);
        select.selectByIndex(index);
        String account = select.getFirstSelectedOption().getText().trim();
        log.info("Selected account for transaction search: {}", account);
        VideoRecorderUtil.captureStepFrame();
        return account;
    }

    public void searchByAmount(String amount) {
        log.info("Searching transactions by amount: {}", amount);
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(amountInput));
        input.clear();
        input.sendKeys(amount);
        wait.until(ExpectedConditions.elementToBeClickable(findByAmountButton)).click();
        VideoRecorderUtil.captureStepFrame();
    }

    public void searchByDate(String dateMMDDYYYY) {
        log.info("Searching transactions by date: {}", dateMMDDYYYY);
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(dateInput));
        input.clear();
        input.sendKeys(dateMMDDYYYY);
        wait.until(ExpectedConditions.elementToBeClickable(findByDateButton)).click();
        VideoRecorderUtil.captureStepFrame();
    }

    public boolean areTransactionResultsDisplayed() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(resultContainer));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(transactionRows));
        VideoRecorderUtil.captureStepFrame();
        return !driver.findElements(transactionRows).isEmpty();
    }

    public List<String> getTransactionRowTexts() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(transactionRows));
        List<String> rows = driver.findElements(transactionRows)
                .stream()
                .map(row -> row.getText().trim())
                .collect(Collectors.toList());
        log.info("Found {} transaction record(s): {}", rows.size(), rows);
        return rows;
    }
    // Add this method inside FindTransactionsPage.java:
    public void selectAccountByValue(String accountNumber) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(accountIdDropdown));
        wait.until(d -> new Select(dropdown).getOptions().size() > 0);
        Select select = new Select(dropdown);
        select.selectByVisibleText(accountNumber);
        log.info("Selected exact account for transaction search: {}", accountNumber);
        VideoRecorderUtil.captureStepFrame();
    }

}
